package ru.pentragon.java2.clientserver.commands;

import java.io.Serializable;
import java.util.Map;

public class UpdateUsersListCommandData implements Serializable {

    private final Map<String,String> users;

    public UpdateUsersListCommandData(Map<String, String> users) {
        this.users = users;
    }

    public Map<String, String> getUsers() {
        return users;
    }
}
