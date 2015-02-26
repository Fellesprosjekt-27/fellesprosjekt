package com.gruppe27.fellesprosjekt.server.controllers;

import com.gruppe27.fellesprosjekt.common.Event;
import com.gruppe27.fellesprosjekt.common.User;
import com.gruppe27.fellesprosjekt.common.messages.*;
import com.gruppe27.fellesprosjekt.server.CalendarConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Andreas on 26.02.2015.
 */
public class EventController extends Controller {

    public EventController(CalendarConnection connection) {super(connection);}

    public void handleMessage(Object message) {
        EventMessage eventMessage = (EventMessage) message;
        switch (eventMessage.getCommand()) {
            case CREATE_EVENT:
                createEvent(eventMessage.getEvent());
                break;
        }
    }

    private void createEvent(Event event) {
        try {

            PreparedStatement statement = databaseConnection.prepareStatement(
                "INSERT INTO EVENT(name, date, start, end, creator) VALUES (?,?,?,?,?)"
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

        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMessage error = new ErrorMessage();
            calendarConnection.sendTCP(error);
        }
    }
}
