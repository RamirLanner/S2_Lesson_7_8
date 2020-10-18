package ru.pentragon.java2.clientserver.user;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

public class Message implements Serializable {
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
