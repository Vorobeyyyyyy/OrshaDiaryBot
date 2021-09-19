package com.alexander.orshadiarybot.command;

import com.alexander.orshadiarybot.model.dto.base.StagedCommandDto;
import com.pengrad.telegrambot.model.Update;

import java.util.ArrayList;
import java.util.List;

public abstract class StagedCommand<T extends StagedCommandDto> extends Command {
    public interface StageHandler<T> {
        void handle(long chatId, T dto, Update update);
    }
    protected List<StageHandler<T>> consumers = new ArrayList<>();

    protected abstract void initializeConsumers();
}
