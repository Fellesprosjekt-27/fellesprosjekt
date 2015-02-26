package com.gruppe27.fellesprosjekt.client.controllers;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LogInController extends Application{
	
	private Stage stage;
	
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private Button ok, cancel;
	
	@FXML
	private void ok(){
		
	}
	
	@FXML
	private void cancel(){
		stage.close();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.stage = primaryStage;
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/LogIn.fxml"));
		Scene scene = new Scene(root, 300, 200);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	

}
