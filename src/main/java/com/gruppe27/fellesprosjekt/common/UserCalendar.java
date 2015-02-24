package com.gruppe27.fellesprosjekt.common;


public class UserCalendar {
    private User owner;

    private Group group;

    public UserCalendar(User owner) {
        this.owner = owner;
    }

    public UserCalendar(Group group) {
        this.group = group;

    }

    public User getOwner() {
        return owner;
    }

    public Group getGroup() {
        return group;
    }

}

