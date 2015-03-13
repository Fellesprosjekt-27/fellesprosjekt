package com.gruppe27.fellesprosjekt.common.messages;

import com.gruppe27.fellesprosjekt.common.User;

import java.util.HashSet;

public class UserMessage {
    HashSet<User> users;
    Command command;
    public UserMessage() {
    }

    public UserMessage(Command command) {
        this.command = command;
        this.users = null;
    }

    public UserMessage(Command command, HashSet<User> users) {
        this.command = command;
        this.users = users;
    }

    public Command getCommand() {
        return command;
    }

    public HashSet<User> getUsers() {
        return users;
    }

    public enum Command {
        SEND_ALL, RECEIVE_ALL
    }
}
