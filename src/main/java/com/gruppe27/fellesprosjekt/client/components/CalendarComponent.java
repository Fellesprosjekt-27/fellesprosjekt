package com.gruppe27.fellesprosjekt.client.components;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class CalendarComponent extends BorderPane {
    private ArrayList<CalendarSquare> calendarSquares;

    private static final Font headerFont = Font.font("Helvetica", 26);
    private static final Font controlFont = Font.font("Helvetica", 36);

    private GridPane calendarGrid;
    private HBox header;

    private HBox periodInfo;
    private Text currentMonth;
    private Text currentYear;

    private HBox monthControls;
    private Text previousMonth;
    private Text nextMonth;

    public CalendarComponent() {
        calendarGrid = new GridPane();
        header = new HBox();
        periodInfo = new HBox(10);
        monthControls = new HBox();
        header.setPadding(new Insets(0, 0, 30, 0));
        periodInfo.setPadding(new Insets(5, 20, 0, 0));

        currentMonth = new Text();
        currentYear = new Text();
        periodInfo.getChildren().addAll(currentMonth, currentYear);

        previousMonth = new Text("«");
        nextMonth = new Text("»");
        monthControls.getChildren().addAll(previousMonth, nextMonth);
        previousMonth.getStyleClass().add("change-month");
        nextMonth.getStyleClass().add("change-month");
        previousMonth.setFont(controlFont);
        nextMonth.setFont(controlFont);

        currentYear.setFill(Paint.valueOf("#bbb"));
        currentMonth.setFont(headerFont);
        currentYear.setFont(headerFont);

        header.getChildren().addAll(periodInfo, monthControls);

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
            dayLabel.setFont(Font.font("Helvetica", 18));
            dayLabel.setLayoutX(130);
            dayLabel.setLayoutY(15);

            pane.getChildren().add(dayLabel);
            calendarGrid.add(pane, i, 0);

            ColumnConstraints c = new ColumnConstraints();
            c.setMinWidth(160);
            c.setHgrow(Priority.ALWAYS);
            calendarGrid.getColumnConstraints().add(c);
        }

    }

    public void setCurrentPeriod(int year, int month) {
        ArrayList<LocalDate> days = getCalendarDays(year, month);
        calendarSquares = new ArrayList<>();


        for (int i = 0; i < days.size() / 7; i++) {
            RowConstraints r = new RowConstraints();
            r.setMinHeight(100);
            r.setVgrow(Priority.ALWAYS);
            calendarGrid.getRowConstraints().add(r);

            for (int j = 0; j < 7; j++) {
                CalendarSquare square = new CalendarSquare(days.get(j+i*7), month);

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

        System.out.println("hei " +  days);
        return days;
    }
}
