package com.gruppe27.fellesprosjekt.client;

import com.gruppe27.fellesprosjekt.client.controllers.CreateEventController;
import com.gruppe27.fellesprosjekt.client.controllers.LogInController;
import com.gruppe27.fellesprosjekt.common.Event;
import com.gruppe27.fellesprosjekt.common.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashSet;


public class CalendarApplication extends Application {
    public static final int WIDTH = 500;
    public static final int HEIGHT = 450;

    private HashSet<Event> events;
    private Stage stage;

    private User user;

    public HashSet<Event> getEvents() {
        return events;
    }

    public void setEvents(HashSet<Event> events) {
        this.events = events;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        this.stage.setTitle("Kalender");
        gotoLogin();
        this.stage.show();
    }

    public User getUser() {
        return user;
    }

    public void successfulLogin(User user) {
        System.out.println("Nå har du logget inn.");
        this.user = user;
        gotoCreateEvent();

    }


    private void gotoLogin() {
        LogInController controller = (LogInController) replaceSceneContent("/fxml/LogIn.fxml");
        controller.setApp(this);
    }

    private void gotoCreateEvent() {
        CreateEventController controller = (CreateEventController) replaceSceneContent("/fxml/CreateEvent.fxml");
        controller.setApp(this);
    }

    private Initializable replaceSceneContent(String fxml) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));

        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }

        // replaceSceneContent can be called from a non-GUI thread, runLater makes it work
        Platform.runLater(() -> {
            Scene scene = new Scene(root, WIDTH, HEIGHT);
            stage.setScene(scene);
            stage.sizeToScene();
        });

        return (Initializable) loader.getController();
    }
}
