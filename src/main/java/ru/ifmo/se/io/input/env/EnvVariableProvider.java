package ru.ifmo.se.io.input.env;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EnvVariableProvider implements EnvironmentProvider {

    private final String envName;

    @Override
    public String getFileName() {
        return System.getenv(envName);
    }
}
