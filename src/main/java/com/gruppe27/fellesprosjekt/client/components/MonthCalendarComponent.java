package com.gruppe27.fellesprosjekt.client.components;

import com.gruppe27.fellesprosjekt.client.controllers.CalendarController;
import com.gruppe27.fellesprosjekt.common.Event;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class MonthCalendarComponent extends BorderPane {
    private ArrayList<MonthCalendarSquare> calendarSquares;
    private ArrayList<RowConstraints> squareRowConstraints;
    private ObservableList<Event> events;

    private static final Font headerFont = Font.font("Helvetica", 26);
    private static final Font controlFont = Font.font("Helvetica", 36);

    private CalendarController controller;

    private GridPane calendarGrid;
    private Pane header;

    private Text currentMonth;
    private Text currentYear;

    private int year;
    private int month;

    public MonthCalendarComponent() {
        calendarGrid = new GridPane();
        calendarSquares = new ArrayList<>();
        squareRowConstraints = new ArrayList<>();

        events = FXCollections.observableArrayList();
        events.addListener(this::eventsUpdated);

        header = new Pane();
        header.setPadding(new Insets(0, 0, 30, 0));

        this.addPeriodInfo();
        this.setTop(header);
        this.setCenter(calendarGrid);

        this.setPadding(new Insets(20));

        RowConstraints dayDescriptors = new RowConstraints();
        dayDescriptors.setPrefHeight(30);

        calendarGrid.getRowConstraints().add(dayDescriptors);

        for (int i = 0; i < 7; i++) {
            Pane pane = new Pane();
            pane.getStyleClass().add("label");

            String day = DayOfWeek.of(i + 1).getDisplayName(TextStyle.SHORT_STANDALONE, new Locale("no"));
            Text dayLabel = new Text(day);
            dayLabel.setFill(Color.valueOf("#e67e22"));

            dayLabel.setFont(Font.font("Helvetica", 18));
            dayLabel.setLayoutX(130);
            dayLabel.setLayoutY(15);

            pane.getChildren().add(dayLabel);
            calendarGrid.add(pane, i, 0);

            ColumnConstraints c = new ColumnConstraints();
            c.setMinWidth(130);
            c.setHgrow(Priority.ALWAYS);
            calendarGrid.getColumnConstraints().add(c);
        }

        this.year = LocalDate.now().getYear();
        this.month = LocalDate.now().getMonthValue();
        drawCurrentPeriod();
    }

    public void setController(CalendarController controller) {
        this.controller = controller;
    }

    private void adjustPeriod(LocalDate date) {
        this.year = date.getYear();
        this.month = date.getMonthValue();
        this.drawCurrentPeriod();

        LocalDate to = date.withDayOfMonth(date.lengthOfMonth());
        events.clear();
        this.controller.getEventsForPeriod(date, to, events);
    }

    private void decrementMonth() {
        LocalDate date = LocalDate.of(this.year, this.month, 1).minusMonths(1);
        adjustPeriod(date);
    }

    private void incrementMonth() {
        LocalDate date = LocalDate.of(this.year, this.month, 1).plusMonths(1);
        adjustPeriod(date);
    }

    private void addPeriodInfo() {
        HBox periodInfo = new HBox(10);
        periodInfo.setPadding(new Insets(5, 20, 0, 0));

        HBox monthControls = new HBox();
        currentMonth = new Text();
        currentMonth.setFill(Color.DODGERBLUE);
        currentYear = new Text();
        periodInfo.getChildren().addAll(currentMonth, currentYear);

        Text previousMonth = new Text("◀");
        Text nextMonth = new Text("▶");

        previousMonth.setOnMouseClicked((MouseEvent event) -> decrementMonth());
        nextMonth.setOnMouseClicked((MouseEvent event) -> incrementMonth());

        monthControls.setLayoutX(1000);
        monthControls.getChildren().addAll(previousMonth, nextMonth);

        previousMonth.getStyleClass().add("change-month");
        nextMonth.getStyleClass().add("change-month");
        previousMonth.setFont(controlFont);
        nextMonth.setFont(controlFont);

        currentYear.setFill(Paint.valueOf("#bbb"));
        currentMonth.setFont(headerFont);
        currentYear.setFont(headerFont);

        header.getChildren().addAll(periodInfo, monthControls);
    }

    private MonthCalendarSquare findCalendarSquare(LocalDate date) {
        for (MonthCalendarSquare square : calendarSquares) {
            if (square.getDate().equals(date)) return square;
        }

        throw new Error("Month not propertly initialized for date " + date);
    }

    private void addEvents(ArrayList<Event> events) {
        events.forEach((event) ->
            Platform.runLater(() ->
                findCalendarSquare(event.getDate()).addEvent(event)
            )
        );
    }

    private void eventsUpdated(ListChangeListener.Change<? extends Event> change) {
        ArrayList<Event> newEvents = new ArrayList<>(change.getList());
        addEvents(newEvents);
    }

    public void drawCurrentPeriod() {
        ArrayList<LocalDate> days = getCalendarDays(this.year, this.month);
        calendarGrid.getChildren().removeAll(calendarSquares);
        calendarGrid.getRowConstraints().removeAll(squareRowConstraints);
        calendarSquares = new ArrayList<>();

        for (int i = 0; i < days.size() / 7; i++) {
            RowConstraints r = new RowConstraints();
            r.setMinHeight(100);
            r.setVgrow(Priority.ALWAYS);
            squareRowConstraints.add(r);
            calendarGrid.getRowConstraints().add(r);

            for (int j = 0; j < 7; j++) {
                MonthCalendarSquare square = new MonthCalendarSquare(days.get(j+i*7), this.month);

                if (j == 0) {
                    square.getStyleClass().add("first-in-row");
                }

                calendarSquares.add(square);
                calendarGrid.add(square, j, i + 1);
            }
        }
    }

    private ArrayList<LocalDate> getCalendarDays(int year, int month) {
        String currentMonthLabel = Month.of(month).getDisplayName(TextStyle.FULL_STANDALONE, new Locale("no"));
        currentMonth.setText(currentMonthLabel);
        currentYear.setText(String.valueOf(year));

        ArrayList<LocalDate> days = new ArrayList<>();

        LocalDate first = LocalDate.of(year, month, 1);

        int firstDay = first.getDayOfWeek().getValue();

        for (int i = firstDay - 1; i > 0; i--) {
            LocalDate day = first.minusDays(i);
            days.add(day);
        }


        for (int i = 1; i <= first.lengthOfMonth(); i++) {
            LocalDate day = LocalDate.of(year, month, i);
            days.add(day);
        }

        LocalDate lastDay = days.get(days.size() - 1);
        for (int i = 1; days.size() % 7 != 0; i++) {
            LocalDate day = lastDay.plusDays(i);
            days.add(day);
        }

        return days;
    }
}
