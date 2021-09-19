package com.alexander.orshadiarybot.util;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;

import java.util.List;
import java.util.function.Function;

public class KeyboardGenerator<T> {
    public InlineKeyboardMarkup generateKeyboard(List<T> objects, Function<T, String> name, Function<T, String> data) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        objects.forEach(o -> keyboard.addRow(new InlineKeyboardButton(name.apply(o)).callbackData(data.apply(o))));
        return keyboard;
    }
}
