package com.alexander.orshadiarybot.command.impl;

import com.alexander.orshadiarybot.command.Command;
import com.alexander.orshadiarybot.config.property.MessageProperty;
import com.alexander.orshadiarybot.model.dto.base.CommandDto;
import com.alexander.orshadiarybot.model.dto.base.StagedCommandDto;
import com.alexander.orshadiarybot.service.ChatService;
import com.alexander.orshadiarybot.service.impl.ChatServiceImpl;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@AllArgsConstructor
public class StartCommand extends Command {
    private ChatService chatService;
    private MessageProperty messageProperty;
    private ShowMarksCommand showMarksCommand;
    private AddAccountCommand addAccountCommand;
    private ListAccountsCommand listAccountsCommand;

    @Override
    public void handle(long chatId, Update update) {
        Keyboard keyboard = new InlineKeyboardMarkup(
                new InlineKeyboardButton[]{new InlineKeyboardButton(messageProperty.getShowMarksAction()).callbackData(new StagedCommandDto(showMarksCommand.getIndex()).toByteString())},
                new InlineKeyboardButton[]{new InlineKeyboardButton(messageProperty.getAddAccountAction()).callbackData(new CommandDto(addAccountCommand.getIndex()).toByteString())},
                new InlineKeyboardButton[]{new InlineKeyboardButton(messageProperty.getListAccountsAction()).callbackData(new CommandDto(listAccountsCommand.getIndex()).toByteString())});
        chatService.sendMessage(chatId, messageProperty.getChooseActionMessage(), keyboard);

    }

    @Override
    public String getCommand() {
        return "/start";
    }
}
