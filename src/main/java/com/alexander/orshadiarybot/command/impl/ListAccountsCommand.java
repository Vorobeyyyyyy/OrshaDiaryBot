package com.alexander.orshadiarybot.command.impl;

import com.alexander.orshadiarybot.command.Command;
import com.alexander.orshadiarybot.config.property.MessageProperty;
import com.alexander.orshadiarybot.model.domain.Account;
import com.alexander.orshadiarybot.service.AccountService;
import com.alexander.orshadiarybot.service.ChatService;
import com.alexander.orshadiarybot.service.impl.ChatServiceImpl;
import com.pengrad.telegrambot.model.Update;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Component
@AllArgsConstructor
public class ListAccountsCommand extends Command {
    private ChatService chatService;
    private AccountService accountService;
    private MessageProperty messageProperty;

    @Override
    public void handle(long chatId, Update update) {
        List<String> accountNames = accountService.findByOwner(chatId).stream()
                .map(Account::getFullName)
                .collect(Collectors.toList());
        if (accountNames.isEmpty()) {
            chatService.sendMessage(chatId, messageProperty.getNoLinkedAccountsMessage());
            return;
        }
        chatService.sendMessage(chatId,
                String.format(messageProperty.getAccountListMessage(), String.join("\n", accountNames)));
        }

    @Override
    public String getCommand() {
        return "/list";
    }
}
