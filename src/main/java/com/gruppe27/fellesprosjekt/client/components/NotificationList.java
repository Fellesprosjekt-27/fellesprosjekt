package com.gruppe27.fellesprosjekt.client.components;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.gruppe27.fellesprosjekt.client.CalendarClient;
import com.gruppe27.fellesprosjekt.common.Notification;
import com.gruppe27.fellesprosjekt.common.messages.NotificationMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class NotificationList extends ListView {
    private ObservableList<Notification> notifications;

    public NotificationList() {
        notifications = FXCollections.observableArrayList();
        this.setItems(notifications);
        this.setCellFactory(param -> new NotificationCell());
        this.listenForNotifications();
    }

    private void listenForNotifications() {
        NotificationMessage findNotifications = new NotificationMessage(
                NotificationMessage.Command.GETALL_NOTIFICATIONS);

        CalendarClient client = CalendarClient.getInstance();

        Listener listener = new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof NotificationMessage) {
                    NotificationMessage message = (NotificationMessage) object;

                    switch (message.getCommand()) {
                        case RECEIVE_NOTIFICATION:
                            notifications.addAll(message.getNotifications());
                            break;
                    }
                }
            }
        };

        client.addListener(listener);
        client.sendMessage(findNotifications);
    }
}


