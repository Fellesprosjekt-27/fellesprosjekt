package com.gruppe27.fellesprosjekt.client.controllers;

import com.gruppe27.fellesprosjekt.client.CalendarApplication;
import com.gruppe27.fellesprosjekt.client.components.MonthCalendarComponent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class CalendarController implements Initializable {
    private CalendarApplication application;

    @FXML
    private MonthCalendarComponent calendar;

    public CalendarController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setApp(CalendarApplication application) {
        this.application = application;
    }

    @FXML
    private void showMonthOverview() {
    }
}
