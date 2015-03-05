package com.gruppe27.fellesprosjekt.common.messages;

import com.gruppe27.fellesprosjekt.common.Event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;

public class RoomRequestMessage {

    public enum Command{
        ROOM_REQUEST,
    }

    Command command;

    LocalDate date;
    LocalTime startTime;
    LocalTime endTime;
    int capacity;

    public RoomRequestMessage() {

    }

    public RoomRequestMessage(Command command, LocalDate date, LocalTime startTime, LocalTime endTime, int capacity) {
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
