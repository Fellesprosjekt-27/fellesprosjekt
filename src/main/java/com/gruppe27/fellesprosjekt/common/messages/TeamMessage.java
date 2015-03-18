package com.gruppe27.fellesprosjekt.common.messages;

import com.gruppe27.fellesprosjekt.common.Team;

import java.util.HashSet;

public class TeamMessage {
    HashSet<Team> teams;
    Command command;

    public TeamMessage() {
    }

    public TeamMessage(Command command) {
        this.command = command;
        teams = new HashSet<>();
    }

    public TeamMessage(Command command, HashSet teams) {
        this.command = command;
        this.teams = teams;
    }

    public HashSet<Team> getTeams() {
        return teams;
    }

    public Command getCommand() {
        return command;
    }

    public enum Command {
        SEND_TEAMS, RECEIVE_TEAMS
    }
}
