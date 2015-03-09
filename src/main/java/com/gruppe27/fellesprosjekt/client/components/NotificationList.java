package com.gruppe27.fellesprosjekt.client.components;

import com.gruppe27.fellesprosjekt.common.Notification;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;

import java.util.ArrayList;

public class NotificationList extends ListView {
    private ArrayList<Notification> notifications;


    public NotificationList() {
        notifications = new ArrayList<>();

        this.setCellFactory(param -> new NotificationCell());
    }

    public void setNotifications(ArrayList<Notification> notifications) {
        this.setItems(FXCollections.observableArrayList(notifications));
    }

    public void addNotification(Notification n){
        this.getItems().add(n);
    }

}


