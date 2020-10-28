package ru.pentragon.java2.clientserver.commands;


import java.io.Serializable;

public class ChangeUsernameData implements Serializable {
    private final String newUsername;

    public ChangeUsernameData(String newUsername) {
        this.newUsername = newUsername;
    }

    public String getNewUsername() {
        return newUsername;
    }
}
