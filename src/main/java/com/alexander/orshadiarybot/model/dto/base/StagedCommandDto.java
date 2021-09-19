package com.alexander.orshadiarybot.model.dto.base;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.nio.ByteBuffer;
import java.util.Base64;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
public class StagedCommandDto extends CommandDto{
    protected int stage = 0;

    public StagedCommandDto(int commandIndex) {
        super(commandIndex);
    }

    public StagedCommandDto(int commandIndex, int stage) {
        super(commandIndex);
        this.stage = stage;
    }

    public String toByteString() {
        return Base64.getEncoder().encodeToString(ByteBuffer.allocate(8).putInt(commandIndex).putInt(stage).array());
    }

    public static StagedCommandDto fromByteString(String string) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(Base64.getDecoder().decode(string));
        return StagedCommandDto.builder()
                .commandIndex(byteBuffer.getInt())
                .stage(byteBuffer.getInt())
                .build();
    }

    public StagedCommandDto withStage(int stage) {
        return this.stage == stage ? this : new StagedCommandDto(this.commandIndex, stage);
    }

    @Override
    public StagedCommandDto withCommandIndex(int commandIndex) {
        return this.commandIndex == commandIndex ? this : new StagedCommandDto(commandIndex, this.stage);
    }
}
