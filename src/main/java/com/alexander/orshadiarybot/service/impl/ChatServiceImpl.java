package com.alexander.orshadiarybot.service.impl;

import com.alexander.orshadiarybot.listener.AnswerListener;
import com.alexander.orshadiarybot.model.dto.AnswerUpdate;
import com.alexander.orshadiarybot.service.ChatService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {
    private TelegramBot bot;
    private AnswerListener answerListener;

    @Override
    public void sendMessage(long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        bot.execute(sendMessage);
    }

    @Override
    public void sendMessage(long chatId, String message, Keyboard keyboard) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        sendMessage.replyMarkup(keyboard);
        bot.execute(sendMessage);
    }

    @Override
    public Update askAndWaitForUpdate(long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        return askAndWaitForUpdate(chatId, sendMessage);
    }

    @Override
    public Update askAndWaitForUpdate(long chatId, String message, List<String> keyboardOptions) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        sendMessage.replyMarkup(buildKeyboard(keyboardOptions));
        return askAndWaitForUpdate(chatId, sendMessage);
    }

    @Override
    public Update askAndWaitForUpdate(long chatId, String message, Keyboard keyboard) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        sendMessage.replyMarkup(keyboard);
        return askAndWaitForUpdate(chatId, sendMessage);
    }

    @Override
    public Keyboard buildKeyboard(List<String> options) {
        return new InlineKeyboardMarkup(
                options.stream().map(option -> new InlineKeyboardButton[]{new InlineKeyboardButton(option).callbackData(option)})
                        .toArray(InlineKeyboardButton[][]::new)
        );
    }

    private Update askAndWaitForUpdate(long chatId, SendMessage sendMessage) {
        bot.execute(sendMessage);
        AnswerUpdate phoneAnswerUpdate = answerListener.ask(chatId);
        Optional<Update> optionalPhoneAnswer = phoneAnswerUpdate.get(30 * 1000L);
        answerListener.removeQuestion(chatId);
        return optionalPhoneAnswer.orElseThrow(RuntimeException::new);
    }
}
