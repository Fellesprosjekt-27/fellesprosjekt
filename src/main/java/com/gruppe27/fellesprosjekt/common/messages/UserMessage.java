package com.gruppe27.fellesprosjekt.common.messages;

import com.gruppe27.fellesprosjekt.common.User;

public class UserMessage {
    public enum Command {
        SUCCESSFUL_LOGIN
    }

    private User user;
    private Command command;

    public UserMessage() {}

    public UserMessage(Command command, User user) {
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
