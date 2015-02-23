package com.gruppe27.fellesprosjekt;
import java.util.ArrayList;

/**
 * Created by Andreas on 23.02.2015.
 */

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

