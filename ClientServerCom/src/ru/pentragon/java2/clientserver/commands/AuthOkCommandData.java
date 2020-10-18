package ru.pentragon.java2.clientserver.commands;

import ru.pentragon.java2.clientserver.user.User;

import java.io.Serializable;

public class AuthOkCommandData implements Serializable {

    private final User user;

    public AuthOkCommandData(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
