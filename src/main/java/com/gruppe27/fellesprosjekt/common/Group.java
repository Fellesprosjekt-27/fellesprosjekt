package com.gruppe27.fellesprosjekt.common;

import java.util.HashSet;
import java.util.Set;

public class Group {
    private int number;
    private String name;

    private UserCalendar calendar;

    private Set<User> users;

    public Group(int number, String name) {
        this.number = number;
        this.name = name;
        this.calendar = new UserCalendar(this);
        users = new HashSet<>();
    }

    /**
     * Adds a user to the set of users, if it doesn't already exist
     * @param user
     */
    public void addUser(User user) {
        if (!users.contains(user)) {
            users.add(user);
            user.addUserToGroup(this);
        }
    }

    public void removeUser(User user) {
        if (users.contains(user)) {
            users.remove(user);
            user.removeUserFromGroup(this);
        }
    }

    public Set<User> getUsers() {
        return new HashSet<>(this.users);
    }

    public UserCalendar getCalendar() {
        return calendar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}