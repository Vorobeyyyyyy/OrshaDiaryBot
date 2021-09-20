package com.alexander.orshadiarybot.controller;

import com.alexander.orshadiarybot.command.Command;
import com.alexander.orshadiarybot.command.CommandResolver;
import com.alexander.orshadiarybot.config.property.MessageProperty;
import com.alexander.orshadiarybot.exception.BotException;
import com.alexander.orshadiarybot.listener.AnswerListener;
import com.alexander.orshadiarybot.model.dto.base.StagedCommandDto;
import com.alexander.orshadiarybot.service.ChatService;
import com.alexander.orshadiarybot.service.impl.ChatServiceImpl;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Log4j2
@Component
@AllArgsConstructor
public class BotController {
    private TelegramBot bot;
    private CommandResolver commandResolver;
    private AnswerListener answerListener;
    private MessageProperty messageProperty;
    private ChatService chatService;

    @PostConstruct
    private void configureBot() {
        bot.setUpdatesListener(this::updatesListener);
    }

    private Integer updatesListener(List<Update> updates) {
        updates.forEach(update -> new Thread(() -> handleUpdate(update)).start());
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void handleUpdate(Update update) {
        boolean isCallback = update.callbackQuery() != null;
        long chatId = !isCallback ? update.message().chat().id() : update.callbackQuery().message().chat().id();
        try {
            if (isCallback) {
                int commandIndex = StagedCommandDto.fromByteString(update.callbackQuery().data()).getCommandIndex();
                log.info("Its callback with command index: {}", commandIndex);
                commandResolver
                        .getCommand(commandIndex)
                        .orElseThrow(() -> new BotException(messageProperty.getUnknownCommandMessage()))
                        .handle(chatId, update);
                return;
            }
            if (answerListener.answer(chatId, update)) {
                return;
            }
            String action = update.message() != null ? update.message().text() : update.callbackQuery().data();
            Command command = commandResolver.getCommand(action).orElseThrow(() -> new BotException(messageProperty.getUnknownCommandMessage()));
            command.handle(chatId, update);
        } catch (BotException exception) {
            chatService.sendMessage(chatId, exception.getMessage());
        }
    }

}
