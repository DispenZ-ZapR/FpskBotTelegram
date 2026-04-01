package com.example.fpskguidebot.enums;

public enum Language {
    RU("Русский", "ru"),
    KG("Кыргызча", "ky"),
    EN("English", "en");

    private final String displayName;
    private final String code;

    Language(String displayName, String code) {
        this.displayName = displayName;
        this.code = code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCode() {
        return code;
    }

    public static Language fromCode(String code) {
        for (Language lang : values()) {
            if (lang.code.equals(code)) {
                return lang;
            }
        }
        return RU; // по умолчанию русский
    }
}
