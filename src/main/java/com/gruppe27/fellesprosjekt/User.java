package com.gruppe27.fellesprosjekt;


import java.util.ArrayList;

/**
 * Created by Andreas on 23.02.2015.
 */
public class User {

    private String username;

    private String name;

    private String password;

    private int teamNo;

    private UserCalendar calendar;
    private ArrayList<Group> groups;

    public User(String username, String password, String name) {
        this.setName(name);
        this.setPassword(password);
        this.setUsername(username);
        this.calendar = new UserCalendar(this);
    }

    public UserCalendar getCalendar() {
        return calendar;
    }

    /**
     * tar inn en gruppe, og legger denne gruppe til blant brukerens grupper.
     * @param group
     */
    public void addUserToGroup(Group group) {
        if(! groups.contains(group)) {
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

    public String getPassword() {
    return password;
    }

    public void setPassword(String password) {
    this.password = password;
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
        if(groups.contains(group)) {
            groups.remove(group);
            group.removeUser(this);
        }
    }

    public boolean login(String username, String password) {
        //TODO
    }

}
