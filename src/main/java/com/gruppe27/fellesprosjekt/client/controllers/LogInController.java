package com.gruppe27.fellesprosjekt.client.controllers;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.gruppe27.fellesprosjekt.client.CalendarApplication;
import com.gruppe27.fellesprosjekt.client.CalendarClient;
import com.gruppe27.fellesprosjekt.common.messages.AuthCompleteMessage;
import com.gruppe27.fellesprosjekt.common.messages.AuthMessage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LogInController implements Initializable {
    private CalendarApplication application;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    public void setApp(CalendarApplication application) {
        this.application = application;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private boolean login(String username, String password) {
        AuthMessage testMessage = new AuthMessage(AuthMessage.Command.LOGIN, username, password);
        CalendarClient.getInstance().sendMessage(testMessage);
        // TODO: Listen for the result
        return true;
    }

    @FXML
    private void submit() {
        AuthMessage testMessage = new AuthMessage(AuthMessage.Command.LOGIN,
                usernameField.getText(), passwordField.getText());

        CalendarClient client = CalendarClient.getInstance();

        Listener loginListener = new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof AuthCompleteMessage) {
                    AuthCompleteMessage complete = (AuthCompleteMessage) object;

                    switch (complete.getCommand()) {
                        case SUCCESSFUL_LOGIN:
                            application.successfulLogin();
                            break;
                        case UNSUCCESSFUL_LOGIN:
                            // TODO: Show in GUI
                            System.out.println("Ugyldig brukernavn og/eller passord");
                            break;
                    }
                    client.removeListener(this);
                }
            }
        };

        client.addListener(loginListener);
        client.sendMessage(testMessage);
    }
}
