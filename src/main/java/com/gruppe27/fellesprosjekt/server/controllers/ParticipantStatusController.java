package com.gruppe27.fellesprosjekt.server.controllers;

import com.gruppe27.fellesprosjekt.common.User;
import com.gruppe27.fellesprosjekt.common.messages.ErrorMessage;
import com.gruppe27.fellesprosjekt.common.messages.GeneralMessage;
import com.gruppe27.fellesprosjekt.common.messages.ParticipantStatusMessage;
import com.gruppe27.fellesprosjekt.server.CalendarConnection;
import com.gruppe27.fellesprosjekt.server.DatabaseConnector;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ParticipantStatusController {

    private static ParticipantStatusController instance = null;

    protected ParticipantStatusController() {
    }


    public static ParticipantStatusController getInstance() {
        if (instance == null) {
            instance = new ParticipantStatusController();
        }
        return instance;
    }

    public void handleMessage(CalendarConnection connection, Object message) {
        ParticipantStatusMessage participantStatusMessage = (ParticipantStatusMessage) message;
        switch (participantStatusMessage.getCommand()) {
            case CHANGE_STATUS:
                changeStatus(connection, participantStatusMessage);
                break;
        }
    }

    private void changeStatus(CalendarConnection connection, ParticipantStatusMessage participantStatusMessage) {
        User user = connection.getUser();

        try {

            PreparedStatement statement = DatabaseConnector.getConnection().prepareStatement(
                    "UPDATE UserEvent SET status = ? WHERE username = ? AND event_id = ?"
            );
            statement.setString(1, participantStatusMessage.getStatus().toString());
            statement.setString(2, user.getUsername());
            statement.setInt(3, participantStatusMessage.getEventId());

            int result = statement.executeUpdate();


            GeneralMessage createdMessage = new GeneralMessage(GeneralMessage.Command.SUCCESSFUL_CREATE,
                    user.getName() + " har endret status på avtale nummer " + participantStatusMessage.getEventId());
            connection.sendTCP(createdMessage);


        } catch (SQLException e) {
            e.printStackTrace();
            ErrorMessage error = new ErrorMessage();
            connection.sendTCP(error);
        }
    }

}
