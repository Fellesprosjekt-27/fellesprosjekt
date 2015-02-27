package com.gruppe27.fellesprosjekt.common.messages;

public class ErrorMessage {
    String message;

    public ErrorMessage() {
        this.message = "Noe gikk galt!";
    }

    public ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
