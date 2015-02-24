package com.gruppe27.fellesprosjekt;

import java.util.HashSet;
import java.util.Set;

public class Group {
    private int groupNo;

    private UserCalendar calendar;

    private Set<User> users;

    public Group() {
        this.calendar = new UserCalendar(this);
        users = new HashSet<User>(50);
    }

    /**
     * legger til en bruker i gruppens liste over brukere hvis denne ikke er i listen allerede
     * @param user
     */
    public void addUser(User user) {
        if(!users.contains(user)){
            users.add(user);
            user.addUserToGroup(this);
        }
    }
    public void removeUser(User user) {
        if(users.contains(user)){
            users.remove(user);
            user.removeUserFromGroup(this);
        }
    }

    public Set<User> getUsers() {
        //Lager ny liste, slik at gruppens medlemmer er uforandret.
        return new Set<User>(this.users);
    }
    public UserCalendar getCalendar() {
        return calendar;
    }

    public int getGroupNo() {
        return groupNo;
    }


    public void setGroupNo(int groupNo) {
        this.groupNo = groupNo;
    }


}
