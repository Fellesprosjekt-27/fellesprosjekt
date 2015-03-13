package com.gruppe27.fellesprosjekt.common.messages;

public class AuthMessage {
    Command command;
    String username;
    String password;
    public AuthMessage() {
    }

    public AuthMessage(Command command, String username, String password) {
        this.command = command;
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public Command getCommand() {
        return command;
    }

    public enum Command {
        LOGIN
    }
}
