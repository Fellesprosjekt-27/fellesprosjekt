package com.gruppe27.fellesprosjekt.client.controllers;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.gruppe27.fellesprosjekt.client.CalendarApplication;
import com.gruppe27.fellesprosjekt.client.CalendarClient;
import com.gruppe27.fellesprosjekt.common.Event;
import com.gruppe27.fellesprosjekt.common.User;
import com.gruppe27.fellesprosjekt.common.messages.EventMessage;
import com.gruppe27.fellesprosjekt.common.messages.UserMessage;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalTime;
import java.util.*;

public class CreateEventController implements Initializable {
	
	@FXML
	TextField emne;

	@FXML 
	DatePicker dato;

	@FXML
	TextField fraTid;
	
	@FXML
	TextField tilTid;
	
	@FXML
	ListView<String> participantsListView;
	
	@FXML
	ParticipantComboBox participantComboBox;
	
	@FXML
	Button addParticipantButton;
	
	@FXML
	ChoiceBox<User> romValg;
	
	@FXML
	Button removeParticipants;
	
	@FXML
	Button createEventButton;
	
	@FXML
	Button cancelButton;

    private CalendarApplication application;
    
    private Map<String, User> allUsers;
    private ObservableList<String> availableUsersObservable;

    private HashSet<User> participants;
    
    public void setApp(CalendarApplication application) {
        this.application = application;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        participants = new LinkedHashSet<>();
        getAllUsers();
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
    
    private void setAllUsers(HashSet<User> allUsers) {
        this.allUsers = new HashMap<String, User>();
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
    private void handleAddParticipant(){
        String username = participantComboBox.getValue();
        participants.add(fromStringtoUser(username));
        updateListView();

        availableUsersObservable.remove(participantComboBox.getValue());
        participantComboBox.setValue(null);
        participantComboBox.setItems(availableUsersObservable);
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
    private void handleRemoveParticipant() {
        String username = participantsListView.getSelectionModel().getSelectedItem();
        participants.remove(fromStringtoUser(username));
        updateListView();
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
        event.setAllParticipants(participants);

        EventMessage message = new EventMessage(EventMessage.Command.CREATE_EVENT, event);
        //TODO: add functions backend to invite all users from the eventmessage
        //TODO: make an invite message
        CalendarClient.getInstance().sendMessage(message);
    }
	
	@FXML private void handleCancelAction() {}
	
	private User fromStringtoUser(String username) {
        return allUsers.get(username);
	}
	
}
