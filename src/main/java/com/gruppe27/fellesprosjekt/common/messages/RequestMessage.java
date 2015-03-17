package com.gruppe27.fellesprosjekt.common.messages;


import java.time.LocalDate;
import java.time.LocalTime;

public class RequestMessage {

    Command command;
    LocalDate date;
    LocalTime startTime;
    LocalTime endTime;
    int capacity;
    int event_id;
    public RequestMessage() {
    }

    public RequestMessage(Command command, LocalDate date, LocalTime startTime, LocalTime endTime, int capacity) {
        this.command = command;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;

    }
    
    public RequestMessage(Command command, int event_id){
        this.command = command;
        this.event_id = event_id;
    }

    public Command getCommand() {
        return command;
    }
    
    public int getEventId(){
        return event_id;
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

    public enum Command {
        ROOM_REQUEST,
        USER_REQUEST,
        INVITED_USERS_REQUEST
    }

}
