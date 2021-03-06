package com.gruppe27.fellesprosjekt.server.controllers;

import com.gruppe27.fellesprosjekt.common.ParticipantUser;
import com.gruppe27.fellesprosjekt.common.Room;
import com.gruppe27.fellesprosjekt.common.messages.ErrorMessage;
import com.gruppe27.fellesprosjekt.common.messages.ParticipantUserMessage;
import com.gruppe27.fellesprosjekt.common.messages.RequestMessage;
import com.gruppe27.fellesprosjekt.common.messages.RoomMessage;
import com.gruppe27.fellesprosjekt.server.CalendarConnection;
import com.gruppe27.fellesprosjekt.server.DatabaseConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class RequestController {
    private static final String FIND_TIME_OVERLAP = " WHERE Event.date = ? AND (" +
            " (? < Event.end AND Event.end <= ?) OR " +
            " (? <= Event.start AND Event.start < ?) OR " +
            " (Event.start <= ? AND ? <= Event.end))";
    private static RequestController instance = null;

    protected RequestController() {
    }

    public static RequestController getInstance() {
        if (instance == null) {
            instance = new RequestController();
        }
        return instance;
    }

    public void handleMessage(CalendarConnection connection, Object message) {
        RequestMessage requestMessage = (RequestMessage) message;
        switch (requestMessage.getCommand()) {
            case ROOM_REQUEST:
                sendFilteredRooms(connection, requestMessage);
                break;
            case USER_REQUEST:
                sendAllParticipantUsers(connection, requestMessage);
                break;
            case INVITED_USERS_REQUEST:
                sendAllInvitedUsers(connection, requestMessage);
                break;
        }
    }

    private void sendAllInvitedUsers(CalendarConnection connection, RequestMessage message) {
        try{
            PreparedStatement usersStatus = DatabaseConnector.getConnection().prepareStatement(
                    "SELECT User.username, User.name, UserEvent.status FROM User JOIN UserEvent "
                    + "ON User.username = UserEvent.username JOIN Event ON Event.id = UserEvent.event_id "
                    + "WHERE Event.id = ?"
                    );
            
            usersStatus.setInt(1, message.getEventId());
            
            ResultSet usersStatusResult = usersStatus.executeQuery();
            HashSet<ParticipantUser> invitedUsers = new HashSet<>();
            
            while(usersStatusResult.next()){
                ParticipantUser statusUser = new ParticipantUser(
                        usersStatusResult.getString(1),
                        usersStatusResult.getString(2),
                        false
                        );
                statusUser.setParticipantStatus(usersStatusResult.getString(3));
                invitedUsers.add(statusUser);
            }
            
            System.out.println(invitedUsers.size() + "invited users found.");
            ParticipantUserMessage createdMessage = new ParticipantUserMessage(ParticipantUserMessage.Command.RECEIVE_ALL, invitedUsers);
            connection.sendTCP(createdMessage);
            
        }catch (SQLException e) {
            e.printStackTrace();
            ErrorMessage error = new ErrorMessage();
            connection.sendTCP(error);
        }
    }

    private void sendAllParticipantUsers(CalendarConnection connection, RequestMessage message) {
        String busyQuery = " FROM User " +
                "JOIN UserEvent ON User.username = UserEvent.username " +
                "JOIN Event ON Event.id = UserEvent.event_id " +
                FIND_TIME_OVERLAP;
        try {

            PreparedStatement busyUsersStatement = DatabaseConnector.getConnection().prepareStatement("SELECT User.username, User.name " +
                    "FROM User WHERE User.username IN (SELECT User.username " + busyQuery + ")"
            );

            busyUsersStatement.setString(1, message.getDate().toString());
            busyUsersStatement.setString(2, message.getStartTime().toString());
            busyUsersStatement.setString(3, message.getEndTime().toString());
            busyUsersStatement.setString(4, message.getStartTime().toString());
            busyUsersStatement.setString(5, message.getEndTime().toString());
            busyUsersStatement.setString(6, message.getStartTime().toString());
            busyUsersStatement.setString(7, message.getEndTime().toString());

            PreparedStatement freeUsersStatement = DatabaseConnector.getConnection().prepareStatement(
                    "SELECT User.username, User.name FROM User WHERE User.username NOT IN (SELECT User.username " + busyQuery + ")"
            );

            freeUsersStatement.setString(1, message.getDate().toString());
            freeUsersStatement.setString(2, message.getStartTime().toString());
            freeUsersStatement.setString(3, message.getEndTime().toString());
            freeUsersStatement.setString(4, message.getStartTime().toString());
            freeUsersStatement.setString(5, message.getEndTime().toString());
            freeUsersStatement.setString(6, message.getStartTime().toString());
            freeUsersStatement.setString(7, message.getEndTime().toString());


            HashSet<ParticipantUser> participantUsers = new HashSet<>();
            ResultSet busyUsersResult = busyUsersStatement.executeQuery();
            ResultSet freeUsersResult = freeUsersStatement.executeQuery();

            
            while (busyUsersResult.next()) {
                ParticipantUser busyUser = new ParticipantUser(
                        busyUsersResult.getString(1),
                        busyUsersResult.getString(2),
                        true
                );
                participantUsers.add(busyUser);
            }

            while (freeUsersResult.next()) {
                ParticipantUser freeUser = new ParticipantUser(
                        freeUsersResult.getString(1),
                        freeUsersResult.getString(2),
                        false
                );
                participantUsers.add(freeUser);
            }
            System.out.println(participantUsers.size() + " users found!");
            ParticipantUserMessage createdMessage = new ParticipantUserMessage(ParticipantUserMessage.Command.RECEIVE_ALL, participantUsers);
            connection.sendTCP(createdMessage);

        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMessage error = new ErrorMessage();
            connection.sendTCP(error);
        }
    }

    private void sendFilteredRooms(CalendarConnection connection, RequestMessage message) {
        try {

            PreparedStatement statement = DatabaseConnector.getConnection().prepareStatement(
                    "SELECT Room.name, Room.capacity FROM Room" +
                            " WHERE ? <= Room.capacity AND" +
                            " Room.name NOT IN(" +
                            "SELECT Room.name FROM Room JOIN Event ON Event.room = Room.name" +
                            FIND_TIME_OVERLAP + " )"
            );
            statement.setInt(1, message.getCapacity());
            statement.setString(2, message.getDate().toString());
            statement.setString(3, message.getStartTime().toString());
            statement.setString(4, message.getEndTime().toString());
            statement.setString(5, message.getStartTime().toString());
            statement.setString(6, message.getEndTime().toString());
            statement.setString(7, message.getStartTime().toString());
            statement.setString(8, message.getEndTime().toString());

            HashSet<Room> rooms = new HashSet<>();
            int roomAmount = 0;
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                roomAmount++;
                Room room = new Room();
                room.setRoomName(result.getString("name"));
                room.setCapacity(result.getInt("capacity"));
                rooms.add(room);
            }

            System.out.println(roomAmount + " free rooms found");
            RoomMessage createdMessage = new RoomMessage(RoomMessage.Command.RECEIVE_ROOMS, rooms);
            connection.sendTCP(createdMessage);

        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMessage error = new ErrorMessage();
            connection.sendTCP(error);
        }
    }
}
