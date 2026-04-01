package com.example.fpskguidebot.service;

import com.example.fpskguidebot.enums.Language;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserLanguageService {
    
    private final Map<Long, Language> userLanguages = new HashMap<>();
    
    public Language getUserLanguage(long chatId) {
        return userLanguages.getOrDefault(chatId, Language.RU);
    }
    
    public void setUserLanguage(long chatId, Language language) {
        userLanguages.put(chatId, language);
    }
    
    public boolean hasUserLanguage(long chatId) {
        return userLanguages.containsKey(chatId);
    }
}
