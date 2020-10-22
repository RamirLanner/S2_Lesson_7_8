package ru.pentragon.java2.networkserver.auth;

import ru.pentragon.java2.networkserver.repo.Users;
import ru.pentragon.java2.networkserver.stmc.User;

import java.util.List;

import static java.util.List.of;

public class DefaultAuthService implements AuthService{

/*    private static final List<User> USERS = of(
            new User("login1", "pass1", "User1"),
            new User("login2", "pass2", "User2"),
            new User("login3", "pass3", "User3"),
            new User("login5", "pass5", "User5"),
            new User("login4", "pass4", "User4"),
            new User("login6", "pass6", "User6"),
            new User("login7", "pass7", "User7")
    );*/

    @Override
    public void start() {
        //здесь должна быть логика на подключению к бд с данными
        System.out.println("Auth service has been started");
    }

    @Override
    public synchronized User getUserByLoginAndPassword(String login, String password) {
        for (User user : Users.getUSERS()) {
            if (user.getLogin().equals(login) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void stop() {
        //здесь логика закрытия подключения к бд
        System.out.println("Auth service has been finished");
    }
}
