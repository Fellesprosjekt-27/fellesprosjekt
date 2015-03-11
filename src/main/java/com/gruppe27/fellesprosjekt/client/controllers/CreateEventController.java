package com.gruppe27.fellesprosjekt.client.controllers;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.gruppe27.fellesprosjekt.client.CalendarApplication;
import com.gruppe27.fellesprosjekt.client.CalendarClient;
import com.gruppe27.fellesprosjekt.client.SortableText;
import com.gruppe27.fellesprosjekt.common.Event;
import com.gruppe27.fellesprosjekt.common.ParticipantUser;
import com.gruppe27.fellesprosjekt.common.Room;
import com.gruppe27.fellesprosjekt.common.User;
import com.gruppe27.fellesprosjekt.common.messages.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;


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
        //getAllUsers();
        roomChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                currentRoom = roomsArray.get(newValue.intValue());
            }
        });

    }

    public void setApp(CalendarApplication application) {
        this.application = application;
    }

    @FXML
    private void handleChoiceboxClicked() {
        System.out.println("Cbox clicked.");
        LocalDate date = datePicker.getValue();
        LocalTime start = LocalTime.parse(fromTimeField.getText());
        LocalTime end = LocalTime.parse(toTimeField.getText());
        int capacity = Integer.parseInt(capacityField.getText());
        this.updateCurrentRooms(date,start,end,capacity);
        //TODO needs time to update rooms before I can do something.

    }
    @FXML
    private void updateCurrentRooms(LocalDate date, LocalTime start, LocalTime end, int capacity) {
        RequestMessage message = new RequestMessage(RequestMessage.Command.ROOM_REQUEST, date,start,end,capacity);

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
        for(Room room: roomsArray) {
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
        if(allUsers == null) {
            getAllUsers();
        }
        //TODO Validering
    }

    private void getAllUsers() {
        System.out.println("Getting all users");
        LocalDate date = datePicker.getValue();
        LocalTime start = LocalTime.parse(fromTimeField.getText());
        LocalTime end = LocalTime.parse(toTimeField.getText());

        RequestMessage message = new RequestMessage(RequestMessage.Command.USER_REQUEST, date, start, end, -1);

        CalendarClient client = CalendarClient.getInstance();

        Listener getUsersListener = new Listener() {
            public void received(Connection connection, Object object) {
                System.out.println("Listening for ParticipantUserMessage");
                if (object instanceof ParticipantUserMessage) {
                    System.out.println("Received participantUserMessage.");
                    ParticipantUserMessage complete = (ParticipantUserMessage) object;
                    switch (complete.getCommand()) {
                        case RECEIVE_ALL:
                            System.out.println("Got users");
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
        System.out.println("Setting all users.");
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
    private void handleAddParticipant(){
        String username = participantComboBox.getValue().getText();
        participants.add(allUsers.get(username));
        updateListView();
        availableUsersObservable.remove(participantComboBox.getValue());
        participantComboBox.setValue(null);
        participantComboBox.init(availableUsersObservable);
    }

    private void updateListView() {
        ObservableList<String> observable = FXCollections.observableArrayList();
        for (User user : participants ) {
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

}
