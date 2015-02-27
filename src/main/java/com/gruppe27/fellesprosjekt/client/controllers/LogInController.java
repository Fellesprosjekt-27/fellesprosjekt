package com.gruppe27.fellesprosjekt.client.controllers;

import com.gruppe27.fellesprosjekt.client.CalendarClient;
import com.gruppe27.fellesprosjekt.common.messages.AuthMessage;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LogInController{
	
	@FXML
	private TextField usernameField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private Button okButton, cancelButton;
	
	private CalendarClient client;
	
	@FXML
	private void ok(){
		AuthMessage testMessage = new AuthMessage(AuthMessage.Command.LOGIN, usernameField.getText(), passwordField.getText());
        client.sendMessage(testMessage);
	}
	
	@FXML
	private void cancel(){
		cancelButton.getScene().getWindow().hide();
	}

	public void setClient(CalendarClient client) {
		this.client = client;
	}
	

}
