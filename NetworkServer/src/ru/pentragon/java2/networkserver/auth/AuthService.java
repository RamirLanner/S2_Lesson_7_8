package ru.pentragon.java2.networkserver.auth;


import ru.pentragon.java2.clientserver.user.User;

public interface AuthService {
    default void start(){};

    User getUserByLoginAndPassword(String login, String password);

    default void stop(){};

}
