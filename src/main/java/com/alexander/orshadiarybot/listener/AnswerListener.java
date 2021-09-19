package com.alexander.orshadiarybot.listener;

import com.alexander.orshadiarybot.model.dto.AnswerUpdate;
import com.pengrad.telegrambot.model.Update;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
public class AnswerListener {
    private final Map<Long, AnswerUpdate> answerUpdates = new HashMap<>();

    public AnswerUpdate ask(long chatId) {
        AnswerUpdate answerUpdate = new AnswerUpdate();
        answerUpdates.put(chatId, answerUpdate);
        return answerUpdate;
    }

    public boolean answer(long chatId, Update update) {
        AnswerUpdate answerUpdate = answerUpdates.remove(chatId);
        if (answerUpdate == null) {
            return false;
        }
        answerUpdate.setUpdate(update);
        return true;
    }

    public void removeQuestion(long chatId) {
        answerUpdates.remove(chatId);
    }
}
