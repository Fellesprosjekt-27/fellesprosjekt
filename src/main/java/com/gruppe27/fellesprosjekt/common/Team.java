package com.gruppe27.fellesprosjekt.common;

import java.util.HashSet;

public class Team {

    int number;
    String name;
    HashSet<User> teamMembers;

    public Team() {
        teamMembers = new HashSet<>();
    }

    public Team(int number, String name, HashSet teamMembers) {
        this.number = number;
        this.name = name;
        this. teamMembers = teamMembers;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashSet<User> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(HashSet<User> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public void addUser(User user) {
        teamMembers.add(user);
    }
}
