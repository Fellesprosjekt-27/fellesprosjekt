package com.gruppe27.fellesprosjekt.client.components;

import com.gruppe27.fellesprosjekt.client.events.EventBoxClicked;
import com.gruppe27.fellesprosjekt.common.Event;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class MonthEventSquare extends Pane {
    private static final Font font = new Font("Helvetica", 14);
    private final ArrayList<Event> conflictingEvents;

    public MonthEventSquare(Event event, ArrayList<Event> conflictingEvents) {
        this.conflictingEvents = conflictingEvents;

        Text title = new Text(event.getName());
        title.setFont(font);
        title.setFill(Color.DODGERBLUE);
        this.getChildren().add(title);

        this.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            e.consume();
            this.fireEvent(new EventBoxClicked(event, e.getScreenX(), e.getScreenY(), conflictingEvents));
        });
    }

    public void addConflictingEvent(Event conflictingEvent) {
        conflictingEvents.add(conflictingEvent);
    }
}
