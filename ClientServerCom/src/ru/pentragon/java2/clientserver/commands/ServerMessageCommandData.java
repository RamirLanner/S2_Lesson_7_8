package ru.pentragon.java2.clientserver.commands;

import java.io.Serializable;

public class ServerMessageCommandData implements Serializable {
    private final String message;

    public ServerMessageCommandData(String sender, String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

}
