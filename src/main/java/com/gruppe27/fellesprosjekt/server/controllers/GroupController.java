package com.gruppe27.fellesprosjekt.server.controllers;

import com.gruppe27.fellesprosjekt.common.Group;
import com.gruppe27.fellesprosjekt.server.CalendarConnection;

import java.util.ArrayList;

public class GroupController {
    private CalendarConnection connection;

    public GroupController(CalendarConnection connection) {
        this.connection = connection;
    }

    public void createGroup(Group group) {
        // This should persist a group to the database, TODO


    }

    public ArrayList<Group> getAllGroups() {
        // This should get all groups from the database, TODO
        ArrayList<Group> groups = new ArrayList<>();
        Group testGroup = new Group(1, "TestGruppe");
        groups.add(testGroup);

        return groups;
    }
}
