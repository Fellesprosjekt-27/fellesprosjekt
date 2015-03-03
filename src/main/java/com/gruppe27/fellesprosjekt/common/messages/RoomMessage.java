package com.gruppe27.fellesprosjekt.common.messages;

import com.gruppe27.fellesprosjekt.common.Room;

import java.util.HashSet;

public class RoomMessage {
    public enum Command {
        SEND_ROOMS,
        RECIEVE_ROOMS
    }

    HashSet<Room> rooms;
    Command command;

    public RoomMessage() {
    }

    public RoomMessage(Command command, HashSet<Room> rooms) {
        this.command = command;
        this.rooms = rooms;
    }

    public Command getCommand() {
        return command;
    }

    public HashSet<Room> getRooms() {
        return rooms;
    }
}

