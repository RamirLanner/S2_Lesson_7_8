package ru.pentragon.java2.networkserver.auth;

import ru.pentragon.java2.clientserver.user.User;
import ru.pentragon.java2.networkserver.stmc.MyServer;

public class DefaultAuthService implements AuthService {


    @Override
    public void start() {
        //здесь должна быть логика на подключению к бд с данными
        System.out.println("Auth service has been started");
    }

    @Override
    public synchronized User getUserByLoginAndPassword(String login, String password) {
//        for (User user : MyServer.getUsers().getUserList()) {
//            if (user.getLogin().equals(login) && user.getPassword().equals(password)) {
//                return user;
//            }
//        }
        return null;
    }

    @Override
    public void stop() {
        //здесь логика закрытия подключения к бд
        System.out.println("Auth service has been finished");
    }
}
