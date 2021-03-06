package com.gruppe27.fellesprosjekt.client.components;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.gruppe27.fellesprosjekt.client.CalendarClient;
import com.gruppe27.fellesprosjekt.client.controllers.CalendarController;
import com.gruppe27.fellesprosjekt.common.User;
import com.gruppe27.fellesprosjekt.common.messages.UserMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

import java.util.ArrayList;

public class ChooseCalendarComboBox extends ComboBox<User> {
    private ArrayList<User> allUsers;
    private ObservableList<User> users;
    private CalendarController controller;

    public ChooseCalendarComboBox() {
        super();
        allUsers = new ArrayList<>();

        this.setConverter(new StringConverter<User>() {
            @Override
            public String toString(User user) {
                if (user == null) return null;
                return user.getName();
            }

            @Override
            public User fromString(String name) {
                for (User user : users) {
                    if (user.getName().equals(name)) return user;
                }

                return null;
            }
        });

        this.setEditable(true);
        users = FXCollections.observableArrayList();
        this.valueProperty().addListener((observable, oldValue, newValue) -> {
            this.controller.setCurrentUser(newValue);
        });

        this.setItems(users);
        findUsers();
    }

    public void setController(CalendarController controller) {
        this.controller = controller;
    }

    public void findUsers() {
        UserMessage userMessage = new UserMessage(UserMessage.Command.SEND_ALL);
        CalendarClient client = CalendarClient.getInstance();

        Listener listener = new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof UserMessage) {
                    UserMessage message = (UserMessage) object;
                    if (message.getCommand() == UserMessage.Command.RECEIVE_ALL) {
                        users.addAll(message.getUsers());
                        allUsers.addAll(message.getUsers());
                        client.removeListener(this);
                    }
                }
            }
        };

        client.addListener(listener);
        client.sendMessage(userMessage);
    }

    public void resetUser() {
        this.setValue(null);
        this.hide();
    }

    public User getUser() {
        return this.getValue();
    }
}
