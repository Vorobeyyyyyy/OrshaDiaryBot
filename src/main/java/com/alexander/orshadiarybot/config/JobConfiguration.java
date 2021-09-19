package com.alexander.orshadiarybot.config;

import com.alexander.orshadiarybot.config.property.BotProperty;
import com.alexander.orshadiarybot.job.UpdateMarksJob;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
@AllArgsConstructor
public class JobConfiguration implements SchedulingConfigurer {
    private UpdateMarksJob updateMarksJob;
    private BotProperty botProperty;

    @Bean
    public Executor taskExecutor() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
        taskRegistrar.addTriggerTask(updateMarksJob, context -> {
            Date lastExecution = context.lastActualExecutionTime();
            Date lastCompletion = context.lastCompletionTime();
            if (lastExecution == null || lastCompletion == null) {
                return new Date();
            }
            if (lastCompletion.getTime() - lastExecution.getTime() > botProperty.getUpdateMarksPeriod().toMillis()) {
                return new Date();
            }
            return Date.from(lastExecution.toInstant().plus(botProperty.getUpdateMarksPeriod()));
        });
    }
}
