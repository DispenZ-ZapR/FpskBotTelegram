package com.example.fpskguidebot.service;

import com.example.fpskguidebot.enums.Language;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommandService {

    private final UserLanguageService userLanguageService;
    private final MessageService messageService;

    public CommandService(UserLanguageService userLanguageService, MessageService messageService) {
        this.userLanguageService = userLanguageService;
        this.messageService = messageService;
    }

    public SendMessage handleStartCommand(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        
        if (!userLanguageService.hasUserLanguage(chatId)) {
            message.setText(messageService.getLanguageSelectionMessage());
            message.setReplyMarkup(createLanguageSelectionKeyboard());
        } else {
            Language userLang = userLanguageService.getUserLanguage(chatId);
            message.setText(messageService.getStartMessage(userLang) + "\n\n" + messageService.getWelcomeMessage(userLang));
            message.setReplyMarkup(createMainMenu(userLang));
        }
        
        return message;
    }

    public SendMessage handleHelpCommand(long chatId) {
        Language userLang = userLanguageService.getUserLanguage(chatId);
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(messageService.getHelpMessage(userLang));
        message.enableMarkdown(true);
        return message;
    }

    public SendMessage handleInfoCommand(long chatId, Language language) {
        Language userLang = userLanguageService.getUserLanguage(chatId);
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(messageService.getContactsMessage(userLang));
        message.setReplyMarkup(createSocialMedia(language));
        message.setParseMode("HTML");
        return message;
    }

    public SendMessage handleGuideCommand(long chatId) {
        Language userLang = userLanguageService.getUserLanguage(chatId);
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(messageService.getAboutFederationMessage(userLang));
        message.setReplyMarkup(createGuideInlineKeyboard(userLang));
        message.enableMarkdown(true);
        return message;
    }

    public SendMessage handleNews(long chatId) {
        Language userLang = userLanguageService.getUserLanguage(chatId);
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(messageService.getNewsMessage(userLang));
        message.setReplyMarkup(createNewsKeyboard(userLang));
        message.enableMarkdown(true);
        return message;
    }

    public SendMessage handleMoreInfoCommand(long chatId) {
        Language userLang = userLanguageService.getUserLanguage(chatId);
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(messageService.getLeadershipMessage(userLang));
        message.enableMarkdown(true);
        return message;
    }

    private ReplyKeyboardMarkup createMainMenu(Language language) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        String[] menuButtons = messageService.getMenuButtons(language);

        KeyboardRow row1 = new KeyboardRow();
        row1.add(menuButtons[0]);
        row1.add(menuButtons[1]);

        KeyboardRow row2 = new KeyboardRow();
        row2.add(menuButtons[2]);
        row2.add("🌐 " + getLanguageButtonText(language));
        KeyboardRow row3 = new KeyboardRow();
        row3.add(menuButtons[3]);
        keyboard.add(row3);
        keyboard.add(row1);
        keyboard.add(row2);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    private String getLanguageButtonText(Language language) {
        return switch (language) {
            case RU -> "Язык";
            case KG -> "Тил";
            case EN -> "Language";
        };
    }

    private InlineKeyboardMarkup createLanguageSelectionKeyboard() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        // Русский язык
        InlineKeyboardButton ruButton = new InlineKeyboardButton();
        ruButton.setText("🇷🇺 Русский");
        ruButton.setCallbackData("lang_ru");

        // Кыргызский язык
        InlineKeyboardButton kgButton = new InlineKeyboardButton();
        kgButton.setText("🇰🇬 Кыргызча");
        kgButton.setCallbackData("lang_ky");

        // Английский язык
        InlineKeyboardButton enButton = new InlineKeyboardButton();
        enButton.setText("🇬🇧 English");
        enButton.setCallbackData("lang_en");

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(ruButton);
        row1.add(kgButton);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(enButton);

        rowsInline.add(row1);
        rowsInline.add(row2);

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    public SendMessage handleLanguageSelection(long chatId, String languageCode) {
        Language selectedLanguage = Language.fromCode(languageCode);
        userLanguageService.setUserLanguage(chatId, selectedLanguage);
        
        return handleStartCommand(chatId);
    }
    private InlineKeyboardMarkup createSocialMedia(Language language){
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        button1.setText("Instagram");
        button1.setUrl("https://www.instagram.com/fpsk.kgz/?igsh=cHpzNWJ5eG01czN4");

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Facebook");
        button2.setUrl("https://www.facebook.com/profile.php?id=61557416135754&mibextid=ZbWKwL");

        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText("YouTube");
        button3.setUrl("https://www.youtube.com/@trade_union_kg");

        row1.add(button1);
        row1.add(button2);
        row1.add(button3);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button4.setText(messageService.getSite(language));
        button4.setUrl(messageService.getSiteUrl(language));
        row2.add(button4);

        rowsInline.add(row1);
        rowsInline.add(row2);

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    private InlineKeyboardMarkup createGuideInlineKeyboard(Language language) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        InlineKeyboardButton moreInfoButton = new InlineKeyboardButton();
        moreInfoButton.setText(messageService.getLeadershipButtonText(language));
        moreInfoButton.setCallbackData("moreInfo");

        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(moreInfoButton);
        rowsInline.add(row);

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    private InlineKeyboardMarkup createNewsKeyboard(Language language) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        InlineKeyboardButton websiteButton = new InlineKeyboardButton();
        websiteButton.setText(messageService.getNewsButtonText(language));
        websiteButton.setUrl(messageService.getNewsSite(language));

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(websiteButton);
        rowsInline.add(row1);

        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
    public SendMessage sendWhatsAppMessage(long chatId) {
        Language userLanguage = userLanguageService.getUserLanguage(chatId);
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(messageService.getStatement(userLanguage));
        message.setReplyMarkup(createWhatsAppKeyboard());
        return message;
    }
    public InlineKeyboardMarkup createWhatsAppKeyboard() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        InlineKeyboardButton whatsAppButton = new InlineKeyboardButton();
        whatsAppButton.setText("WhatsApp");
        whatsAppButton.setUrl("https://wa.me/996997557730");
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(whatsAppButton);
        rowsInline.add(row1);
        markup.setKeyboard(rowsInline);
        return markup;
    }
    public SendMessage handleCallbackQuery(long chatId, String callbackData) {
        if (callbackData.startsWith("lang_")) {
            String languageCode = callbackData.substring(5);
            return handleLanguageSelection(chatId, languageCode);
        }

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        Language userLang = userLanguageService.getUserLanguage(chatId);

        switch (callbackData) {
            case "guide_basics":
                message.setText("🎯 *Основы работы*\n\n" +
                        "1. Используйте кнопки для навигации\n" +
                        "2. Отправляйте текст для получения информации\n" +
                        "3. Используйте команды для быстрых действий");
                break;
            case "guide_docs":
                message.setText("📚 *Документация*\n\n" +
                        "Полная документация доступна на нашем сайте:\n" +
                        "🔗 [Ссылка на документацию](https://example.com)");
                break;
            case "back_to_main":
                return handleStartCommand(chatId);
            case "moreInfo":
                return handleMoreInfoCommand(chatId);
            default:
                message.setText("❌ " + messageService.getUnknownCommandMessage(userLang));
        }

        message.enableMarkdown(true);
        return message;
    }
}
