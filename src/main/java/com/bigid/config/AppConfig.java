package com.bigid.config;

public class AppConfig {
    private static AppConfig INSTANCE;

    private AppConfig() {}

    public static AppConfig getInstance() {
        if (INSTANCE == null) INSTANCE = new AppConfig();
        return INSTANCE;
    }

    public String getMatcherType() {
        return "simple";
    }
}
