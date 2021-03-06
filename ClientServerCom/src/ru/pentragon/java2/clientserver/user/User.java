package ru.pentragon.java2.clientserver.user;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {

    private final String login;
    private final String password;
    private String username;
    private Messages messages;

    public User(String login, String password, String username) {
        this.login = login;
        this.password = password;
        this.username = username;
        this.messages = new Messages();
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Messages getMessages() {
        return messages;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public StringProperty getUserNameStringPropertyType(){
        return new SimpleStringProperty(username);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(login, user.login) &&
                Objects.equals(password, user.password) &&
                Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password, username);
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public void saveMsgToDialog(String user, String msg){//User user
        messages.addMsg(user, msg);
    }

}
