package com.alexander.orshadiarybot.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Configuration
@Data
@ConfigurationProperties(prefix = "quoter")

public class QuoterProperty {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate quoterTwoBegin;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate quoterThreeBegin;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate quoterFourBegin;
}
