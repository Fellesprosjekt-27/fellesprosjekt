package com.gruppe27.fellesprosjekt.client.controllers;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.gruppe27.fellesprosjekt.client.CalendarApplication;
import com.gruppe27.fellesprosjekt.client.CalendarClient;
import com.gruppe27.fellesprosjekt.common.Event;
import com.gruppe27.fellesprosjekt.common.Room;
import com.gruppe27.fellesprosjekt.common.messages.EventMessage;
import com.gruppe27.fellesprosjekt.common.messages.RoomMessage;
import com.gruppe27.fellesprosjekt.common.messages.RoomRequestMessage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.ResourceBundle;

public class CreateEventController implements Initializable {
    @FXML
    Button getEventTest;

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

    private CalendarApplication application;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setApp(CalendarApplication application) {
        this.application = application;
    }
    @FXML
    private void handleGetRooms() {
        LocalDate date = LocalDate.parse("2015-03-05");
        LocalTime start = LocalTime.parse("12:30");
        LocalTime end = LocalTime.parse("13:00");
        int capacity = 2;
        RoomRequestMessage message = new RoomRequestMessage(RoomRequestMessage.Command.ROOM_REQUEST, date,start,end,capacity);

        CalendarClient client = CalendarClient.getInstance();

        Listener roomListener = new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof RoomMessage) {
                    RoomMessage message = (RoomMessage) object;

                    switch (message.getCommand()) {
                        case RECEIVE_ROOMS:
                            HashSet<Room> rooms = message.getRooms();
                            System.out.println(rooms);
                            break;
                    }
                    client.removeListener(this);
                }
            }
        };
        client.addListener(roomListener);
        client.sendMessage(message);
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

    @FXML
    private void handleCancelAction() {
    }
}
