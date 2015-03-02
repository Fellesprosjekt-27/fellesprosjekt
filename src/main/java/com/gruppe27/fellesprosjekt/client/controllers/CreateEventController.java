package com.gruppe27.fellesprosjekt.client.controllers;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
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
import java.util.HashSet;
import java.util.ResourceBundle;

public class CreateEventController implements Initializable {
	@FXML
    Button getEventTest;


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
    private void getEvents() {
        EventMessage message = new EventMessage(EventMessage.Command.SEND_ALL, new Event());


        CalendarClient client = CalendarClient.getInstance();

        Listener eventListener = new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof EventMessage) {
                    EventMessage complete = (EventMessage) object;

                    switch (complete.getCommand()) {
                        case RECIEVE_ALL:
                            HashSet<Event> events = complete.getEvents();
                            application.setEvents(events);
                            System.out.println(events);
                            break;
                    }
                    client.removeListener(this);
                }
            }
        };
        client.addListener(eventListener);
        client.sendMessage(message);
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

		EventMessage message = new EventMessage(EventMessage.Command.CREATE_EVENT, event);
		CalendarClient.getInstance().sendMessage(message);
	}
	
	@FXML private void handleCancelAction() {}
}
