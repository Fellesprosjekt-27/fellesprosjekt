package com.gruppe27.fellesprosjekt.common.messages;


import java.time.LocalDate;
import java.time.LocalTime;

public class RequestMessage {

    public enum Command{
        ROOM_REQUEST,
        USER_REQUEST
    }

    Command command;

    LocalDate date;
    LocalTime startTime;
    LocalTime endTime;
    int capacity;

    public RequestMessage() {
    }

    public RequestMessage(Command command, LocalDate date, LocalTime startTime, LocalTime endTime, int capacity) {
        this.command = command;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;

    }

    public Command getCommand() {
        return command;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public LocalDate getDate() {
        return date;
    }

}
