package com.gruppe27.fellesprosjekt.server.controllers;

import com.gruppe27.fellesprosjekt.common.Event;
import com.gruppe27.fellesprosjekt.common.Room;
import com.gruppe27.fellesprosjekt.common.User;
import com.gruppe27.fellesprosjekt.common.messages.ErrorMessage;
import com.gruppe27.fellesprosjekt.common.messages.EventMessage;
import com.gruppe27.fellesprosjekt.common.messages.GeneralMessage;
import com.gruppe27.fellesprosjekt.server.CalendarConnection;
import com.gruppe27.fellesprosjekt.server.DatabaseConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashSet;

public class EventController {
    public static final String EVENT_QUERY =
            "SELECT Event.id, Event.name, Event.date, Event.start, Event.end, Event.capacity_need, " +
                    "Room.name, Room.capacity, " +
                    "Creator.username, Creator.name, " +
                    "Participant.username, Participant.name, UserEvent.status " +
                    "FROM Event JOIN User AS Creator ON Event.creator = Creator.username " +
                    "JOIN UserEvent ON Event.id = UserEvent.event_id " +
                    "JOIN User AS Participant ON UserEvent.username = Participant.username " +
                    "JOIN Room ON Room.name = Event.room ";
    private static EventController instance = null;

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
                sendEvents(connection, eventMessage.getFrom(), eventMessage.getTo(), eventMessage.getUser());
                break;
        }
    }

    private void sendEvents(CalendarConnection connection, LocalDate from, LocalDate to, User user) {
        try {
            String query = EVENT_QUERY + "WHERE (Event.date >= ? AND Event.date <= ?) AND " +
                    "? IN (SELECT username FROM UserEvent WHERE event_id=Event.id) " +
                    " ORDER BY Event.id";

            PreparedStatement statement = DatabaseConnector.getConnection().prepareStatement(query);
            statement.setString(1, from.toString());
            statement.setString(2, to.toString());

            if (user == null) {
                statement.setString(3, connection.getUser().getUsername());
            } else {
                statement.setString(3, user.getUsername());
            }

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

    public HashSet<Event> parseEventResult(ResultSet result, String username) throws SQLException {
        int currentEventId = -1;
        Event event = null;
        HashSet<Event> events = new HashSet<>();
        while (result.next()) {
            if (result.getInt(1) != currentEventId) {
                event = new Event();
                currentEventId = result.getInt("Event.id");
                event.setName(result.getString("Event.name"));
                event.setDate(result.getDate("Event.date").toLocalDate());
                event.setStartTime(result.getTime("Event.start").toLocalTime());
                event.setEndTime(result.getTime("Event.end").toLocalTime());

                Room room =  new Room(result.getString("Room.name"),result.getInt("Room.capacity"));
                event.setRoom(room);

                User creator = new User(result.getString("Creator.username"), result.getString("Creator.name"));
                User participant = new User(result.getString("Participant.username"), result.getString("Participant.name"));
                if (participant.getUsername().equals(username)) {
                    event.setStatus(Event.Status.valueOf(result.getString("UserEvent.status")));
                }
                event.setCapacityNeed(result.getInt("Event.capacity_need"));



                event.addParticipant(participant);

                event.setCreator(creator);
                event.setId(currentEventId);
                events.add(event);
                System.out.println("add event");
            } else {
                if (event == null) {
                    return events;
                }
                User participant = new User(result.getString("Participant.username"), result.getString("Participant.name"));
                if (participant.getUsername().equals(username)) {
                    event.setStatus(Event.Status.valueOf(result.getString("UserEvent.status")));
                }
                event.addParticipant(participant);
                System.out.println("Add user");
            }
        }
        return events;
    }

    private void createEvent(CalendarConnection connection, Event event) {
        if (event.getId() != -1) {
            updateEvent(connection, event);
        } else {
            try {
                PreparedStatement statement = DatabaseConnector.getConnection().prepareStatement(
                        "INSERT INTO Event(name, date, start, end, creator, room, capacity_need) VALUES (?,?,?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS
                );

                statement.setString(1, event.getName());
                statement.setString(2, event.getDate().toString());
                statement.setString(3, event.getStartTime().toString());
                statement.setString(4, event.getEndTime().toString());
                statement.setString(5, connection.getUser().getUsername());
                if (event.getRoom() == null) {
                    statement.setString(6, null);
                } else {
                    statement.setString(6, event.getRoom().getRoomName());
                }
                statement.setInt(7, event.getCapacityNeed());
                int result = statement.executeUpdate();

                int eventId;
                ResultSet eventIdResultSet = statement.getGeneratedKeys();
                eventIdResultSet.next();
                eventId = eventIdResultSet.getInt(1);
                event.setId(eventId);

                int number_of_participants = 0;
                for (User participant : event.getUserParticipants()) {
                    PreparedStatement participantStatement = DatabaseConnector.getConnection().prepareStatement(
                            "INSERT INTO UserEvent(username,event_id) VALUES (?,?)"
                    );
                    participantStatement.setString(1, participant.getUsername());
                    participantStatement.setInt(2, eventId);
                    int participantResult = participantStatement.executeUpdate();
                    number_of_participants += participantResult;

                    NotificationController.getInstance().newEventNotification(event, participant);
                }

                System.out.println(number_of_participants + " participants added to event.");
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

    private void updateEvent(CalendarConnection connection, Event event) {
        try {
            PreparedStatement statement = DatabaseConnector.getConnection().prepareStatement(
                    "UPDATE Event " +
                            "SET Event.name=?, Event.date=?, Event.start=?, Event.end=?, " +
                            "Event.creator=?, Event.room=?, Event.capacity_need=? " +
                            "WHERE Event.id= ?"
            );

            statement.setString(1, event.getName());
            statement.setString(2, event.getDate().toString());
            statement.setString(3, event.getStartTime().toString());
            statement.setString(4, event.getEndTime().toString());
            statement.setString(5, connection.getUser().getUsername());
            if (event.getRoom() == null) {
                statement.setString(6, null);
            } else {
                statement.setString(6, event.getRoom().getRoomName());
            }

            if (event.getRoom() == null) {
                statement.setString(6, null);
            } else {
                statement.setString(6, event.getRoom().getRoomName());
            }

            statement.setInt(7, event.getCapacityNeed());
            statement.setInt(8, event.getId());
            int result = statement.executeUpdate();

            PreparedStatement removeUsersStatement = DatabaseConnector.getConnection().prepareStatement(
                    "DELETE FROM UserEvent WHERE event_id =?"
            );
            removeUsersStatement.setInt(1,event.getId());
            removeUsersStatement.execute();

            PreparedStatement creatorStatus = DatabaseConnector.getConnection().prepareStatement(
                    "INSERT INTO UserEvent(username, event_id, status) VALUES (?,?,?)"
            );
            creatorStatus.setString(1, event.getCreator().getUsername());
            creatorStatus.setInt(2, event.getId());
            creatorStatus.setString(3, Event.Status.ATTENDING.toString());
            result = creatorStatus.executeUpdate();

            int number_of_participants = 0;
            for (User participant : event.getUserParticipants()) {
                PreparedStatement participantStatement = DatabaseConnector.getConnection().prepareStatement(
                        "INSERT INTO UserEvent(username,event_id) VALUES (?,?)"
                );
                participantStatement.setString(1, participant.getUsername());
                participantStatement.setInt(2, event.getId());
                int participantResult = participantStatement.executeUpdate();
                number_of_participants += participantResult;

                NotificationController.getInstance().newEventNotification(event, participant);
                //TODO update notification, not new event notification.
            }

            System.out.println(number_of_participants + " participants added to event.");
            System.out.println(result + " rows affected");
            GeneralMessage createdMessage = new GeneralMessage(GeneralMessage.Command.SUCCESSFUL_CREATE,
                    "Avtalen " + event.getName() + " oppdatert.");
            connection.sendTCP(createdMessage);
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMessage error = new ErrorMessage();
            connection.sendTCP(error);
        }
    }
}
