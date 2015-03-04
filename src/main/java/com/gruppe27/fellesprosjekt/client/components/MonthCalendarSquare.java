package com.gruppe27.fellesprosjekt.client.components;

import com.gruppe27.fellesprosjekt.common.Event;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

public class MonthCalendarSquare extends Pane {
    private ArrayList<Event> events;
    private LocalDate date;

    private final static Font labelFont = new Font("Helvetica", 12);

    private VBox eventBox;

    public MonthCalendarSquare(LocalDate date, int month) {
        Text dayLabel = new Text(String.valueOf(date.getDayOfMonth()));
        if (date.getMonth().getValue() != month) {
            dayLabel.setFill(Color.GREY);
        }

        dayLabel.setFont(labelFont);
        if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            dayLabel.setFill(Color.valueOf("#fa403f"));
        }

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

    public LocalDate getDate() {
        return date;
    }

    public void addEvent(Event event) {
        this.events.add(event);
        MonthEventSquare square = new MonthEventSquare(event);
        eventBox.getChildren().add(square);
    }

    @Override
    public String toString() {
        return "CalendarSquare{" +
                "date=" + date +
                ", events=" + events +
                '}';
    }
}
