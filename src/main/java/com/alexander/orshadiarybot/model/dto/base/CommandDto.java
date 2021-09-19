package com.alexander.orshadiarybot.model.dto.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.nio.ByteBuffer;
import java.util.Base64;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CommandDto {
    protected int commandIndex;

    public String toByteString() {
        return Base64.getEncoder().encodeToString(ByteBuffer.allocate(8).putInt(commandIndex).array());
    }

    public static CommandDto fromByteString(String string) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(Base64.getDecoder().decode(string));
        return new CommandDto(byteBuffer.getInt());
    }

    public CommandDto withCommandIndex(int commandIndex) {
        return this.commandIndex == commandIndex ? this : new CommandDto(commandIndex);
    }
}
