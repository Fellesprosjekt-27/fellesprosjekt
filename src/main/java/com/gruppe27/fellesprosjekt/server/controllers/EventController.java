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

    private static final String EVENT_QUERY =
            "SELECT Event.id, Event.name, Event.date, Event.start, Event.end, Creator.username, Creator.name, " +
            "Participant.username, Participant.name, status " +
            "FROM Event JOIN User AS Creator ON Event.creator = Creator.username " +
            "JOIN UserEvent ON Event.id = UserEvent.event_id " +
            "JOIN User AS Participant ON UserEvent.username = Participant.username ";

    protected EventController() {
    }

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
            case SEND_EVENTS:
                sendEvents(connection, eventMessage.getFrom(), eventMessage.getTo());
                break;
        }
    }

    private void sendEvents(CalendarConnection connection, LocalDate from, LocalDate to) {
        try {
            String query = EVENT_QUERY + "WHERE Event.date >= ? AND Event.date <= ? ORDER BY Event.id";
            PreparedStatement statement = DatabaseConnector.getConnection().prepareStatement(query);
            statement.setString(1, from.toString());
            statement.setString(2, to.toString());
            statement.setString(3, connection.getUser().getUsername());

            ResultSet resultSet = statement.executeQuery();
            HashSet<Event> events = parseEventResult(resultSet, connection.getUser().getUsername());
            EventMessage createdMessage = new EventMessage(EventMessage.Command.RECEIVE_EVENTS, events);
            connection.sendTCP(createdMessage);
            System.out.println("sent " + events.size() + "events.");
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMessage error = new ErrorMessage();
            connection.sendTCP(error);
        }
    }

    private HashSet<Event> parseEventResult(ResultSet result, String username) throws SQLException {
        int currentEventId = -1;
        Event event = null;
        HashSet<Event> events = new HashSet<>();
        while (result.next()) {
            if (result.getInt(1) != currentEventId) {
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
                if (participant.getUsername() == username) {
                    event.setStatus(result.getString(10));
                }

                event.addParticipant(participant);

                event.setCreator(creator);
                event.setId(currentEventId);
                events.add(event);
                System.out.println("add event");
            } else {
                if (event == null) {
                    return events;
                }
                User participant = new User();
                participant.setUsername(result.getString(8));
                participant.setName(result.getString(9));
                if (participant.getUsername() == username) {
                    event.setStatus(result.getString(10));
                }
                event.addParticipant(participant);
                System.out.println("Add user");
            }
        }
        return events;
    }

    private void createEvent(CalendarConnection connection, Event event) {
        try {

            PreparedStatement statement = DatabaseConnector.getConnection().prepareStatement(
                    "INSERT INTO Event(name, date, start, end, creator,room) VALUES (?,?,?,?,?,?)"
            );

            statement.setString(1, event.getName());
            statement.setString(2, event.getDate().toString());
            statement.setString(3, event.getStartTime().toString());
            statement.setString(4, event.getEndTime().toString());
            statement.setString(5, connection.getUser().getUsername());
            statement.setString(6, event.getRoom().getRoomName());
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
