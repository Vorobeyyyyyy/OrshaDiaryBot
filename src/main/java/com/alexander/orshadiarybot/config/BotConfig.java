package com.alexander.orshadiarybot.config;

import com.alexander.orshadiarybot.config.property.BotProperty;
import com.pengrad.telegrambot.TelegramBot;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class BotConfig {
    private BotProperty botProperty;

    @Bean
    public TelegramBot bot() {
        return new TelegramBot(botProperty.getToken());
    }
}
