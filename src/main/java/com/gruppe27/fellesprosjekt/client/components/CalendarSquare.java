package com.gruppe27.fellesprosjekt.client.components;

import com.gruppe27.fellesprosjekt.common.Event;
import com.gruppe27.fellesprosjekt.common.User;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

public class CalendarSquare extends Pane {
    private ArrayList<Event> events;
    private LocalDate date;

    private final static Font labelFont = new Font("Helvetica", 12);

    private Text dayLabel;
    private VBox eventBox;

    public CalendarSquare(LocalDate date, int month) {
        dayLabel = new Text(String.valueOf(date.getDayOfMonth()));
        if (date.getMonth().getValue() != month) {
            dayLabel.setFill(Color.GREY);
        }

        dayLabel.setFont(labelFont);
        dayLabel.setLayoutX(130);
        dayLabel.setLayoutY(15);

        this.events = new ArrayList<>();

        eventBox = new VBox();
        eventBox.setLayoutX(5);
        eventBox.setLayoutY(30);
        eventBox.setSpacing(5);

        this.getChildren().addAll(dayLabel, eventBox);
        this.date = date;

        // DEBUG STUFF
        Event test1 = new Event("hei", new User("lel", "lel"), date, LocalTime.of(10, 30), LocalTime.of(12, 0));
        Event test2 = new Event("hei2", new User("lel", "lel"), date, LocalTime.of(10, 30), LocalTime.of(12, 0));
        Event test3 = new Event("hei3", new User("lel", "lel"), date, LocalTime.of(10, 30), LocalTime.of(12, 0));
        addEvents(test1, test2, test3);
    }

    public void addEvents(Event... events) {
        ArrayList<Event> newEvents = new ArrayList<>(Arrays.asList(events));
        this.events.addAll(newEvents);

        newEvents.forEach((event) -> {
            EventSquare square = new EventSquare(event);
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
