package com.gruppe27.fellesprosjekt.client.controllers;

import com.gruppe27.fellesprosjekt.client.CalendarApplication;
import com.gruppe27.fellesprosjekt.client.CalendarClient;
import com.gruppe27.fellesprosjekt.common.Event;
import com.gruppe27.fellesprosjekt.common.messages.EventMessage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class CreateEventController implements Initializable {
	
	@FXML
	TextField emne;

	@FXML DatePicker dato;

	@FXML
	TextField fraTid;
	
	@FXML
	TextField tilTid;
	
	@FXML
	ListView deltakere;
	
	@FXML
	ChoiceBox romValg;
	
	@FXML
	Button createEventButton;
	
	@FXML
	Button cancelButton;

    private CalendarApplication application;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public void setApp(CalendarApplication application) {
        this.application = application;
    }

	@FXML
	private void handleCreateEventAction() {
		Event event = new Event();
		event.setName(emne.getText());
	
		event.setDate(dato.getValue());
		
		LocalTime startTime = LocalTime.parse(fraTid.getText());
		LocalTime endTime = LocalTime.parse(tilTid.getText());
		event.setStartTime(startTime);
		event.setEndTime(endTime);
		event.setCreator(application.getUser()); // TODO: This should just be set on the backend instead
		
		EventMessage message = new EventMessage(EventMessage.Command.CREATE_EVENT, event);
		CalendarClient.getInstance().sendMessage(message);
	}
	
	@FXML private void handleCancelAction() {}
}
