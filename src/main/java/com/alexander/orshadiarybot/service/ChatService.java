package com.alexander.orshadiarybot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.Keyboard;

import java.util.List;

public interface ChatService {
    void sendMessage(long chatId, String message);

    void sendMessage(long chatId, String message, Keyboard keyboard);

    Update askAndWaitForUpdate(long chatId, String message);

    Update askAndWaitForUpdate(long chatId, String message, List<String> keyboardOptions);

    Update askAndWaitForUpdate(long chatId, String message, Keyboard keyboard);

    Keyboard buildKeyboard(List<String> options);
}
