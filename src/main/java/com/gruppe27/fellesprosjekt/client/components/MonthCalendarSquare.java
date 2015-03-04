package com.gruppe27.fellesprosjekt.client.components;

import com.gruppe27.fellesprosjekt.common.Event;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class MonthCalendarSquare extends Pane {
    private ArrayList<Event> events;
    private LocalDate date;

    private final static Font labelFont = new Font("Helvetica", 12);

    private Text dayLabel;
    private VBox eventBox;

    public MonthCalendarSquare(LocalDate date, int month) {
        dayLabel = new Text(String.valueOf(date.getDayOfMonth()));
        if (date.getMonth().getValue() != month) {
            dayLabel.setFill(Color.GREY);
        }

        dayLabel.setFont(labelFont);
        dayLabel.setLayoutX(125);
        dayLabel.setLayoutY(15);

        this.events = new ArrayList<>();

        eventBox = new VBox();
        eventBox.setLayoutX(5);
        eventBox.setLayoutY(30);
        eventBox.setSpacing(5);

        this.getChildren().addAll(dayLabel, eventBox);
        this.date = date;
    }

    public void addEvents(Event... events) {
        ArrayList<Event> newEvents = new ArrayList<>(Arrays.asList(events));
        this.events.addAll(newEvents);

        newEvents.forEach((event) -> {
            MonthEventSquare square = new MonthEventSquare(event);
            eventBox.getChildren().add(square);
        });

    }

    @Override
    public String toString() {
        return "CalendarSquare{" +
                "date=" + date +
                ", events=" + events +
                '}';
    }
}
