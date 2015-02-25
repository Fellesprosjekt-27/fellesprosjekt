package com.gruppe27.fellesprosjekt.common;


import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;

public class Event {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private User creator;

    private HashSet<User> userParticipants;
    private HashSet<Group> groupParticipants;

    public Event(User creator, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.creator = creator;
        setDate(date);
        setStartTime(startTime);
        setEndTime(endTime);
    }

    public void addParticipant(User participant) {
        if(!userParticipants.contains(participant)) {
            userParticipants.add(participant);
            participant.addEvent(this);
        }
    }
    public void addGroupParticipant(Group participant) {
        if(!groupParticipants.contains(participant)) {
            groupParticipants.add(participant);
            participant.addEvent(this);
        }
    }

    public HashSet<Group> getGroupParticipants() {
        return new HashSet<Group>(groupParticipants);
    }


    public HashSet<User> getUserParticipants() {
        return new HashSet<>(userParticipants);
    }

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
