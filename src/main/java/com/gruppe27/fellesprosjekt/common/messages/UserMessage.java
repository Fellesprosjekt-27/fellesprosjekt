package com.gruppe27.fellesprosjekt.common.messages;

import java.util.HashSet;

import com.gruppe27.fellesprosjekt.common.User;

public class UserMessage {
    public enum Command {
        SEND_ALL, RECEIVE_ALL
    }

    HashSet<User> users;
    Command command;

    public UserMessage() {
    }

    public UserMessage(Command command) {
        this.command = command;
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
}
