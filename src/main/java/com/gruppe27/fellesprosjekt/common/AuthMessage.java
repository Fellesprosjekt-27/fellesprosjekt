package com.gruppe27.fellesprosjekt.common;

public class AuthMessage {
    public enum Command {
        LOGIN
    }

    private Command command;
    private String username;
    private String password;

    public AuthMessage(Command command, String password, String username) {
        this.command = command;
        this.password = password;
        this.username = username;
    }

    public Command getCommand() {
        return command;
    }
}
