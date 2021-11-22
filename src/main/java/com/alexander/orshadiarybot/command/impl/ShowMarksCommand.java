package com.alexander.orshadiarybot.command.impl;

import com.alexander.orshadiarybot.command.StagedCommand;
import com.alexander.orshadiarybot.config.property.MessageProperty;
import com.alexander.orshadiarybot.config.property.QuoterProperty;
import com.alexander.orshadiarybot.exception.BotException;
import com.alexander.orshadiarybot.model.domain.Account;
import com.alexander.orshadiarybot.model.domain.Mark;
import com.alexander.orshadiarybot.model.domain.Subject;
import com.alexander.orshadiarybot.model.dto.ShowMarksCommandDto;
import com.alexander.orshadiarybot.service.AccountService;
import com.alexander.orshadiarybot.service.ChatService;
import com.alexander.orshadiarybot.service.MarkService;
import com.alexander.orshadiarybot.service.SubjectService;
import com.alexander.orshadiarybot.util.KeyboardGenerator;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
@Component
@AllArgsConstructor
public class ShowMarksCommand extends StagedCommand<ShowMarksCommandDto> {
    private ChatService chatService;
    private AccountService accountService;
    private MessageProperty messageProperty;
    private MarkService markService;
    private SubjectService subjectService;
    private QuoterProperty quoterProperty;

    private static final int ASK_QUOTER_STAGE = 1;
    private static final int ASK_SUBJECT_STAGE = 2;
    private static final int SHOW_STAGE = 3;
    private static final int SHOW_ALL_STAGE = 4;

    private static final long ALL_SUBJECTS_ID = -1;

    private static final int QUOTER_SIZE = 4;

    private Map<Integer, Predicate<Mark>> quoterPredicates;

    @Override
    @PostConstruct
    protected void initializeConsumers() {
        consumers.add(this::askAccount);
        consumers.add(this::askQuoter);
        consumers.add(this::askSubject);
        consumers.add(this::showMarks);
        consumers.add(this::showAllMarks);

        quoterPredicates.put(1, m -> m.getDate().isBefore(quoterProperty.getQuoterTwoBegin()));
        quoterPredicates.put(2, m -> m.getDate().isAfter(quoterProperty.getQuoterTwoBegin())
                && m.getDate().isBefore(quoterProperty.getQuoterThreeBegin()));
        quoterPredicates.put(3, m -> m.getDate().isAfter(quoterProperty.getQuoterThreeBegin())
                && m.getDate().isBefore(quoterProperty.getQuoterFourBegin()));
        quoterPredicates.put(4, m -> m.getDate().isAfter(quoterProperty.getQuoterFourBegin()));
    }

    @Override
    public void handle(long chatId, Update update) {
        String callbackData = update.callbackQuery().data();
        ShowMarksCommandDto dto = ShowMarksCommandDto.fromByteString(callbackData);
        consumers.get(dto.getStage()).handle(chatId, dto, update);
    }

    private void askAccount(long chatId, ShowMarksCommandDto dto, Update update) {
        List<Account> accounts = accountService.findByOwner(chatId);
        if (accounts.isEmpty()) {
            throw new BotException(messageProperty.getNoLinkedAccountsMessage());
        }
        if (accounts.size() == 1) {
            askQuoter(chatId, dto.withAccountId(accounts.get(0).getId()), update);
            return;
        }
        KeyboardGenerator<Account> keyboardGenerator = new KeyboardGenerator<>();
        Keyboard keyboard = keyboardGenerator.generateKeyboard(accounts,
                Account::getFullName,
                account -> dto.withAccountId(account.getId()).withStage(ASK_QUOTER_STAGE).toByteString());
        chatService.sendMessage(chatId, messageProperty.getChooseAccountUserMessage(), keyboard);
    }

    private void askQuoter(long chatId, ShowMarksCommandDto dto, Update update) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        IntStream.rangeClosed(1, QUOTER_SIZE).forEach(q -> keyboardMarkup.addRow(
                new InlineKeyboardButton(String.format(messageProperty.getQuoterOption(), q))
                        .callbackData(dto.withStage(ASK_SUBJECT_STAGE).withQuoter(q).toByteString())));
        chatService.sendMessage(chatId, messageProperty.getChooseQuoterMessage(), keyboardMarkup);
    }

    private void askSubject(long chatId, ShowMarksCommandDto dto, Update update) {
        List<Subject> subjects = subjectService.findByAccountId(dto.getAccountId());
        KeyboardGenerator<Subject> keyboardGenerator = new KeyboardGenerator<>();
        InlineKeyboardMarkup keyboard = keyboardGenerator.generateKeyboard(subjects,
                Subject::getName, s -> dto.withSubjectId(s.getId())
                        .withStage(SHOW_STAGE).toByteString());
        keyboard.addRow(new InlineKeyboardButton(messageProperty.getAllMarksOption())
                .callbackData(dto.withSubjectId(ALL_SUBJECTS_ID).withStage(SHOW_ALL_STAGE).toByteString()));
        chatService.sendMessage(chatId, messageProperty.getChooseSubjectMessage(), keyboard);
    }

    private void showAllMarks(long chatId, ShowMarksCommandDto dto, Update update) {
        List<Mark> marks = markService.findMarksByAccount(dto.getAccountId());
        Account account = accountService.findById(dto.getAccountId());
        String message = marks.stream()
                .filter(quoterPredicates.get(dto.getQuoter()))
                .collect(Collectors.groupingBy(mark -> mark.getSubject().getName()))
                .entrySet().stream()
                .map(e -> e.getKey() + ": " + e.getValue().stream().map(m -> String.valueOf(m.getValue())).collect(Collectors.joining(", ")))
                .collect(Collectors.joining("\n"));
        String resultMessage = String.format(messageProperty.getMarksMessage(), account.getFullName(), message);
        chatService.sendMessage(chatId, resultMessage);
    }

    private void showMarks(long chatId, ShowMarksCommandDto dto, Update update) {
        List<Mark> marks = markService.findMarksByAccountAndSubjectId(dto.getAccountId(), dto.getSubjectId());
        Subject subject = subjectService.findById(dto.getSubjectId());
        List<String> markValues = marks.stream()
                .filter(quoterPredicates.get(dto.getQuoter()))
                .map(Mark::getValue)
                .map(String::valueOf)
                .collect(Collectors.toList());
        String resultMessage = String.format(messageProperty.getMarksBySubjectMessage(), subject.getName(), String.join(", ", markValues));
        chatService.sendMessage(chatId, resultMessage);
    }

    @Override
    public String getCommand() {
        return "/showMarks";
    }
}
