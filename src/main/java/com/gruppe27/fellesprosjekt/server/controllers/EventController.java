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
                            " JOIN UserEvent ON Event.id = UserEvent.event_id JOIN User AS Participant ON UserEvent.username = Participant.username"
            );
            HashSet<Event> events =  new HashSet<>();
            int eventAmount = 0;
            ResultSet result = statement.executeQuery();
            int currentEventId = -1;
            Event event = null;
            while(result.next()) {
                if( !(result.getInt("Event.id") == currentEventId) ) {
                    eventAmount++;
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
                    User participant = new User();
                    participant.setUsername(result.getString(8));
                    participant.setName(result.getString(9));
                    if(event == null) {return;}
                    event.addParticipant(participant);
                    System.out.println("Add user");
                }

            }

            System.out.println(eventAmount + " events sent");
            EventMessage createdMessage = new EventMessage(EventMessage.Command.RECIEVE_ALL, events);
            connection.sendTCP(createdMessage);

        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMessage error = new ErrorMessage();
            connection.sendTCP(error);
        }
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
