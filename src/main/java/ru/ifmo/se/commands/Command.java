package ru.ifmo.se.commands;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.ifmo.se.io.input.readers.Reader;

@Getter
@RequiredArgsConstructor
public abstract class Command {

    protected final String commandSignature;
    protected final String commandDescription;

    public abstract void execute(String[] inputArgs, Reader reader);
}
