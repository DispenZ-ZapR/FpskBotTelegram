package com.example.fpskguidebot.service;

import com.example.fpskguidebot.enums.Language;
import com.example.fpskguidebot.model.UserRequest;
import com.example.fpskguidebot.repository.UserRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestService {

    private final UserRequestRepository userRequestRepository;
    private final UserLanguageService userLanguageService;
    private final MessageService messageService;

    @Value("${operator.chat.id:1083645714}")
    private Long operatorChatId;
    
    private static final int MAX_REQUESTS_PER_HOUR = 3;
    private static final int MAX_MESSAGE_LENGTH = 1000;

    public boolean canCreateRequest(Long chatId) {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        long recentRequests = userRequestRepository.countByChatIdAndRequestDateAfter(chatId, oneHourAgo);
        return recentRequests < MAX_REQUESTS_PER_HOUR;
    }

    public UserRequest createRequest(long chatId, String username, String firstName, String messageText, Language language) {
        UserRequest request = new UserRequest();
        request.setChatId(chatId);
        request.setUsername(username);
        request.setFirstName(firstName);
        request.setMessageText(messageText);
        request.setRequestDate(LocalDateTime.now());
        request.setStatus("PENDING");
        request.setLanguage(language.name());
        request.setIsRead(false);

        UserRequest savedRequest = userRequestRepository.save(request);
        
        log.info("Created new request #{} from chat {}", savedRequest.getId(), chatId);
        return savedRequest;
    }

    public void sendNotificationToOperator(UserRequest request, org.telegram.telegrambots.bots.TelegramLongPollingBot bot) {
        try {
            SendMessage notification = new SendMessage();
            notification.setChatId(String.valueOf(operatorChatId));
            
            String message = String.format(
                "🔔 Новое обращение #%d\n\n" +
                "👤 Пользователь: %s (%s)\n" +
                "🌐 Язык: %s\n" +
                "💬 Сообщение: %s\n" +
                "🕒 Время: %s\n\n" +
                "⚙️ Для ответа используйте:\n" +
                "/answer %d [ваш ответ]",
                request.getId(),
                request.getFirstName() != null ? request.getFirstName() : "Аноним",
                request.getUsername() != null ? "@" + request.getUsername() : "no username",
                request.getLanguage(),
                request.getMessageText(),
                request.getRequestDate().toString().substring(0, 19).replace('T', ' '),
                request.getId()
            );
            
            notification.setText(message);
            bot.execute(notification);
            
            log.info("Notification sent to operator for request #{}", request.getId());
            
        } catch (TelegramApiException e) {
            log.error("Error sending notification to operator: {}", e.getMessage(), e);
        }
    }

    public List<UserRequest> getPendingRequests() {
        return userRequestRepository.findByStatusOrderByRequestDateDesc("PENDING");
    }

    public UserRequest getRequestById(Long id) {
        return userRequestRepository.findById(id).orElse(null);
    }

    public boolean answerRequest(Long requestId, String responseText, String operatorName, org.telegram.telegrambots.bots.TelegramLongPollingBot bot) {
        UserRequest request = getRequestById(requestId);
        if (request == null || !"PENDING".equals(request.getStatus())) {
            log.warn("Cannot answer request #{}: not found or already answered", requestId);
            return false;
        }

        request.setOperatorResponse(responseText);
        request.setResponseDate(LocalDateTime.now());
        request.setOperatorName(operatorName);
        request.setStatus("ANSWERED");
        request.setIsRead(true);

        userRequestRepository.save(request);
        
        // Отправляем ответ пользователю
        try {
            Language userLang = Language.valueOf(request.getLanguage());
            String message = messageService.getOperatorResponseMessage(userLang, responseText);
            
            SendMessage userMessage = new SendMessage();
            userMessage.setChatId(String.valueOf(request.getChatId()));
            userMessage.setText(message);
            
            log.info("Sending response to user chatId: {}, requestId: {}", request.getChatId(), requestId);
            bot.execute(userMessage);
            log.info("Response sent successfully to user");
        } catch (TelegramApiException e) {
            log.error("Error sending response to user: {}", e.getMessage(), e);
            return false;
        } catch (IllegalArgumentException e) {
            log.error("Invalid language in request: {}", request.getLanguage(), e);
        }
        
        log.info("Request #{} answered by {}", requestId, operatorName);
        return true;
    }

    public SendMessage createAnswerCommandResponse(String commandText, org.telegram.telegrambots.bots.TelegramLongPollingBot bot) {
        SendMessage response = new SendMessage();
        response.setChatId(String.valueOf(operatorChatId));
        
        try {
            String[] parts = commandText.split(" ", 3);
            if (parts.length < 3) {
                response.setText("⚠️ Неверный формат команды.\nИспользуйте: /answer [id] [текст ответа]");
                return response;
            }

            Long requestId = Long.parseLong(parts[1]);
            String answerText = parts[2];

            if (answerRequest(requestId, answerText, "Operator", bot)) {
                response.setText("✅ Ответ на обращение #" + requestId + " отправлен!");
            } else {
                response.setText("❌ Не удалось найти обращение #" + requestId + " или оно уже обработано");
            }
            
        } catch (NumberFormatException e) {
            response.setText("⚠️ Неверный ID обращения. Используйте: /answer [id] [текст ответа]");
            log.warn("Invalid request ID format in command: {}", commandText);
        } catch (Exception e) {
            response.setText("❌ Ошибка при обработке команды: " + e.getMessage());
            log.error("Error processing answer command: {}", commandText, e);
        }

        return response;
    }

    public SendMessage createListCommandResponse() {
        List<UserRequest> pendingRequests = getPendingRequests();
        SendMessage response = new SendMessage();
        response.setChatId(String.valueOf(operatorChatId));
        
        if (pendingRequests.isEmpty()) {
            response.setText("✅ Нет ожидающих обращений");
            return response;
        }

        StringBuilder message = new StringBuilder("📋 Ожидающие обращения (" + pendingRequests.size() + "):\n\n");
        
        for (UserRequest request : pendingRequests) {
            String readStatus = Boolean.TRUE.equals(request.getIsRead()) ? "✅" : "🔴";
            message.append(String.format(
                "%s #%d - %s\n" +
                "👤 %s | 🌐 %s\n" +
                "💬 %s\n" +
                "🕒 %s\n" +
                "---\n",
                readStatus,
                request.getId(),
                request.getStatus(),
                request.getFirstName() != null ? request.getFirstName() : "Аноним",
                request.getLanguage(),
                request.getMessageText().length() > 50 ? 
                    request.getMessageText().substring(0, 50) + "..." : 
                    request.getMessageText(),
                request.getRequestDate().toString().substring(0, 16).replace('T', ' ')
            ));
        }

        response.setText(message.toString());
        return response;
    }
    
    public int getMaxMessageLength() {
        return MAX_MESSAGE_LENGTH;
    }
    
    public int getMaxRequestsPerHour() {
        return MAX_REQUESTS_PER_HOUR;
    }
}
