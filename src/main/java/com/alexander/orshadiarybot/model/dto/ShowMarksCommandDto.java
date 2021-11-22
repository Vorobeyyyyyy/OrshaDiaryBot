package com.alexander.orshadiarybot.model.dto;

import com.alexander.orshadiarybot.model.dto.base.StagedCommandDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.nio.ByteBuffer;
import java.util.Base64;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ShowMarksCommandDto extends StagedCommandDto {
    private Long accountId;
    private Long subjectId;
    private Integer quoter;

    public ShowMarksCommandDto(int commandIndex, int stage, Long accountId, Integer quoter, Long subjectId) {
        super(commandIndex, stage);
        this.accountId = accountId;
        this.subjectId = subjectId;
        this.quoter = quoter;
    }

    @Override
    public String toByteString() {
        byte[] bytes = ByteBuffer.allocate(28)
                .putInt(commandIndex)
                .putInt(stage)
                .putLong(accountId != null ? accountId : 0)
                .putLong(subjectId != null ? subjectId : 0)
                .putInt(quoter != null ? quoter : 0)
                .array();
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static ShowMarksCommandDto fromByteString(String string) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(Base64.getDecoder().decode(string));
        return builder()
                .commandIndex(byteBuffer.getInt())
                .stage(byteBuffer.getInt())
                .accountId(byteBuffer.hasRemaining() ? byteBuffer.getLong() : null)
                .subjectId(byteBuffer.hasRemaining() ? byteBuffer.getLong() : null)
                .quoter(byteBuffer.hasRemaining() ? byteBuffer.getInt() : null)
                .build();
    }

    @Override
    public ShowMarksCommandDto withCommandIndex(int commandIndex) {
        return this.commandIndex == commandIndex ? this : new ShowMarksCommandDto(commandIndex, this.stage, this.accountId, this.quoter, this.subjectId);
    }

    @Override
    public ShowMarksCommandDto withStage(int stage) {
        return this.stage == stage ? this : new ShowMarksCommandDto(this.commandIndex, stage, this.accountId, this.quoter, this.subjectId);
    }

    public ShowMarksCommandDto withAccountId(Long accountId) {
        return new ShowMarksCommandDto(this.commandIndex, this.stage, accountId, this.quoter, this.subjectId);
    }

    public ShowMarksCommandDto withSubjectId(Long subjectId) {
        return new ShowMarksCommandDto(this.commandIndex, this.stage, this.accountId, this.quoter, subjectId);
    }

    public ShowMarksCommandDto withQuoter(Integer quoter) {
        return new ShowMarksCommandDto(this.commandIndex, this.stage, this.accountId, quoter, this.subjectId);
    }
}
