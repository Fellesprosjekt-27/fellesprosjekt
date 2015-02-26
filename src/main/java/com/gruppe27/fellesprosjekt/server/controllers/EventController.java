package com.gruppe27.fellesprosjekt.server.controllers;

import com.gruppe27.fellesprosjekt.common.Event;
import com.gruppe27.fellesprosjekt.common.User;
import com.gruppe27.fellesprosjekt.common.messages.*;
import com.gruppe27.fellesprosjekt.server.CalendarConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.SQLException;

public class EventController extends Controller {

    public EventController(Connection databaseConnection) {super(databaseConnection);}

    public void handleMessage(CalendarConnection connection, Object message) {
        EventMessage eventMessage = (EventMessage) message;
        switch (eventMessage.getCommand()) {
            case CREATE_EVENT:
                createEvent(connection, eventMessage.getEvent());
                break;
        }
    }

    private void createEvent(CalendarConnection connection, Event event) {
        try {

            PreparedStatement statement = databaseConnection.prepareStatement(
                "INSERT INTO Event(name, date, start, end, creator) VALUES (?,?,?,?,?)"
            );

            statement.setString(1, event.getName());
            statement.setString(2, event.getDate().toString());
            statement.setString(3, event.getStartTime().toString());
            statement.setString(4, event.getEndTime().toString());
            statement.setString(5, event.getCreator().getUsername());
            int result = statement.executeUpdate();


            System.out.println(result + " rows affected");
            GeneralMessage createdMessage = new GeneralMessage(GeneralMessage.Command.SUCCESSFUL_CREATE,
                    "Avtalen " + event.getName() + " opprettet.");
            connection.sendTCP(createdMessage);

        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMessage error = new ErrorMessage();
            connection.sendTCP(error);
        }
    }
}
