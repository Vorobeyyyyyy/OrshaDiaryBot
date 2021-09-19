package com.alexander.orshadiarybot.command;

import com.pengrad.telegrambot.model.Update;
import lombok.Data;

@Data
public abstract class Command {
    protected int index;

    public abstract void handle(long chatId, Update update);

    public abstract String getCommand();

    final void setIndex(int index) {
        this.index = index;
    }
}