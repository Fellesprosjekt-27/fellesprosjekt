package com.gruppe27.fellesprosjekt.client.controllers;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.gruppe27.fellesprosjekt.client.CalendarApplication;
import com.gruppe27.fellesprosjekt.client.CalendarClient;
import com.gruppe27.fellesprosjekt.client.components.ChooseCalendarComboBox;
import com.gruppe27.fellesprosjekt.client.components.EventPopOver;
import com.gruppe27.fellesprosjekt.client.components.MonthCalendarComponent;
import com.gruppe27.fellesprosjekt.client.components.NotificationList;
import com.gruppe27.fellesprosjekt.client.events.EventBoxClicked;
import com.gruppe27.fellesprosjekt.common.Event;
import com.gruppe27.fellesprosjekt.common.User;
import com.gruppe27.fellesprosjekt.common.messages.EventMessage;
import com.gruppe27.fellesprosjekt.common.messages.ParticipantStatusMessage;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CalendarController implements Initializable {
    private CalendarApplication application;
    private boolean popOverFlag;
    private EventPopOver popOver;
    private ObjectProperty<User> currentUser;

    @FXML
    private ChooseCalendarComboBox chooseCalendarComboBox;

    @FXML
    private MonthCalendarComponent calendar;

    @FXML
    private Button createEventButton;

    @FXML
    private AnchorPane root;

    @FXML
    private NotificationList notificationList;

    public CalendarController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.calendar.setController(this);
        this.chooseCalendarComboBox.setController(this);

        currentUser = new SimpleObjectProperty<>();
        currentUser.addListener((observable, oldValue, newValue) ->
            this.calendar.findEvents()
        );

        this.calendar.findEvents();
        popOverFlag = false;
        root.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
            if (popOverFlag) {
                dismissPopOver();
            }
        });

        root.addEventHandler(EventBoxClicked.eventType, e -> {
            if (!popOverFlag) {
                popOver = new EventPopOver((EventBoxClicked) e, this);
                popOverFlag = true;
            } else {
                dismissPopOver();
            }

        });

    }

    public void setApp(CalendarApplication application) {
        this.application = application;
    }

    public void handleCreateNewEvent() {
        if (popOverFlag) {
            dismissPopOver();
        }
        application.createNewEvent();
    }
    public User getConnectedUser(){
        return application.getUser();
    }

    public void getEventsForPeriod(LocalDate from, LocalDate to, ObservableList<Event> observableEvents) {
        EventMessage eventMessage = new EventMessage(currentUser.getValue(), EventMessage.Command.SEND_EVENTS, from, to);
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

    public void handleChangeParticipationStatus(Event event) {
        CalendarClient client = CalendarClient.getInstance();
        ParticipantStatusMessage statusMessage = new ParticipantStatusMessage(
                ParticipantStatusMessage.Command.CHANGE_STATUS, event);
        client.sendMessage(statusMessage);
    }

    public void dismissPopOver() {
        popOver.hide();
        popOverFlag = false;
        root.getChildren().remove(popOver.getPopOver());
    }

    public AnchorPane getRoot() {
        return root;
    }

    public User getCurrentUser() {
        return currentUser.getValue();
    }

    public void setCurrentUser(User user) {
        this.currentUser.setValue(user);
    }

    public void resetCurrentUser() {
        setCurrentUser(null);
        this.chooseCalendarComboBox.resetUser();
    }

    public void editEvent(Event event) {
        dismissPopOver();
        application.gotoEditEvent(event);
    }
}
