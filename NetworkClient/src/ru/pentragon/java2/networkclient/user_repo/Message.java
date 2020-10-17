package ru.pentragon.java2.networkclient.user_repo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class Message {
    private StringProperty msg;

    public Message(String msg) {
        this.msg = new SimpleStringProperty(msg);
    }

    public String getMsg() {
        return msg.get();
    }

    public StringProperty msgProperty() {
        return msg;
    }
}
