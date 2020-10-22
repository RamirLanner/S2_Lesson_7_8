package ru.pentragon.java2.networkserver.stmc;

import ru.pentragon.java2.networkserver.repo.Messages;

import java.util.Objects;

public class User {

    private final String login;
    private final String password;
    private final String username;
    private Messages messages;
    private boolean onlineStatus;

    public User(String login, String password, String username) {
        this.login = login;
        this.password = password;
        this.username = username;
        this.messages = new Messages();
    }

    public synchronized String getLogin() {
        return login;
    }

    public synchronized String getPassword() {
        return password;
    }

    public synchronized Messages getMessages() {
        return messages;
    }

    public synchronized String getUsername() {
        return username;
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
}
