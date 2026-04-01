package com.example.fpskguidebot.config;

import com.example.fpskguidebot.FpskBot;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import jakarta.annotation.PostConstruct;

@Configuration
public class BotConfig {

    private final FpskBot fpskBot;

    public BotConfig(FpskBot fpskBot) {
        this.fpskBot = fpskBot;
    }

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(fpskBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
