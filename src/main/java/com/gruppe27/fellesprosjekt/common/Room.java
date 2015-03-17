package com.gruppe27.fellesprosjekt.common;


public class Room {

    private String roomName;

    private int capacity;

    public Room() {

    }
    public Room(String roomName) {
        this.roomName = roomName;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomName='" + roomName + '\'' +
                ", capacity=" + capacity +
                '}';
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
