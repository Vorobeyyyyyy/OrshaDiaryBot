package com.alexander.orshadiarybot.command;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Log4j2
@Component
@AllArgsConstructor
public class CommandResolver {
    private List<Command> commands;

    @PostConstruct
    private void indexCommands() {
        for (int i = 0; i < commands.size(); i++) {
            commands.get(i).setIndex(i);
        }
    }

    public Optional<Command> getCommand(String command) {
        return commands.stream().filter(c -> c.getCommand().equals(command)).findAny();
    }

    public Optional<Command> getCommand(int index) {
        return Optional.ofNullable(commands.get(index));
    }
}