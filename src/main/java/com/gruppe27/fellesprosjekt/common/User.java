package com.gruppe27.fellesprosjekt.common;


import java.util.HashSet;
import java.util.Set;


public class User {

    private String username;

    private String name;

    private int teamNo;

    private Set<Group> groups;

    public User(String username, String name) {
        this.setName(name);
        this.setUsername(username);
        groups = new HashSet<>();
    }

    /**
     * Takes in a group and adds it to the user's other groups
     * @param group
     */
    public void addUserToGroup(Group group) {
        if (!groups.contains(group)) {
            groups.add(group);
            group.addUser(this);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTeamNo() {
        return teamNo;
    }

    public void setTeamNo(int teamNo) {
        this.teamNo = teamNo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void removeUserFromGroup(Group group) {
        if (groups.contains(group)) {
            groups.remove(group);
            group.removeUser(this);
        }
    }
}
