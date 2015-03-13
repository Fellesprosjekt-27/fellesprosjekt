package com.gruppe27.fellesprosjekt.client.controllers;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.gruppe27.fellesprosjekt.client.CalendarApplication;
import com.gruppe27.fellesprosjekt.client.CalendarClient;
import com.gruppe27.fellesprosjekt.client.SortableText;
import com.gruppe27.fellesprosjekt.client.components.ValidationDecoration;
import com.gruppe27.fellesprosjekt.common.Event;
import com.gruppe27.fellesprosjekt.common.ParticipantUser;
import com.gruppe27.fellesprosjekt.common.Room;
import com.gruppe27.fellesprosjekt.common.User;
import com.gruppe27.fellesprosjekt.common.messages.EventMessage;
import com.gruppe27.fellesprosjekt.common.messages.ParticipantUserMessage;
import com.gruppe27.fellesprosjekt.common.messages.RequestMessage;
import com.gruppe27.fellesprosjekt.common.messages.RoomMessage;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.ResourceBundle;


public class CreateEventController implements Initializable {

    @FXML
    TextField emne;

    @FXML
    DatePicker datePicker;

    @FXML
    TextField fromTimeField;

    @FXML
    TextField toTimeField;

    @FXML
    ListView<String> participantsListView;

    @FXML
    ParticipantComboBox participantComboBox;

    @FXML
    Button addParticipantButton;

    @FXML
    Button fjernDeltakere;

    @FXML
    ChoiceBox<String> roomChoiceBox;

    @FXML
    TextField capacityField;

    @FXML
    Button createEventButton;

    @FXML
    Button cancelButton;

    private Room currentRoom;

    private ArrayList<Room> roomsArray;
    private CalendarApplication application;

    private Map<String, ParticipantUser> allUsers;
    private ObservableList<SortableText> availableUsersObservable;
    private HashSet<User> participants;

    private ValidationSupport vd = new ValidationSupport();

    public CreateEventController() {
        participants = new LinkedHashSet<>();
        roomsArray = new ArrayList<>();
        currentRoom = null;
    }

    public void emptyRoomsArray() {
        this.roomsArray = new ArrayList<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setDisableStates(true);

        addListeners();

        registerValidators();

    }

    public boolean isTimeValid(String fromTime, String toTime) {
        try {
            return fromTime.matches("([0-1]?[0-9]|2[0-3]):[0-5][0-9]") &&
                    toTime.matches("([0-1]?[0-9]|2[0-3]):[0-5][0-9]") &&
                    LocalTime.parse(toTime).compareTo(LocalTime.parse(fromTime)) > 0;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public void setApp(CalendarApplication application) {
        this.application = application;
    }

    @FXML
    private void handleChoiceboxClicked() {
        LocalDate date = datePicker.getValue();
        LocalTime start = LocalTime.parse(fromTimeField.getText());
        LocalTime end = LocalTime.parse(toTimeField.getText());
        int capacity = Integer.parseInt(capacityField.getText());
        this.updateCurrentRooms(date, start, end, capacity);
        //TODO needs time to update rooms before I can do something.

    }

    @FXML
    private void updateCurrentRooms(LocalDate date, LocalTime start, LocalTime end, int capacity) {
        RequestMessage message = new RequestMessage(RequestMessage.Command.ROOM_REQUEST, date, start, end, capacity);

        CalendarClient client = CalendarClient.getInstance();

        Listener roomListener = new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof RoomMessage) {
                    RoomMessage message = (RoomMessage) object;

                    switch (message.getCommand()) {
                        case RECEIVE_ROOMS:
                            updateChoiceBox(message.getRooms());
                            break;
                    }
                    client.removeListener(this);
                }
            }

        };

        client.addListener(roomListener);
        client.sendMessage(message);

    }

    private void updateChoiceBox(HashSet<Room> rooms) {
        this.emptyRoomsArray();
        this.roomsArray.addAll(rooms);
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (Room room : roomsArray) {
            String roomString = room.toString();
            stringArrayList.add(roomString);
        }

        ObservableList<String> observableList = FXCollections.observableArrayList(stringArrayList);

        Platform.runLater(() -> {
            roomChoiceBox.setItems(observableList);
            roomChoiceBox.show();
        });

    }

    @FXML
    private void handleComboBoxClicked() {
        System.out.println("combobox clicked");
        if (allUsers == null) {
            getAllUsers();
        }
        //TODO Validering
    }

    private void getAllUsers() {
        LocalDate date = datePicker.getValue();
        LocalTime start = LocalTime.parse(fromTimeField.getText());
        LocalTime end = LocalTime.parse(toTimeField.getText());

        RequestMessage message = new RequestMessage(RequestMessage.Command.USER_REQUEST, date, start, end, -1);

        CalendarClient client = CalendarClient.getInstance();

        Listener getUsersListener = new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof ParticipantUserMessage) {
                    ParticipantUserMessage complete = (ParticipantUserMessage) object;
                    switch (complete.getCommand()) {
                        case RECEIVE_ALL:
                            setAllUsers(complete.getParticipantUsers());
                            break;
                        case SEND_ALL:
                            break;
                    }
                    client.removeListener(this);
                }
            }

        };
        client.addListener(getUsersListener);
        client.sendMessage(message);
    }

    private void setAllUsers(HashSet<ParticipantUser> allUsers) {
        this.allUsers = new HashMap<>();
        availableUsersObservable = FXCollections.observableArrayList();
        for (ParticipantUser participantUser : allUsers) {

            this.allUsers.put(participantUser.getUsername(), participantUser);
            SortableText text = new SortableText(participantUser.getUsername());
            if (participantUser.isBusy()) {
                text.setFill(Color.RED);
            } else {
                text.setFill(Color.GREEN);
            }
            availableUsersObservable.add(text);
        }

        Collections.sort(availableUsersObservable);
        Platform.runLater(() -> {
            participantComboBox.init(availableUsersObservable);
        });
    }


    @FXML
    private void handleAddParticipant() {
        String username = participantComboBox.getValue().getText();
        participants.add(allUsers.get(username));
        updateListView();
        availableUsersObservable.remove(participantComboBox.getValue());
        participantComboBox.setValue(null);
        participantComboBox.init(availableUsersObservable);
    }

    private void updateListView() {
        ObservableList<String> observable = FXCollections.observableArrayList();
        for (User user : participants) {
            observable.add(user.getUsername());
        }

        Platform.runLater(() -> {
            participantsListView.setItems(observable);
        });
    }


    @FXML
    private void handleCreateEventAction() {

        Event event = new Event();
        event.setName(emne.getText());

        event.setDate(datePicker.getValue());

        LocalTime startTime = LocalTime.parse(fromTimeField.getText());
        LocalTime endTime = LocalTime.parse(toTimeField.getText());
        event.setCreator(application.getUser());
        participants.add(event.getCreator());
        event.setStartTime(startTime);
        event.setEndTime(endTime);
        event.setAllParticipants(participants);
        event.setRoom(currentRoom);

        EventMessage message = new EventMessage(EventMessage.Command.CREATE_EVENT, event);
        //TODO: add functions backend to invite all users from the eventmessage
        //TODO: make an invite message
        CalendarClient.getInstance().sendMessage(message);
    }

    @FXML
    private void handleCancelAction() {
        application.cancelCreateNewEvent();
    }

    private void setDisableStates(boolean state) {
        createEventButton.setDisable(state);
        roomChoiceBox.setDisable(state);
    }

    private boolean isValid() {
        return !emne.getText().isEmpty() && canPickRoom();
    }

    private boolean canPickRoom() {
        return datePicker.getValue() != null &&
                isTimeValid(fromTimeField.getText(), toTimeField.getText()) &&
                capacityField.getText().matches("[\\d]+");
    }

    private void addListeners() {
        roomChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            currentRoom = roomsArray.get(newValue.intValue());
        });

        emne.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isValid()) {
                setDisableStates(false);
            } else {
                createEventButton.setDisable(true);
            }
        });

        ChangeListener roomListener = (observable, oldValue, newValue) -> {
            roomChoiceBox.setValue(null);
            if (isValid()) {
                setDisableStates(false);
            } else if (canPickRoom()) {
                roomChoiceBox.setDisable(false);
            } else {
                setDisableStates(true);
            }
        };

        datePicker.valueProperty().addListener(roomListener);
        fromTimeField.textProperty().addListener(roomListener);
        toTimeField.textProperty().addListener(roomListener);
        capacityField.textProperty().addListener(roomListener);

        roomChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (isValid()) {
                createEventButton.setDisable(false);
            } else {
                createEventButton.setDisable(true);
            }
        });
    }

    private void registerValidators() {
        vd.registerValidator(emne, Validator.createEmptyValidator("Tittel mangler", Severity.WARNING));
        vd.registerValidator(datePicker, Validator.createEmptyValidator("Dato mangler", Severity.WARNING));
        vd.registerValidator(capacityField, Validator.createRegexValidator("Kapasiteten må være et tall lavere enn 20", "^$|[0-9]+", Severity.ERROR));

        vd.registerValidator(toTimeField,
                Validator.combine(
                        Validator.createEmptyValidator("Sluttidspunkt mangler", Severity.WARNING),
                        Validator.createRegexValidator("Tid må være på formen hh:mm", "^$|([0-1]?[0-9]|2[0-3]):[0-5][0-9]", Severity.ERROR),
                        Validator.createPredicateValidator(o -> isTimeValid(fromTimeField.getText(),
                                toTimeField.getText()),
                                "Sluttidspunkt må være etter starttidspunkt",
                                Severity.ERROR)));

        vd.registerValidator(toTimeField,
                Validator.combine(
                        Validator.createEmptyValidator("Sluttidspunkt mangler", Severity.WARNING),
                        Validator.createRegexValidator("Tid må være på formen hh:mm", "^$|([0-1]?[0-9]|2[0-3]):[0-5][0-9]", Severity.ERROR),
                        Validator.createPredicateValidator(o -> isTimeValid(fromTimeField.getText(),
                                toTimeField.getText()), "Sluttidspunkt må være etter starttidspunkt", Severity.ERROR)));

        vd.registerValidator(fromTimeField,
                Validator.combine(
                        Validator.createEmptyValidator("Starttidspunkt mangler", Severity.WARNING),
                        Validator.createRegexValidator("Tid må være på formen hh:mm",
                                "^$|([0-1]?[0-9]|2[0-3]):[0-5][0-9]", Severity.ERROR)));


        vd.setValidationDecorator(new ValidationDecoration());
    }
}




