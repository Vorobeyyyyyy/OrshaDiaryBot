package com.alexander.orshadiarybot.model.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShowMarksCommandDtoTest {
    @Test
    void toByteString() {
        ShowMarksCommandDto expected = ShowMarksCommandDto.builder().commandIndex(12).stage(4).quoter(34).accountId(443L).subjectId(3L).build();
        ShowMarksCommandDto actual = ShowMarksCommandDto.fromByteString(expected.toByteString());
        assertEquals(expected, actual);
    }
}