package com.gruppe27.fellesprosjekt.common;


import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Event {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private User creator;

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }


    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    public double getDuration() {
        long amount = getStartTime().until(getEndTime(), ChronoUnit.MINUTES);
        return (double) amount;

    }
}
