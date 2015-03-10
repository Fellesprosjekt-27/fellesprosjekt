package com.gruppe27.fellesprosjekt.client.controllers;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.gruppe27.fellesprosjekt.client.CalendarApplication;
import com.gruppe27.fellesprosjekt.client.CalendarClient;
import com.gruppe27.fellesprosjekt.client.components.MonthCalendarComponent;
import com.gruppe27.fellesprosjekt.common.Event;
import com.gruppe27.fellesprosjekt.client.components.NotificationList;
import com.gruppe27.fellesprosjekt.common.messages.EventMessage;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CalendarController implements Initializable {
    private CalendarApplication application;

    @FXML
    private MonthCalendarComponent calendar;

    @FXML
    private Button createEventButton;

    @FXML
    private NotificationList notificationList;

    public CalendarController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.calendar.setController(this);
        this.calendar.findEvents();
    }

    public void setApp(CalendarApplication application) {
        this.application = application;
    }

    public void handleCreateNewEvent() {
        application.createNewEvent();
    }

    public void getEventsForPeriod(LocalDate from, LocalDate to, ObservableList<Event> observableEvents) {
        EventMessage eventMessage = new EventMessage(EventMessage.Command.SEND_EVENTS, from, to);
        CalendarClient client = CalendarClient.getInstance();

        Listener listener = new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof EventMessage) {
                    EventMessage message = (EventMessage) object;
                    if (message.getCommand() == EventMessage.Command.RECEIVE_EVENTS) {
                        observableEvents.addAll(message.getEvents());
                        client.removeListener(this);
                    }
                }
            }
        };

        client.addListener(listener);
        client.sendMessage(eventMessage);
    }
}
