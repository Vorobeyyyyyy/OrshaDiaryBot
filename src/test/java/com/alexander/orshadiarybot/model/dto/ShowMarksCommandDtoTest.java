package com.alexander.orshadiarybot.model.dto;

import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ShowMarksCommandDtoTest {
    @Test
    void toByteString() {
        ShowMarksCommandDto expected = ShowMarksCommandDto.builder().commandIndex(12).stage(4).subjectId(34L).accountId(443L).build();
        ShowMarksCommandDto actual = ShowMarksCommandDto.fromByteString(expected.toByteString());
        assertEquals(expected, actual);
    }
}