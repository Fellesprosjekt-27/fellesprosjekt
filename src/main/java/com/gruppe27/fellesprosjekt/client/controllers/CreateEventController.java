package com.gruppe27.fellesprosjekt.client.controllers;

import java.time.LocalTime;

import com.gruppe27.fellesprosjekt.client.CalendarClient;
import com.gruppe27.fellesprosjekt.common.Event;
import com.gruppe27.fellesprosjekt.common.messages.EventMessage;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class CreateEventController {
	
	@FXML
	TextField emne;
	
	@FXML
	DatePicker dato;
	
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

	private CalendarClient calendarClient;
	
	@FXML
	private void handleCreateEventAction(){
		Event event = new Event();
		event.setName(emne.getText());
	
		event.setDate(dato.getValue());
		
		LocalTime startTime = LocalTime.parse(fraTid.getText());
		LocalTime endTime = LocalTime.parse(tilTid.getText());
		event.setStartTime(startTime);
		event.setEndTime(endTime);
		event.setCreator(calendarClient.getUser());
		
		EventMessage message = new EventMessage(EventMessage.Command.CREATE_EVENT, event);
		calendarClient.sendMessage(message);
	}
	
	@SuppressWarnings("unused")
	@FXML private void handleCancelAction(){
		
	}

	public void setClient(CalendarClient calendarClient) {
		this.calendarClient = calendarClient;
	}

}
