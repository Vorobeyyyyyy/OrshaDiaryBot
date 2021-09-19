package com.alexander.orshadiarybot.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "url")
@Data
public class UrlProperty {
    private String login;
    private String marks;
}
