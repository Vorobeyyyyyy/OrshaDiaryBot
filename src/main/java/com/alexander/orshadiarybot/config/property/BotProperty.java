package com.alexander.orshadiarybot.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "bot")
@Data
public class BotProperty {
    private String token;

    private Duration updateMarksPeriod;
}
