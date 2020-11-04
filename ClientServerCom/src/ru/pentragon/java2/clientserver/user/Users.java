package ru.pentragon.java2.clientserver.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.List.of;

public class Users implements Serializable {//хранилище данных
    private static List<User> users;

    public Users() {
        users = new ArrayList<>();
    }

    public synchronized void addUsers(User... args){
        Collections.addAll(users, args);
    }

    public synchronized void addUser(User user){
        users.add(user);
    }

    public synchronized ArrayList<User> getUserList() {
        //System.out.println(USERS);
        return new ArrayList<>(users);
    }

    public synchronized User getUserByLogin(String login){
        for (User user : users) {
            if(user.getLogin().equals(login)){
                return user;
            }
        }
        return null;
    }
    public synchronized void removeUser(User user){
        users.remove(user);
    }



}
