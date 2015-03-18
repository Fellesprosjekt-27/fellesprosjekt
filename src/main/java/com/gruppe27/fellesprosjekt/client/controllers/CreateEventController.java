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
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;


public class CreateEventController implements Initializable {

    @FXML
    Text titleText;

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
    Button removeParticipantButton;

    @FXML
    ChoiceBox<String> roomChoiceBox;

    @FXML
    TextField capacityField;

    @FXML
    Button createEventButton;

    @FXML
    Button cancelButton;

    private HashMap<String, Room> availableRooms;

    private CalendarApplication application;

    private Map<String, ParticipantUser> allUsers;
    private ObservableList<SortableText> availableUsersObservable;

    private ObservableList<String> availableRoomsObservable;

    private ValidationSupport vd = new ValidationSupport();
    private Event currentEvent;

    public CreateEventController() {
        availableUsersObservable = FXCollections.observableArrayList();
        currentEvent = new Event();
        availableRooms = new HashMap<>();

        currentEvent.setId(-1);
    }

    public void emptyRoomsArray() {
        this.availableRooms = new HashMap<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        enableStates();
        addListeners();

        registerValidators();
    }


    public boolean isTimeValid() {
            LocalTime from = toLocalTime(fromTimeField.getText());
            LocalTime to = toLocalTime(toTimeField.getText());

            return from != null && to != null && from.compareTo(to) < 0;
    }

    public void setApp(CalendarApplication application) {
        this.application = application;
    }

    @FXML
    private void handleChoiceboxClicked() {
        LocalDate date = datePicker.getValue();
        LocalTime start = toLocalTime(fromTimeField.getText());
        LocalTime end = toLocalTime(toTimeField.getText());
        int capacity = Integer.parseInt(capacityField.getText());
        this.updateCurrentRooms(date, start, end, capacity);
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

        availableRoomsObservable = FXCollections.observableArrayList();

        for(Room r: rooms){
            this.availableRooms.put(r.toString(), r);
            availableRoomsObservable.add(r.toString());
        }
        if(currentEvent.getRoom() != null) {
            availableRoomsObservable.add(currentEvent.getRoom().toString() + " (opprinnelig rom.)");
        }
        Platform.runLater(() -> {
            roomChoiceBox.setItems(availableRoomsObservable);
        });
    }

    @FXML
    private void handleComboBoxClicked() {
        getAllUsers(-1);

    }

    private LocalTime toLocalTime(String time) {
        boolean isValid = time.matches("([0-1]?[0-9]|2[0-3]):[0-5][0-9]");
        if(isValid){
            // LocalTime.parse requires exactly 2 digit hours.
            if (time.length() < 5) {
                time = "0" + time;
            }
            return LocalTime.parse(time);
        } else {
            return null;
        }
    }

    private void getAllUsers(int eventId) {
        LocalDate date = datePicker.getValue();
        LocalTime start = toLocalTime(fromTimeField.getText());
        LocalTime end = toLocalTime(toTimeField.getText());

        // If time is not yet set: bail.
        // TODO: Visualize this with a validation.
        if (start == null || end == null) return;

        RequestMessage message = new RequestMessage(RequestMessage.Command.USER_REQUEST, date, start, end, -1);

        CalendarClient client = CalendarClient.getInstance();

        Listener getUsersListener = new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof ParticipantUserMessage) {
                    ParticipantUserMessage complete = (ParticipantUserMessage) object;
                    switch (complete.getCommand()) {
                        case RECEIVE_ALL:
                            setAllUsers(complete.getParticipantUsers(), eventId);
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

    private void setAllUsers(HashSet<ParticipantUser> allUsers, int eventId) {
        this.allUsers = new HashMap<>();
        availableUsersObservable = FXCollections.observableArrayList();

        for (ParticipantUser participantUser : allUsers) {
            if(participantUser.getUsername().equals(application.getUser().getUsername()))
                continue;

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
        removeListViewParticipants();


        Platform.runLater(() -> {
            participantComboBox.init(availableUsersObservable);
        });

        if (eventId > 0) {
            getInvited(eventId);
        }
    }

    private void removeListViewParticipants() {
        for (String string : participantsListView.getItems()) {
            removeUserFromObservable(string.split(":")[0]);
        }
    }


    @FXML
    private void handleAddParticipant() {
        addParticipant(participantComboBox.getValue().getText());
    }

    private void addParticipant(String username) {
        participantComboBox.setValue(null);
        removeUserFromObservable(username);
        Platform.runLater(() -> {
            participantComboBox.init(availableUsersObservable);
            participantsListView.getItems().add(username);
        });
    }

    private boolean removeUserFromObservable(String username) {
        SortableText removeText = null;
        for(SortableText text: availableUsersObservable) {
            if(text.getText().equals(username)) {
                removeText = text;
                break;
            }
        }
        if(removeText != null) {
            availableUsersObservable.remove(removeText);
            return true;
        } else {
            return false;
        }
    }
    
    @FXML
    private void handleRemoveParticipant() {
        if (participantsListView.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        String removeString = participantsListView.getSelectionModel().getSelectedItem();
        String username = removeString.split(":")[0];
        participantsListView.getSelectionModel().select(null);
        participantsListView.getItems().remove(removeString);

        SortableText text = new SortableText(username);
        System.out.println("allusers:" + allUsers);
        if (allUsers.get(username).isBusy()) {
            text.setFill(Color.RED);
        } else {
            text.setFill(Color.GREEN);
        }
        availableUsersObservable.add(text);
        Collections.sort(availableUsersObservable);
        participantComboBox.init(availableUsersObservable);
    }

    @FXML
    private void handleCreateEventAction() {

        Event event = getCurrentEvent();
        event.setName(emne.getText());

        event.setDate(datePicker.getValue());

        LocalTime startTime = toLocalTime(fromTimeField.getText());
        LocalTime endTime = toLocalTime(toTimeField.getText());
        event.setCreator(application.getUser());
        event.setStartTime(startTime);
        event.setEndTime(endTime);
        HashSet<User> participants = getListViewParticipants();
        participants.add(event.getCreator());
        event.setAllParticipants(participants);
        if(capacityField.getText().isEmpty()) {
            event.setCapacityNeed(Integer.parseInt(capacityField.getText()));
        } else {
            event.setCapacityNeed(0);
        }
        event.setRoom(availableRooms.get(roomChoiceBox.getValue()));

        EventMessage message = new EventMessage(EventMessage.Command.CREATE_EVENT, event);
        CalendarClient.getInstance().sendMessage(message);
        application.gotoCalendar();
    }
    
    private HashSet<User> getListViewParticipants(){
        HashSet<User> participants = new HashSet<>();
         for (String listItem : participantsListView.getItems()) {
             String username = listItem.split(":")[0];
             participants.add(allUsers.get(username));
        }
        return participants;
    }

    @FXML
    private void handleCancelAction() {
        application.gotoCalendar();
    }

    private boolean isTimeDateSet(){
        return datePicker.getValue() != null && isTimeValid();
    }

    private boolean canCreateEvent() {
        return !emne.getText().isEmpty() && isTimeDateSet();
    }

    private boolean canPickRoom() {
        return isTimeDateSet() && capacityField.getText().matches("[\\d]+");
    }

    private boolean canAddParticipant(){
        return isTimeDateSet() &&  participantComboBox.getItems().contains(participantComboBox.getValue());
    }

    private void enableStates(){
        createEventButton.setDisable(!canCreateEvent());

        roomChoiceBox.setDisable(!canPickRoom());

        participantComboBox.setDisable(!isTimeDateSet());

        addParticipantButton.setDisable(!canAddParticipant());
    }

    private void addListeners() {
        emne.textProperty().addListener((observable, oldValue, newValue) -> {
            createEventButton.setDisable(!canCreateEvent());

        });

        ChangeListener enableActionListener = (observable, oldValue, newValue) -> {
           enableStates();
        };

        ChangeListener clearRoomChoice = (observable, oldValue, newValue) -> {
            roomChoiceBox.setValue(null);
        };

        datePicker.valueProperty().addListener(enableActionListener);
        datePicker.valueProperty().addListener(clearRoomChoice);

        fromTimeField.textProperty().addListener(enableActionListener);
        fromTimeField.textProperty().addListener(clearRoomChoice);

        toTimeField.textProperty().addListener(enableActionListener);
        toTimeField.textProperty().addListener(clearRoomChoice);

        capacityField.textProperty().addListener(enableActionListener);
        capacityField.textProperty().addListener(clearRoomChoice);

        participantComboBox.getEditor().textProperty().addListener(enableActionListener);

    }

    private void registerValidators() {
        vd.registerValidator(emne, Validator.createEmptyValidator("Tittel mangler", Severity.WARNING));
        vd.registerValidator(datePicker, Validator.createEmptyValidator("Dato mangler", Severity.WARNING));
        vd.registerValidator(capacityField, Validator.createRegexValidator("Kapasiteten må være et tall", "[0-9]*",
                Severity.ERROR));

        vd.registerValidator(toTimeField,
                Validator.combine(
                        Validator.createEmptyValidator("Sluttidspunkt mangler", Severity.WARNING),
                        Validator.createRegexValidator("Tid må være på formen hh:mm",
                                "^$|([0-1]?[0-9]|2[0-3]):[0-5][0-9]", Severity.ERROR),
                        Validator.createPredicateValidator(o -> isTimeValid(),
                                "Sluttidspunkt må være etter starttidspunkt", Severity.ERROR)));


        vd.registerValidator(fromTimeField,
                Validator.combine(
                        Validator.createEmptyValidator("Starttidspunkt mangler", Severity.WARNING),
                        Validator.createRegexValidator("Tid må være på formen hh:mm",
                                "^$|([0-1]?[0-9]|2[0-3]):[0-5][0-9]", Severity.ERROR)));

        vd.setValidationDecorator(new ValidationDecoration());
    }

    public void editEvent(Event event) {
        setCurrentEvent(event);

        titleText.setText("ENDRE AVTALE");
        createEventButton.setText("ENDRE AVTALE");
        emne.setText(event.getName());

        datePicker.setValue(event.getDate());
        fromTimeField.setText(event.getStartTime().toString());
        toTimeField.setText(event.getEndTime().toString());
        capacityField.setText(Integer.toString(event.getCapacityNeed()));

        handleChoiceboxClicked();
        roomChoiceBox.getItems().add(event.getRoom().toString());
        getAllUsers(event.getId());
    }

    private void getInvited(int id) {
        RequestMessage message = new RequestMessage(RequestMessage.Command.INVITED_USERS_REQUEST, id);

        CalendarClient client = CalendarClient.getInstance();

        Listener getInvitedListener = new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof ParticipantUserMessage) {
                    ParticipantUserMessage complete = (ParticipantUserMessage) object;
                    switch (complete.getCommand()) {
                        case RECEIVE_ALL:
                            setInvited(complete.getParticipantUsers());
                            break;
                        case SEND_ALL:
                            break;
                    }
                    client.removeListener(this);
                }
            }
        };
        client.addListener(getInvitedListener);
        client.sendMessage(message);
    }

    private void setInvited(HashSet<ParticipantUser> participantUsers) {
        for (ParticipantUser participantUser : participantUsers) {
            addParticipantWithStatus(participantUser);
        }

    }

    private void addParticipantWithStatus(ParticipantUser participantUser) {
        participantComboBox.setValue(null);
        removeUserFromObservable(participantUser.getUsername());
        Platform.runLater(() -> {
            participantComboBox.init(availableUsersObservable);
            participantsListView.getItems().add(participantUser.getUsername() + ": " + participantUser.getParticipantStatus().toString());
        });
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
    }
    public Event getCurrentEvent() {
        return this.currentEvent;
    }
}
