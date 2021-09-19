package com.alexander.orshadiarybot.model.dto;

import com.pengrad.telegrambot.model.Update;

import java.util.Optional;


public class AnswerUpdate {
    private Update update;

    public synchronized Optional<Update> get(Long timeInMillis) {
        try {
            wait(timeInMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(update);
    }

    public synchronized void setUpdate(Update update) {
        this.update = update;
        notify();
    }
}
