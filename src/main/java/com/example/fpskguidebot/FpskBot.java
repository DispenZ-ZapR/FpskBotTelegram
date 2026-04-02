package com.example.fpskguidebot;

import com.example.fpskguidebot.enums.Language;
import com.example.fpskguidebot.model.UserRequest;
import com.example.fpskguidebot.repository.UserRequestRepository;
import com.example.fpskguidebot.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
@RequiredArgsConstructor
public class FpskBot extends TelegramLongPollingBot {

    private final CommandService commandService;
    private final RequestService requestService;
    private final UserRequestRepository userRequestRepository;
    private final UserLanguageService userLanguageService;
    private final UserStateService userStateService;
    private final MessageService messageService;

    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.username:@FPSKGuide_bot}")
    private String botUsername;

    @Value("${operator.chat.id:1083645714}")
    private Long operatorChatId;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                handleMessage(update);
            } else if (update.hasCallbackQuery()) {
                handleCallback(update);
            }
        } catch (Exception e) {
            log.error("Error processing update: {}", e.getMessage(), e);
        }
    }

    private void handleMessage(Update update) throws TelegramApiException {
        String messageText = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();
        
        // Проверяем, ожидает ли пользователь ввод сообщения для обращения
        if (userStateService.isUserWaitingForRequest(chatId)) {
            handleUserMessage(chatId, update);
            return;
        }
        
        SendMessage message = switch (messageText) {
            case "/start" -> commandService.handleStartCommand(chatId);
            case "/help" -> commandService.handleHelpCommand(chatId);
            case "/lang" -> createLanguageSelectionMessage(chatId);

            case "📖 О Федерации" -> commandService.handleGuideCommand(chatId);
            case "ℹ️ Контакты" -> commandService.handleInfoCommand(chatId, Language.RU);
            case "📰 Новости" -> commandService.handleNews(chatId);
            case "📞 Обращение" -> startSupportRequest(chatId, update);

            case "📖 Федерация жөнүндө" -> commandService.handleGuideCommand(chatId);
            case "ℹ️ Байланыштар" -> commandService.handleInfoCommand(chatId, Language.KG);
            case "📰 Жаңылыктар" -> commandService.handleNews(chatId);
            case "📞 Кайрылуу" -> startSupportRequest(chatId, update);

            case "📖 About Federation" -> commandService.handleGuideCommand(chatId);
            case "ℹ️ Contacts" -> commandService.handleInfoCommand(chatId, Language.EN);
            case "📰 News" -> commandService.handleNews(chatId);
            case "📞 Statement" -> startSupportRequest(chatId, update);
            default -> {
                if (messageText.startsWith("/answer") && chatId == operatorChatId) {
                    log.info("Operator {} processing answer command: {}", chatId, messageText);
                    yield requestService.createAnswerCommandResponse(messageText, this);
                } else if (messageText.equals("/list") && chatId == operatorChatId) {
                    log.info("Operator {} processing list command", chatId);
                    yield requestService.createListCommandResponse();
                } else if (messageText.startsWith("/answer")) {
                    log.warn("Non-operator {} tried to use answer command: {}", chatId, messageText);
                    SendMessage errorMsg = new SendMessage();
                    errorMsg.setChatId(String.valueOf(chatId));
                    errorMsg.setText("Эта команда доступна только операторам");
                    yield errorMsg;
                } else if (messageText.contains("🌐") && (messageText.contains("Язык") || messageText.contains("Тил") || messageText.contains("Language"))) {
                    yield createLanguageSelectionMessage(chatId);
                } else {
                    yield createDefaultResponse(chatId, messageText);
                }
            }
        };
        
        execute(message);
    }

    private void handleCallback(Update update) throws TelegramApiException {
        String callbackData = update.getCallbackQuery().getData();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        
        SendMessage message = commandService.handleCallbackQuery(chatId, callbackData);
        execute(message);
    }

    private SendMessage createDefaultResponse(long chatId, String messageText) {
        Language userLang = userLanguageService.getUserLanguage(chatId);
        
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(messageService.getUnknownCommandMessage(userLang));
        return message;
    }

    private SendMessage createLanguageSelectionMessage(long chatId) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("🌐 Пожалуйста, выберите язык / Тилди тандаңыз / Please select language:");
        
        // Создаем inline клавиатуру для выбора языка
        org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup markupInline = 
            new org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup();
        java.util.List<java.util.List<org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton>> rowsInline = 
            new java.util.ArrayList<>();

        // Русский язык
        org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton ruButton = 
            new org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton();
        ruButton.setText("🇷🇺 Русский");
        ruButton.setCallbackData("lang_ru");

        // Кыргызский язык
        org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton kgButton = 
            new org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton();
        kgButton.setText("🇰🇬 Кыргызча");
        kgButton.setCallbackData("lang_ky");

        // Английский язык
        org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton enButton = 
            new org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton();
        enButton.setText("🇬🇧 English");
        enButton.setCallbackData("lang_en");

        java.util.List<org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton> row1 = new java.util.ArrayList<>();
        row1.add(ruButton);
        row1.add(kgButton);

        java.util.List<org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton> row2 = new java.util.ArrayList<>();
        row2.add(enButton);

        rowsInline.add(row1);
        rowsInline.add(row2);

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        
        return message;
    }
    
    private SendMessage startSupportRequest(long chatId, Update update) {
        Language userLang = userLanguageService.getUserLanguage(chatId);
        
        // Проверяем rate limiting
        if (!requestService.canCreateRequest(chatId)) {
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText(messageService.getRateLimitMessage(
                userLang, 
                requestService.getMaxRequestsPerHour(), 
                1
            ));
            return message;
        }
        
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(messageService.getRequestPromptMessage(userLang));
        
        // Создаем inline клавиатуру с кнопкой отмены
        org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup markup = 
            new org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup();
        java.util.List<java.util.List<org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton>> rows = 
            new java.util.ArrayList<>();
        
        org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton cancelButton = 
            new org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton();
        cancelButton.setText(messageService.getCancelButtonText(userLang));
        cancelButton.setCallbackData("cancel_request");
        
        java.util.List<org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton> row = 
            new java.util.ArrayList<>();
        row.add(cancelButton);
        rows.add(row);
        
        markup.setKeyboard(rows);
        message.setReplyMarkup(markup);
        
        // Устанавливаем состояние пользователя
        userStateService.setUserState(chatId, UserStateService.STATE_WAITING_FOR_REQUEST);
        
        return message;
    }
    
    private void handleUserMessage(long chatId, Update update) throws TelegramApiException {
        String messageText = update.getMessage().getText();
        Language userLang = userLanguageService.getUserLanguage(chatId);
        
        // Проверка длины сообщения
        if (messageText.length() > requestService.getMaxMessageLength()) {
            SendMessage errorMessage = new SendMessage();
            errorMessage.setChatId(String.valueOf(chatId));
            errorMessage.setText(messageService.getRequestTooLongMessage(
                userLang, 
                requestService.getMaxMessageLength()
            ));
            execute(errorMessage);
            return;
        }
        
        // Создаем обращение в БД
        String username = update.getMessage().getFrom().getUserName();
        String firstName = update.getMessage().getFrom().getFirstName();
        
        UserRequest savedRequest = requestService.createRequest(
            chatId, username, firstName, messageText, userLang
        );
        
        // Отправляем уведомление оператору
        requestService.sendNotificationToOperator(savedRequest, this);
        
        // Очищаем состояние пользователя
        userStateService.clearUserState(chatId);
        
        SendMessage confirmMessage = new SendMessage();
        confirmMessage.setChatId(String.valueOf(chatId));
        confirmMessage.setText(messageService.getRequestConfirmationMessage(userLang, messageText));
        execute(confirmMessage);
    }
}
