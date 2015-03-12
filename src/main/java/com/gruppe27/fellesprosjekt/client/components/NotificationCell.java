package com.gruppe27.fellesprosjekt.client.components;

import com.gruppe27.fellesprosjekt.common.Notification;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.time.format.DateTimeFormatter;

public class NotificationCell extends ListCell<Notification> {
    // TODO: move to fxml
    Shape invitationIcon = new Circle(5.0, Paint.valueOf("lightgreen"));
    Shape declineIcon = new Circle(5.0, Paint.valueOf("red"));
    Shape changedIcon = new Circle(5.0, Paint.valueOf("blue"));
    Shape conflictingIcon = new Circle(5.0, Paint.valueOf("black"));

    public NotificationCell() {
        super();
    }

    @Override
    protected void updateItem(Notification item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null) {
            String text = item.getMessage() + "\n" +
                    item.getTimestamp().format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));
            setText(text);

            switch (item.getType()) {
                case INVITATION:
                    setGraphic(invitationIcon);
                    break;
                case PARTICIPATION_DECLINED:
                    setGraphic(declineIcon);
                    break;
                case EVENT_CHANGED:
                    setGraphic(changedIcon);
                    break;
                case CONFLICTING_EVENTS:
                    setGraphic(conflictingIcon);
                    break;
            }
        } else {
            setGraphic(null);
            setText("");
        }
    }
}
