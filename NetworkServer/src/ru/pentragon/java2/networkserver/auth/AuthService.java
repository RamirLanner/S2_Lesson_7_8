package ru.pentragon.java2.networkserver.auth;


import ru.pentragon.java2.clientserver.user.User;

import java.sql.SQLException;

public interface AuthService {
    default void start(){};

    User getUserByLoginAndPassword(String login, String password);
    void updateUsername(User user, String newUsername);

    default void stop(){};

}
