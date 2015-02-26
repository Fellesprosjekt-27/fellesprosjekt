package com.gruppe27.fellesprosjekt.client;
import com.gruppe27.fellesprosjekt.client.controllers.LogInController;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;


public class CalendarGUI extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        CalendarClient calendarClient = new CalendarClient();

        primaryStage.setTitle("Kalender");

//        Button button = new Button();
//        button.setText("Send test message");
//        button.setOnAction((ActionEvent e) -> {
//            AuthMessage testMessage = new AuthMessage(AuthMessage.Command.LOGIN, "testbruker", "passord");
//            calendarClient.sendMessage(testMessage);
//        });

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LogIn.fxml"));
        Parent root = (Parent)loader.load();
        LogInController loginController = loader.<LogInController>getController();
        loginController.setClient(calendarClient);

        Scene scene = new Scene(root, 500, 450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
