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
    private final static Font labelFont = new Font("Helvetica", 12);
    private ArrayList<Event> events;
    private ArrayList<Event> conflictingEvents;
    private ArrayList<MonthEventSquare> squares;
    private LocalDate date;
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
        squares = new ArrayList<>();

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
        Color colour = Color.DODGERBLUE;
        conflictingEvents = new ArrayList<>();
        for (int i = 0; i < events.size(); i++) {
            if (isConflicting(events.get(i), event)) {
                conflictingEvents.add(events.get(i));
                squares.get(i).addConflictingEvent(event);
                squares.get(i).getTitle().setFill(Color.RED);
                colour = Color.RED;
            }
        }
        MonthEventSquare square = new MonthEventSquare(event, conflictingEvents);
        eventBox.getChildren().add(square);
        square.getTitle().setFill(colour);
        this.events.add(event);
        squares.add(square);
    }

    private boolean isConflicting(Event event, Event event1) {
        if (event.getStartTime().isBefore(event1.getEndTime()) && event.getStartTime().isAfter(event1.getStartTime())) {
            return true;
        }
        if (event.getEndTime().isAfter(event1.getStartTime()) && event.getEndTime().isBefore(event1.getEndTime())) {
            return true;
        }
        if (event.getStartTime().equals(event1.getStartTime()) || event.getEndTime().equals(event1.getEndTime())) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "CalendarSquare{" +
                "date=" + date +
                ", events=" + events +
                '}';
    }
}
