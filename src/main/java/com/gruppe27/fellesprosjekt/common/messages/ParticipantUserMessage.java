package com.gruppe27.fellesprosjekt.common.messages;

import com.gruppe27.fellesprosjekt.common.ParticipantUser;

import java.util.HashSet;

public class ParticipantUserMessage {
    HashSet<ParticipantUser> participantUsers;
    Command command;
    public ParticipantUserMessage() {
    }

    public ParticipantUserMessage(Command command, HashSet<ParticipantUser> participantUsers) {
        this.command = command;
        this.participantUsers = participantUsers;
    }

    public Command getCommand() {
        return command;
    }

    public HashSet<ParticipantUser> getParticipantUsers() {
        return participantUsers;
    }

    public enum Command {
        SEND_ALL, RECEIVE_ALL
    }
}
