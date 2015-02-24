package com.gruppe27.fellesprosjekt;
import java.util.ArrayList;



public class UserCalendar {

    //ArrayList<Event> events;

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

