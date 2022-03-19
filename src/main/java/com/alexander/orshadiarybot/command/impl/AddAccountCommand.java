package com.alexander.orshadiarybot.command.impl;

import com.alexander.orshadiarybot.command.Command;
import com.alexander.orshadiarybot.config.property.MessageProperty;
import com.alexander.orshadiarybot.model.domain.Account;
import com.alexander.orshadiarybot.model.domain.Mark;
import com.alexander.orshadiarybot.service.AccountService;
import com.alexander.orshadiarybot.service.ChatService;
import com.alexander.orshadiarybot.service.MarkService;
import com.alexander.orshadiarybot.service.SiteDataService;
import com.pengrad.telegrambot.model.Update;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Log4j2
@Component
@AllArgsConstructor
public class AddAccountCommand extends Command {
    private ChatService chatService;
    private AccountService accountService;
    private SiteDataService siteDataService;
    private MarkService markService;
    private MessageProperty messageProperty;

    @Override
    @Transactional
    public void handle(long chatId, Update update) {
        String phone = chatService.askAndWaitForUpdate(chatId, messageProperty.getEnterPhoneMessage()).message().text();
        String password = chatService.askAndWaitForUpdate(chatId, messageProperty.getEnterPasswordMessage()).message().text();

        Account account = accountService.addAccount(phone, password, chatId);
        chatService.sendMessage(chatId, messageProperty.getAccountWasAddedMessage());
        List<Mark> marks = siteDataService.findMarksByAccounts(account);
        markService.rebaseMarks(account, marks);
    }

    @Override
    public String getCommand() {
        return "/addAccount";
    }
}
