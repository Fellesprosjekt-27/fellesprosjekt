package com.gruppe27.fellesprosjekt.server.controllers;

import com.gruppe27.fellesprosjekt.common.Room;
import com.gruppe27.fellesprosjekt.common.messages.ErrorMessage;
import com.gruppe27.fellesprosjekt.common.messages.RoomMessage;
import com.gruppe27.fellesprosjekt.server.CalendarConnection;
import com.gruppe27.fellesprosjekt.server.DatabaseConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class RoomController {

    private static RoomController instance = null;

    protected RoomController() {
    }

    public static RoomController getInstance() {
        if (instance == null) {
            instance = new RoomController();
        }
        return instance;
    }

    public void handleMessage(CalendarConnection connection, Object message) {
        RoomMessage roomMessage = (RoomMessage) message;
        switch (roomMessage.getCommand()) {
            case SEND_ROOMS:
                sendRooms(connection);
                break;
        }
    }

    private void sendRooms(CalendarConnection connection) {
        try {

            PreparedStatement statement = DatabaseConnector.getConnection().prepareStatement(
                    "SELECT * FROM Rooms"
            );
            HashSet<Room> rooms = new HashSet<>();
            int roomAmount = 0;
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                roomAmount++;
                Room room = new Room();
                room.setRoomNo(result.getString("name"));
                room.setCapacity(result.getInt("capacity"));
                rooms.add(room);
            }

            System.out.println(roomAmount + " rooms found");
            RoomMessage createdMessage = new RoomMessage(RoomMessage.Command.RECIEVE_ROOMS, rooms);
            connection.sendTCP(createdMessage);

        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMessage error = new ErrorMessage();
            connection.sendTCP(error);
        }
    }
}

