package com.gruppe27.fellesprosjekt.common;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;

public class Event {

    public enum Status {
        ATTENDING,
        NOT_ATTENDING,
        MAYBE
    }


    int id;

    LocalDate date;
    LocalTime startTime;
    LocalTime endTime;

    User creator;
    Status status;

    HashSet<User> userParticipants;
    HashSet<Group> groupParticipants;
    String name;
    Room room;

    public Event() {
        this.creator = null;
        userParticipants = new HashSet<>();
        groupParticipants = new HashSet<>();
    }

    public Event(String name, User creator, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.name = name;
        this.creator = creator;
        setDate(date);
        setStartTime(startTime);
        setEndTime(endTime);
    }

    public void addParticipant(User participant) {
        userParticipants.add(participant);
    }

    public void addGroupParticipant(Group participant) {
        groupParticipants.add(participant);
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

    public String getName() {
        return name;
    }

    public User getCreator() {
        return creator;
    }

    public void setName(String s) {
        this.name = s;


    }

    public void setCreator(User user) {
        this.creator = user;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String toString() {
        return "Name: " + getName() + ".";
    }

    public void setAllParticipants(HashSet<User> participants) {
        userParticipants.addAll(participants);
    }

    public Room getRoom() {
        return room;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}
