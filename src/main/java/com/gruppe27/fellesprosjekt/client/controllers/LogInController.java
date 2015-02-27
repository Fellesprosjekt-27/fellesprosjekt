package com.gruppe27.fellesprosjekt.client.controllers;

import com.gruppe27.fellesprosjekt.client.CalendarApplication;
import com.gruppe27.fellesprosjekt.client.CalendarClient;
import com.gruppe27.fellesprosjekt.common.messages.AuthMessage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
        if (login(usernameField.getText(), passwordField.getText())) {
            application.successfulLogin();
        } else {
            System.out.println("Dårlig login.");
        }
    }
}
