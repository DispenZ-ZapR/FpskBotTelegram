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

    public UserRequest createRequest(long chatId, String username, String firstName, String messageText, Language language) {
        UserRequest request = new UserRequest();
        request.setChatId(chatId);
        request.setUsername(username);
        request.setFirstName(firstName);
        request.setMessageText(messageText);
        request.setRequestDate(LocalDateTime.now());
        request.setStatus("PENDING");
        request.setLanguage(language.name());

        UserRequest savedRequest = userRequestRepository.save(request);
        
        log.info("Created new request #{} from chat {}", savedRequest.getId(), chatId);
        return savedRequest;
    }

    public void sendNotificationToOperator(UserRequest request, org.telegram.telegrambots.bots.TelegramLongPollingBot bot) {
        try {
            SendMessage notification = new SendMessage();
            notification.setChatId(String.valueOf(operatorChatId));
            
            String message = String.format(
                "Новое обращение #%d\n\n" +
                "Пользователь: %s (%s)\n" +
                "Сообщение: %s\n" +
                "Время: %s\n\n" +
                "Для ответа используйте команду:\n" +
                "/answer %d [ваш ответ]",
                request.getId(),
                request.getFirstName() != null ? request.getFirstName() : "Аноним",
                request.getUsername() != null ? "@" + request.getUsername() : "no username",
                request.getMessageText(),
                request.getRequestDate().toString().substring(0, 19),
                request.getId()
            );
            
            notification.setText(message);
            
            bot.execute(notification);
            
        } catch (Exception e) {
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
            return false;
        }

        request.setOperatorResponse(responseText);
        request.setResponseDate(LocalDateTime.now());
        request.setOperatorName(operatorName);
        request.setStatus("ANSWERED");

        userRequestRepository.save(request);
        
        // Отправляем ответ пользователю
        try {
            SendMessage userMessage = new SendMessage();
            userMessage.setChatId(String.valueOf(request.getChatId()));
            userMessage.setText("Ответ оператора:\n\n" + responseText + 
                              "\n\n---\nЕсли у вас есть еще вопросы, нажмите 'Обращение' снова.");
            
            log.info("Sending response to user chatId: {}, requestId: {}", request.getChatId(), requestId);
            bot.execute(userMessage);
            log.info("Response sent successfully to user");
        } catch (Exception e) {
            log.error("Error sending response to user: {}", e.getMessage(), e);
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
                response.setText("Неверный формат команды.\nИспользуйте: /answer [id] [текст ответа]");
                return response;
            }

            Long requestId = Long.parseLong(parts[1]);
            String answerText = parts[2];

            if (answerRequest(requestId, answerText, "Operator", bot)) {
                response.setText("Ответ на обращение #" + requestId + " отправлен!");
            } else {
                response.setText("Не удалось найти обращение #" + requestId + " или оно уже обработано");
            }
            
        } catch (NumberFormatException e) {
            response.setText("Неверный ID обращения. Используйте: /answer [id] [текст ответа]");
        } catch (Exception e) {
            response.setText("Ошибка при обработке команды: " + e.getMessage());
        }

        return response;
    }

    public SendMessage createListCommandResponse() {
        List<UserRequest> pendingRequests = getPendingRequests();
        SendMessage response = new SendMessage();
        response.setChatId(String.valueOf(operatorChatId));
        
        if (pendingRequests.isEmpty()) {
            response.setText("Нет ожидающих обращений");
            return response;
        }

        StringBuilder message = new StringBuilder("Ожидающие обращения:\n\n");
        
        for (UserRequest request : pendingRequests) {
            message.append(String.format(
                "#%d - %s\nПользователь: %s\nСообщение: %s\nВремя: %s\n\n",
                request.getId(),
                request.getStatus(),
                request.getFirstName() != null ? request.getFirstName() : "Аноним",
                request.getMessageText().length() > 50 ? 
                    request.getMessageText().substring(0, 50) + "..." : 
                    request.getMessageText(),
                request.getRequestDate().toString()
            ));
        }

        response.setText(message.toString());
        return response;
    }
}
