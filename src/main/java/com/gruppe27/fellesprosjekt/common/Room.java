package com.gruppe27.fellesprosjekt.common;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Room {

    private String roomNo;

    private int capacity;

    private ArrayList<Event> events;
    
    public Room (String roomNo, int capacity) {
        this.roomNo = roomNo;
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public void addEvent(Event event) {
        if (!events.contains(event)) {
            events.add(event);
            event.setRoom(this);
        }
    }

    public void removeEvent(Event event) {
        this.events.remove(event);
    }

    public boolean isFree(LocalDate date, LocalTime start, LocalTime end) {
        for(Event event : events) {
            if(event.getDate() == date) {
                if(timeCrash(event, start) || timeCrash(event, end)) {
                    return false;
                }
            }

        }
        return true;
    }
    private boolean timeCrash(Event event, LocalTime time) {
        return (event.getStartTime().isBefore(time) && event.getEndTime().isAfter(time));
    }

    public boolean hasCapacityFor(int capacity) {
        return this.capacity >= capacity;
    }
}
