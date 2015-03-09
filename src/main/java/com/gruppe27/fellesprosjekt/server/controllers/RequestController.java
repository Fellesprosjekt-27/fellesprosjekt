package com.gruppe27.fellesprosjekt.server.controllers;

import com.gruppe27.fellesprosjekt.common.Room;
import com.gruppe27.fellesprosjekt.common.messages.ErrorMessage;
import com.gruppe27.fellesprosjekt.common.messages.RoomMessage;
import com.gruppe27.fellesprosjekt.common.messages.RequestMessage;
import com.gruppe27.fellesprosjekt.server.CalendarConnection;
import com.gruppe27.fellesprosjekt.server.DatabaseConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class RequestController {
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
                sendAllParticipantUsers(connection,requestMessage);
                break;
        }
    }

    private void sendAllParticipantUsers(CalendarConnection connection, RequestMessage message) {
        try {

            PreparedStatement statement = DatabaseConnector.getConnection().prepareStatement(
                            "SELECT User.username FROM User JOIN UserEvent ON User.username = UserEvent.username JOIN Event ON Event.id = UserEvent.event_id" +
                            " WHERE Event.date = ? AND (" +
                            " (Event.start < ? AND ? < Event.end) OR " +
                            " (Event.start < ? AND ? < Event.end))"
            );
            statement.setString(1, message.getDate().toString());
            statement.setString(2, message.getStartTime().toString());
            statement.setString(3, message.getStartTime().toString());
            statement.setString(4, message.getEndTime().toString());
            statement.setString(5, message.getEndTime().toString());

            /*
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
            */
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
                            " WHERE Event.date = ? AND (" +
                            " (Event.start < ? AND ? < Event.end) OR " +
                            " (Event.start < ? AND ? < Event.end)))"
            );
            statement.setInt(1, message.getCapacity());
            statement.setString(2, message.getDate().toString());
            statement.setString(3, message.getStartTime().toString());
            statement.setString(4, message.getStartTime().toString());
            statement.setString(5, message.getEndTime().toString());
            statement.setString(6, message.getEndTime().toString());

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