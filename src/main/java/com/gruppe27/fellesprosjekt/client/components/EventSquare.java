package com.gruppe27.fellesprosjekt.client.components;

import com.gruppe27.fellesprosjekt.common.Event;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class EventSquare extends Pane {
    private Text title;
    private static final Font font = new Font("Helvetica", 14);

    public EventSquare(Event event) {
        title = new Text(event.getName());
        title.setFont(font);
        title.setFill(Color.DODGERBLUE);
        this.getChildren().add(title);
    }
}
