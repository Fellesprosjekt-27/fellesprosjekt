package com.gruppe27.fellesprosjekt.client.controllers;

import com.gruppe27.fellesprosjekt.client.CalendarApplication;
import com.gruppe27.fellesprosjekt.client.components.CalendarComponent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class CalendarController implements Initializable {
    private CalendarApplication application;

    @FXML
    private CalendarComponent calendar;

    public CalendarController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        calendar.setCurrentPeriod(2015, 1);
    }

    public void setApp(CalendarApplication application) {
        this.application = application;
    }
}
