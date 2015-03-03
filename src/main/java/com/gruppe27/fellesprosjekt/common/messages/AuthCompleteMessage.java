package com.gruppe27.fellesprosjekt.common.messages;

import com.gruppe27.fellesprosjekt.common.User;

public class AuthCompleteMessage {
    public enum Command {
        SUCCESSFUL_LOGIN,
        UNSUCCESSFUL_LOGIN
    }

    Command command;
    User user;

    public AuthCompleteMessage() {
    }

    public AuthCompleteMessage(Command command) {
        this.command = command;
    }

    public AuthCompleteMessage(Command command, User user) {
        this.command = command;
        this.user = user;
    }

    public Command getCommand() {
        return command;
    }

    public User getUser() {
        return user;
    }
}
