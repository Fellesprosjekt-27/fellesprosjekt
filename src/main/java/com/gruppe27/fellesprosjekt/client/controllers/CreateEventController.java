package com.gruppe27.fellesprosjekt.client.controllers;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.gruppe27.fellesprosjekt.client.CalendarApplication;
import com.gruppe27.fellesprosjekt.client.CalendarClient;
import com.gruppe27.fellesprosjekt.client.SortableText;
import com.gruppe27.fellesprosjekt.client.components.ValidationDecoration;
import com.gruppe27.fellesprosjekt.common.*;
import com.gruppe27.fellesprosjekt.common.messages.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

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
    ComboBox teamComboBox;

    @FXML
    Button addTeamButton;

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
    private Map<Integer, Team> teams;
    private ObservableList<SortableText> availableUsersObservable;
    private ObservableList<String> availableTeamsObservable;

    private ObservableList<String> availableRoomsObservable;

    private ArrayList<String> membersAdded;

    private ValidationSupport vd = new ValidationSupport();


    public void emptyRoomsArray() {
        this.availableRooms = new HashMap<>();
    }

    public CreateEventController() {
        availableRooms = new HashMap<>();
        membersAdded = new ArrayList<>();
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

        availableRoomsObservable = FXCollections.observableArrayList();

        for(Room r: rooms){
            this.availableRooms.put(r.toString(), r);
            availableRoomsObservable.add(r.toString());
        }

        Platform.runLater(() -> {
        roomChoiceBox.setItems(availableRoomsObservable);
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

    @FXML
    private void handleTeamsClicked() {
        if (teams == null) {
            getAllTeams();
        }
        if (allUsers == null) {
            getAllUsers();
        }
    }

    LocalTime toLocalTime(String time) {
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

    private void getAllUsers() {
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
                            //TODO The HashSet returned contains a duplicate of user 'a', something wrong with the request.
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

    private void getAllTeams() {
        TeamMessage message = new TeamMessage(TeamMessage.Command.SEND_TEAMS);
        CalendarClient client = CalendarClient.getInstance();

        Listener getTeamsListener = new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof TeamMessage) {
                    TeamMessage complete = (TeamMessage) object;
                    switch (complete.getCommand()) {
                        case RECEIVE_TEAMS:
                            setAllTeams(complete.getTeams());
                            break;
                        case SEND_TEAMS:
                            break;
                    }
                    client.removeListener(this);
                }
            }

        };
        client.addListener(getTeamsListener);
        client.sendMessage(message);
    }

    private void setAllUsers(HashSet<ParticipantUser> allUsers) {
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
        Platform.runLater(() -> {
            participantComboBox.init(availableUsersObservable);
        });
    }

    private void setAllTeams(HashSet<Team> teams) {
        this.teams = new HashMap<>();
        availableTeamsObservable = FXCollections.observableArrayList();
        for (Team team : teams) {
            this.teams.put(team.getNumber(), team);
            availableTeamsObservable.add(team.getNumber() + "");
        }
        Platform.runLater(() -> {
            initComboBox();
        });
    }

    private void initComboBox() {
        teamComboBox.getItems().addAll(availableTeamsObservable);
    }


    @FXML
    private void handleAddParticipant() {

        String username = participantComboBox.getValue().getText();
        if (participantsListView.getItems().indexOf(username) == -1) {
            participantsListView.getItems().add(username);
        }


        availableUsersObservable.remove(participantComboBox.getValue());
        participantComboBox.setValue(null);
//        Don't remember why this method is here.
//        participantComboBox.getItems().clear();
        participantComboBox.init(availableUsersObservable);
    }
    
    @FXML
    private void handleAddTeam() {
        Integer number = Integer.parseInt((String) teamComboBox.getValue());
        Team team = teams.get(number);
        for (User member : team.getTeamMembers()) {
            if ((participantsListView.getItems().indexOf(member.getUsername()) == -1) &&
                    !member.getUsername().equals(application.getUser().getUsername())) {
                participantsListView.getItems().add(member.getUsername());
                membersAdded.add(member.getUsername());
            }
        }
        availableTeamsObservable.remove(teamComboBox.getValue());
        teamComboBox.getItems().clear();
        teamComboBox.setValue(null);
        initComboBox();
    }

    @FXML
    private void handleRemoveParticipant() {
        String username = participantsListView.getSelectionModel().getSelectedItem();
        if (membersAdded.indexOf(username) == -1) {
            participantsListView.getSelectionModel().select(null);
            participantsListView.getItems().remove(username);

            SortableText text = new SortableText(username);
            if(allUsers.get(username).isBusy()){
                text.setFill(Color.RED);
            }else {
                text.setFill(Color.GREEN);
            }
            availableUsersObservable.add(text);
            Collections.sort(availableUsersObservable);
            participantComboBox.init(availableUsersObservable);
        } else {
            membersAdded.remove(username);
            participantsListView.getItems().remove(username);
            reAddTeams();
        }

    }

    private void reAddTeams() {
        for (Map.Entry<Integer, Team> teamEntry : teams.entrySet()) {
            boolean canAdd = true;
            for (User member : teamEntry.getValue().getTeamMembers()) {
                if (participantsListView.getItems().indexOf(member.getUsername()) != -1) {
                    canAdd = false;
                }
            }
            if (canAdd) {
                availableTeamsObservable.add(teamEntry.getKey() + "");
                initComboBox();
            }
        }
    }


    @FXML
    private void handleCreateEventAction() {

        Event event = new Event();
        event.setName(emne.getText());

        event.setDate(datePicker.getValue());

        LocalTime startTime = toLocalTime(fromTimeField.getText());
        LocalTime endTime = toLocalTime(toTimeField.getText());
        event.setCreator(application.getUser());
        event.setStartTime(startTime);
        event.setEndTime(endTime);
        HashSet<User> participants = getListViewParticipant();
        event.setAllParticipants(participants);
        event.setRoom(availableRooms.get(roomChoiceBox.getValue()));

        EventMessage message = new EventMessage(EventMessage.Command.CREATE_EVENT, event);
        CalendarClient.getInstance().sendMessage(message);

        //application.cancelCreateNewEvent();
        application.gotoCalendar();
    }
    
    private HashSet<User> getListViewParticipant(){
        HashSet<User> participants = new HashSet<>();
         for (String username : participantsListView.getItems()) {
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

    private  boolean canAddMembers() {
        return isTimeDateSet() && teamComboBox.getItems().contains(teamComboBox.getValue());
    }

    private void enableStates(){
        createEventButton.setDisable(!canCreateEvent());

        roomChoiceBox.setDisable(!canPickRoom());

        participantComboBox.setDisable(!isTimeDateSet());

        addParticipantButton.setDisable(!canAddParticipant());

        teamComboBox.setDisable(!isTimeDateSet());

        addTeamButton.setDisable(!canAddMembers());


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
        teamComboBox.getEditor().textProperty().addListener(enableActionListener);

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
}
