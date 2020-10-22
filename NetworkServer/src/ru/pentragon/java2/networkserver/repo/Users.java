package ru.pentragon.java2.networkserver.repo;

import ru.pentragon.java2.networkserver.stmc.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.List.of;

public class Users {//хранилище данных
    private static final List<User> USERS = Collections.synchronizedList(of(
            new User("login1", "pass1", "User1"),
            new User("login2", "pass2", "User2"),
            new User("login3", "pass3", "User3"),
            new User("login5", "pass5", "User5"),
            new User("login4", "pass4", "User4"),
            new User("login6", "pass6", "User6"),
            new User("login7", "pass7", "User7"),
            new User("SERVER", "SERVER", "Server")
    ));

    private static List<User> activeUsers;

    public static List<User> getUSERS() {
        return USERS;
    }

}
