package com.gruppe27.fellesprosjekt.client;

import com.gruppe27.fellesprosjekt.common.Event;
import com.gruppe27.fellesprosjekt.common.User;
import com.gruppe27.fellesprosjekt.common.messages.AuthMessage;
import com.gruppe27.fellesprosjekt.common.messages.EventMessage;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;

public class CalendarGUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        CalendarClient calendarClient = new CalendarClient();

        primaryStage.setTitle("Kalender");

        Button button = new Button();
        button.setText("Send test message");
        button.setOnAction((ActionEvent e) -> {
            AuthMessage testMessage = new AuthMessage(AuthMessage.Command.LOGIN, "testbruker", "passord");
            calendarClient.sendMessage(testMessage);
        });

        Button eventButton = new Button();
        eventButton.setText("Send event");
        eventButton.setOnAction((ActionEvent e) -> {
            User user = new User("username","name");
            LocalDate date = LocalDate.of(2015,02,26);
            LocalTime start = LocalTime.of(14,00,00);
            LocalTime end = LocalTime.of(16,00,00);
            Event event = new Event("testevent", user, date,start,end);

            EventMessage eventMessage = new EventMessage(EventMessage.Command.CREATE_EVENT, event);
            calendarClient.sendMessage(eventMessage);
        });

        StackPane root = new StackPane();
        root.getChildren().add(button);
        root.getChildren().add(eventButton);

        Scene scene = new Scene(root, 500, 450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
