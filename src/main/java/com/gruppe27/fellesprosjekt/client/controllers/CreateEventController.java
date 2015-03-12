package com.gruppe27.fellesprosjekt.client.controllers;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.gruppe27.fellesprosjekt.client.CalendarApplication;
import com.gruppe27.fellesprosjekt.client.CalendarClient;
import com.gruppe27.fellesprosjekt.client.components.ValidationDecoration;
import com.gruppe27.fellesprosjekt.common.Event;
import com.gruppe27.fellesprosjekt.common.Room;
import com.gruppe27.fellesprosjekt.common.User;
import com.gruppe27.fellesprosjekt.common.messages.EventMessage;
import com.gruppe27.fellesprosjekt.common.messages.RoomMessage;
import com.gruppe27.fellesprosjekt.common.messages.RoomRequestMessage;
import com.gruppe27.fellesprosjekt.common.messages.UserMessage;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Predicate;


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
    ChoiceBox<User> romValg;

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

    private Map<String, User> allUsers;
    private ObservableList<String> availableUsersObservable;
    private HashSet<User> participants;

    private ValidationSupport vd = new ValidationSupport();

    public void emptyRoomsArray() {
        this.roomsArray = new ArrayList<>();
    }

    public CreateEventController() {
        participants = new LinkedHashSet<>();
        roomsArray = new ArrayList<>();
        currentRoom = null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getAllUsers();
        roomChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                currentRoom = roomsArray.get(newValue.intValue());
            }
        });
        disableButtons();

        addListeners();

        registerValidators();

    }

    public boolean isTimeValid(String fromTime, String toTime) {
        return fromTime.matches("([0-1]?[0-9]|2[0-3]):[0-5][0-9]") && toTime.matches("([0-1]?[0-9]|2[0-3]):[0-5][0-9]") && LocalTime.parse(toTime).compareTo(LocalTime.parse(fromTime)) > 0;
    }

    ;

    public void setApp(CalendarApplication application) {
        this.application = application;
    }

    private void getAllUsers() {
        UserMessage message = new UserMessage(UserMessage.Command.SEND_ALL);

        CalendarClient client = CalendarClient.getInstance();

        Listener getUsersListener = new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof UserMessage) {
                    UserMessage complete = (UserMessage) object;
                    switch (complete.getCommand()) {
                        case RECEIVE_ALL:
                            setAllUsers(complete.getUsers());
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

    @FXML
    private void handleChoiceboxClicked() {
        System.out.println("Cbox clicked.");
        LocalDate date = datePicker.getValue();
        LocalTime start = LocalTime.parse(fromTimeField.getText());
        LocalTime end = LocalTime.parse(toTimeField.getText());
        int capacity = Integer.parseInt(capacityField.getText());
        this.updateCurrentRooms(date, start, end, capacity);
        //TODO needs time to update rooms before I can do something.

    }

    @FXML
    private void updateCurrentRooms(LocalDate date, LocalTime start, LocalTime end, int capacity) {
        RoomRequestMessage message = new RoomRequestMessage(RoomRequestMessage.Command.ROOM_REQUEST, date, start, end, capacity);

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

    private void setAllUsers(HashSet<User> allUsers) {
        this.allUsers = new HashMap<>();
        availableUsersObservable = FXCollections.observableArrayList();
        for (User user : allUsers) {
            this.allUsers.put(user.getUsername(), user);
            availableUsersObservable.add(user.getUsername());
        }

        Collections.sort(availableUsersObservable);


        Platform.runLater(() -> {
            participantComboBox.init(availableUsersObservable);
        });
    }


    @FXML
    private void handleAddParticipant() {
        String username = participantComboBox.getValue();
        participants.add(allUsers.get(username));
        updateListView();

        availableUsersObservable.remove(participantComboBox.getValue());
        participantComboBox.setValue(null);
        participantComboBox.setItems(availableUsersObservable);
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
    private void disableButtons(){
        createEventButton.setDisable(true);
        roomChoiceBox.setDisable(true);
    }
    private void addListeners(){
        emne.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!emne.getText().isEmpty() && datePicker.getValue() != null && isTimeValid(fromTimeField.getText(), toTimeField.getText())) {
                createEventButton.setDisable(false);
            } else {
                createEventButton.setDisable(true);
            }
        });
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!emne.getText().isEmpty() && datePicker.getValue() != null && isTimeValid(fromTimeField.getText(), toTimeField.getText())) {
                createEventButton.setDisable(false);
            } else {
                createEventButton.setDisable(true);
            }
        });
        fromTimeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!emne.getText().isEmpty() && datePicker.getValue() != null && isTimeValid(fromTimeField.getText(), toTimeField.getText())) {
                createEventButton.setDisable(false);
            } else {
                createEventButton.setDisable(true);
            }
        });
        toTimeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!emne.getText().isEmpty() && datePicker.getValue() != null && isTimeValid(fromTimeField.getText(), toTimeField.getText())) {
                createEventButton.setDisable(false);
            } else {
                createEventButton.setDisable(true);
            }
        });
        capacityField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (capacityField.getText().matches("[1-9]+")) {
                roomChoiceBox.setDisable(false);
            } else {
                roomChoiceBox.setDisable(true);
            }
        });
    }
    private void registerValidators(){
        vd.registerValidator(emne, Validator.createEmptyValidator("Tittel mangler", Severity.WARNING));
        vd.registerValidator(datePicker, Validator.createEmptyValidator("Dato mangler", Severity.WARNING));
        vd.registerValidator(capacityField, Validator.createRegexValidator("Kapasiteten må være et tall lavere enn 20", "^$|[0-9]+", Severity.ERROR));

        vd.registerValidator(toTimeField,
                Validator.combine(
                        Validator.createEmptyValidator("Sluttidspunkt mangler", Severity.WARNING),
                        Validator.createRegexValidator("Tid må være på formen hh:mm", "^$|([0-1]?[0-9]|2[0-3]):[0-5][0-9]", Severity.ERROR),
                        Validator.createPredicateValidator(new Predicate<String>() {
                            @Override
                            public boolean test(String o) {
                                return isTimeValid(fromTimeField.getText(), toTimeField.getText());
                            }
                        }, "Sluttidspunkt må være etter starttidspunkt", Severity.ERROR)));

        vd.registerValidator(toTimeField,
                Validator.combine(
                        Validator.createEmptyValidator("Sluttidspunkt mangler", Severity.WARNING),
                        Validator.createRegexValidator("Tid må være på formen hh:mm", "^$|([0-1]?[0-9]|2[0-3]):[0-5][0-9]", Severity.ERROR),
                        Validator.createPredicateValidator(new Predicate<String>() {
                            @Override
                            public boolean test(String o) {
                                return isTimeValid(fromTimeField.getText(), toTimeField.getText());
                            }
                        }, "Sluttidspunkt må være etter starttidspunkt", Severity.ERROR)));

        vd.registerValidator(fromTimeField,
                Validator.combine(
                        Validator.createEmptyValidator("Starttidspunkt mangler", Severity.WARNING),
                        Validator.createRegexValidator("Tid må være på formen hh:mm", "^$|([0-1]?[0-9]|2[0-3]):[0-5][0-9]", Severity.ERROR)));



        vd.setValidationDecorator(new ValidationDecoration());
    }
    }




