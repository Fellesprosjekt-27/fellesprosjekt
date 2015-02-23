package com.gruppe27.fellesprosjekt;

import java.util.Calendar;

/**
 * Created by Andreas on 23.02.2015.
 */
public class User {

    String username;

    String name;

    String password;

    int teamNo;

    Calendar calendar;

    public User(String username, String password, String name) {
        this.setName(name);
        this.setPassword(password);
        this.setUsername(username);
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
}

}
