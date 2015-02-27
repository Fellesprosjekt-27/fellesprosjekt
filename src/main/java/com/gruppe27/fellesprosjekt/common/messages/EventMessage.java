package com.gruppe27.fellesprosjekt.common.messages;

import com.gruppe27.fellesprosjekt.common.Event;

public class EventMessage {
        public enum Command {
            CREATE_EVENT
        }

        Event event;
        Command command;

        public EventMessage() {}

        public EventMessage(Command command, Event event) {
            this.command = command;
            this.event = event;
        }

        public Command getCommand() {
            return command;
        }

        public Event getEvent() {
            return event;
        }

}
