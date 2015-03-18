package com.gruppe27.fellesprosjekt.server.controllers;

import com.gruppe27.fellesprosjekt.common.Team;
import com.gruppe27.fellesprosjekt.common.User;
import com.gruppe27.fellesprosjekt.common.messages.ErrorMessage;
import com.gruppe27.fellesprosjekt.common.messages.TeamMessage;
import com.gruppe27.fellesprosjekt.server.CalendarConnection;
import com.gruppe27.fellesprosjekt.server.DatabaseConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class TeamController {
    private static TeamController instance = null;

    protected TeamController() {
    }

    public static TeamController getInstance() {
        if (instance == null) {
            instance = new TeamController();
        }
        return instance;
    }

    public void handleMessage(CalendarConnection connection, Object message) {
        TeamMessage teamMessage =  (TeamMessage) message;
        switch (teamMessage.getCommand()) {
            case SEND_TEAMS :
                sendTeams(connection, teamMessage);
                break;
            case RECEIVE_TEAMS:
                break;
        }
    }

    private void sendTeams(CalendarConnection connection, TeamMessage teamMessage) {
        try {
            String query = "SELECT Team.number, Team.name, User.username, User.name FROM Team " +
                    "JOIN TeamMember ON Team.number = TeamMember.team_number JOIN User ON " +
                    "TeamMember.username = User.username ORDER BY Team.number";
            PreparedStatement statement = DatabaseConnector.getConnection().prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();
            int currentId = -1;
            Team team = null;
            HashSet<Team> teams = new HashSet<>();
            while (resultSet.next()) {
                if (currentId != resultSet.getInt(1)) {
                    currentId = resultSet.getInt(1);
                    team = new Team();
                    team.setNumber(currentId);
                    team.setName(resultSet.getString(2));

                    User user = new User(resultSet.getString(3), resultSet.getString(4));
                    team.addUser(user);
                    teams.add(team);
                } else {
                    User user = new User(resultSet.getString(3), resultSet.getString(4));
                    team.addUser(user);
                }
            }
            TeamMessage reply = new TeamMessage(TeamMessage.Command.RECEIVE_TEAMS, teams);
            connection.sendTCP(reply);
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMessage error = new ErrorMessage();
            connection.sendTCP(error);
        }
    }

}
