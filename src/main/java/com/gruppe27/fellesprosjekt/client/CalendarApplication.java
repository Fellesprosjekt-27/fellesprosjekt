package com.gruppe27.fellesprosjekt.client;
import com.gruppe27.fellesprosjekt.client.controllers.LogInController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

import java.io.IOException;


public class CalendarApplication extends Application {
    private Stage stage;

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

    public void successfulLogin() {
        System.out.println("Nå har du logget inn.");
    }

    private void gotoLogin() {
        LogInController loginController = (LogInController) replaceSceneContent("/fxml/LogIn.fxml");
        loginController.setApp(this);
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

        Scene scene = new Scene(root, 500, 450);
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }
}
