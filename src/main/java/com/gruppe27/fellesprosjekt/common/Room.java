package com.gruppe27.fellesprosjekt.common;


public class Room {

    private String roomNo;

    private int capacity;


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

    public boolean hasCapacityFor(int capacity) {
        return this.capacity >= capacity;
    }
}
