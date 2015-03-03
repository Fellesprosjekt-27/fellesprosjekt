package com.gruppe27.fellesprosjekt.server.controllers;

import com.gruppe27.fellesprosjekt.common.Event;
import com.gruppe27.fellesprosjekt.common.User;
import com.gruppe27.fellesprosjekt.common.messages.ErrorMessage;
import com.gruppe27.fellesprosjekt.common.messages.EventMessage;
import com.gruppe27.fellesprosjekt.common.messages.GeneralMessage;
import com.gruppe27.fellesprosjekt.server.CalendarConnection;
import com.gruppe27.fellesprosjekt.server.DatabaseConnector;

import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;

public class EventController {
    private static EventController instance = null;

    protected EventController() {}

    public static EventController getInstance() {
        if (instance == null) {
            instance = new EventController();
        }
        return instance;
    }

    public void handleMessage(CalendarConnection connection, Object message) {
        EventMessage eventMessage = (EventMessage) message;
        switch (eventMessage.getCommand()) {
            case CREATE_EVENT:
                createEvent(connection, eventMessage.getEvent());
                break;
            case SEND_ALL:
                sendAllEvents(connection);
        }
    }
    private void sendAllEvents(CalendarConnection connection) {
        try {
            PreparedStatement statement = DatabaseConnector.getConnection().prepareStatement(
                    "SELECT Event.id,Event.name,Event.date,Event.start,Event.end, Creator.username,Creator.name, Participant.username, Participant.name" +
                            " FROM Event JOIN User AS Creator ON Event.creator = Creator.username" +
                            " JOIN UserEvent ON Event.id = UserEvent.event_id JOIN User AS Participant ON UserEvent.username = Participant.username");

            ResultSet resultSet = statement.executeQuery();
            HashSet<Event> events = parseEventResult(resultSet);
            EventMessage createdMessage = new EventMessage(EventMessage.Command.RECIEVE_ALL, events);
            connection.sendTCP(createdMessage);
            System.out.println("sent " + events.size() + "events.");
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMessage error = new ErrorMessage();
            connection.sendTCP(error);
        }
    }

    private void sendConnectedUserEvents(CalendarConnection connection) {
        HashSet<Event> events = getUserEvents(connection, connection.getUser());
        EventMessage createdMessage = new EventMessage(EventMessage.Command.RECIEVE_ALL, events);
        connection.sendTCP(createdMessage);
    }
    private HashSet<Event> getUserEvents(CalendarConnection connection, User user) {
        try {
            PreparedStatement statement = DatabaseConnector.getConnection().prepareStatement(
                    "SELECT Event.id,Event.name,Event.date,Event.start,Event.end, Creator.username,Creator.name, Participant.username, Participant.name" +
                            " FROM Event JOIN User AS Creator ON Event.creator = Creator.username" +
                            " JOIN UserEvent ON Event.id = UserEvent.event_id JOIN User AS Participant ON UserEvent.username = Participant.username" +
                            " WHERE Participant.username = " + user.getUsername()
            );
            ResultSet result = statement.executeQuery();

            HashSet<Event> events = parseEventResult(result);

            return events;


        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMessage error = new ErrorMessage();
            connection.sendTCP(error);
            return null;
        }
    }

    private HashSet<Event> parseEventResult(ResultSet result) throws SQLException {
        int currentEventId = -1;
        Event event = null;
        HashSet<Event> events = new HashSet<Event>();
        while(result.next()) {
            if (result.getInt("Event.id") != currentEventId ) {
                event = new Event();
                User creator = new User();
                User participant = new User();
                currentEventId = result.getInt(1);
                event.setName(result.getString(2));
                event.setDate(result.getDate(3).toLocalDate());
                event.setStartTime(result.getTime(4).toLocalTime());
                event.setEndTime(result.getTime(5).toLocalTime());
                creator.setUsername(result.getString(6));
                creator.setName(result.getString(7));
                participant.setUsername(result.getString(8));
                participant.setName(result.getString(9));

                event.addParticipant(participant);

                event.setCreator(creator);
                events.add(event);
                System.out.println("add event");
            } else {
                if(event == null) {
                    return events;
                }
                User participant = new User();
                participant.setUsername(result.getString(8));
                participant.setName(result.getString(9));
                event.addParticipant(participant);
                System.out.println("Add user");
            }
        }
        return events;
    }

    private void createEvent(CalendarConnection connection, Event event) {
        try {

            PreparedStatement statement = DatabaseConnector.getConnection().prepareStatement(
                    "INSERT INTO Event(name, date, start, end, creator) VALUES (?,?,?,?,?)"
            );

            statement.setString(1, event.getName());
            statement.setString(2, event.getDate().toString());
            statement.setString(3, event.getStartTime().toString());
            statement.setString(4, event.getEndTime().toString());
            statement.setString(5, connection.getUser().getUsername());
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
