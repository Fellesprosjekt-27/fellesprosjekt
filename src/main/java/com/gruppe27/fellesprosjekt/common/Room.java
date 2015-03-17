package com.gruppe27.fellesprosjekt.common;


public class Room {

    private String roomName;

    private int capacity;

    @Override
    public String toString() {
        return "" + roomName + ", kapasitet: " + capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public boolean hasCapacityFor(int capacity) {
        return this.capacity >= capacity;
    }
}
