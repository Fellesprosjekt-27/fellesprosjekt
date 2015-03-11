package com.gruppe27.fellesprosjekt.client.controllers;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.gruppe27.fellesprosjekt.client.CalendarApplication;
import com.gruppe27.fellesprosjekt.client.CalendarClient;
import com.gruppe27.fellesprosjekt.client.components.EventPopOver;
import com.gruppe27.fellesprosjekt.client.components.MonthCalendarComponent;
import com.gruppe27.fellesprosjekt.client.events.EventBoxClicked;
import com.gruppe27.fellesprosjekt.common.Event;
import com.gruppe27.fellesprosjekt.common.messages.EventMessage;
import com.gruppe27.fellesprosjekt.common.messages.ParticipantStatusMessage;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.PopOver;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CalendarController implements Initializable {
    private CalendarApplication application;
    private boolean popOverFlag;
    private EventPopOver popOver;
    ParticipantStatusMessage.Status pStatus;

    @FXML
    private MonthCalendarComponent calendar;

    @FXML
    private Button createEventButton;

    @FXML
    private AnchorPane root;

    public CalendarController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.calendar.setController(this);
        this.calendar.findEvents();
        popOverFlag = false;
        calendar.addEventHandler(MouseEvent.MOUSE_RELEASED, mEvent -> {
            if (!(mEvent.getTarget() instanceof PopOver) && popOverFlag) {
                popOver.hide();
                popOverFlag = false;
                root.getChildren().remove(popOver.getPopOver());
            }
        });
        root.addEventHandler(EventBoxClicked.eventType, e -> {
            if (!popOverFlag) {
                popOver = new EventPopOver((EventBoxClicked) e, this);
                popOverFlag = true;
            } else {
                popOver.hide();
                popOverFlag = false;
                root.getChildren().remove(popOver.getPopOver());
            }

        });

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

    public void handleChangeParticipationStatus(String status, int id) {
        CalendarClient client = CalendarClient.getInstance();
        pStatus = ParticipantStatusMessage.Status.MAYBE;
        switch(status){
            case "Deltar":
                pStatus = ParticipantStatusMessage.Status.ATTENDING;
                break;
            case "Kanskje":
                pStatus = ParticipantStatusMessage.Status.MAYBE;
                break;
            case "Deltar ikke":
                pStatus = ParticipantStatusMessage.Status.NOT_ATTENDING;
                break;
        }
        ParticipantStatusMessage statusMessage = new ParticipantStatusMessage(ParticipantStatusMessage.Command.CHANGE_STATUS, pStatus, id);
        client.sendMessage(statusMessage);
    }

    public AnchorPane getRoot() {
        return root;
    }
}
