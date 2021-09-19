package com.alexander.orshadiarybot.model.dto.base;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StagedCommandDtoTest {
    @Test
    void toByteString() {
        StagedCommandDto expected = StagedCommandDto.builder().commandIndex(12).stage(4).build();
        StagedCommandDto actual = StagedCommandDto.fromByteString(expected.toByteString());
        System.out.println(expected.toByteString());
        assertEquals(expected, actual);
    }
}