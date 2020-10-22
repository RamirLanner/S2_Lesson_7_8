package ru.pentragon.java2.networkserver.repo;

import ru.pentragon.java2.networkserver.stmc.User;

import java.util.*;

public class Messages {

    private Map<User, LinkedList<String>> messageData;

    public Messages() {
        this.messageData = new HashMap<>();
    }

    public void addMsg(User user, String msg){
        if(messageData.containsKey(user)){
            messageData.get(user).addFirst(msg);
        }
        else {
            LinkedList<String> myList = new LinkedList<>();
            myList.add(msg);
            messageData.put(user, myList);
        }
    }

    public LinkedList<String> getMSGs(User user){
        return messageData.getOrDefault(user, null);
    }

}
