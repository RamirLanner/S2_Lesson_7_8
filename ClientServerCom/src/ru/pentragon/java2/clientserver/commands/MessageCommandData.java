package ru.pentragon.java2.clientserver.commands;

import java.io.Serializable;

public class MessageCommandData implements Serializable {

    private final String receiver;
    private final String message;
    private final String sender;

    public MessageCommandData(String receiver, String message, String sender) {
        this.receiver = receiver;
        this.message = message;
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }
}
