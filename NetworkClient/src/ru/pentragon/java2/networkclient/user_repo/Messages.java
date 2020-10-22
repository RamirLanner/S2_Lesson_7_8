package ru.pentragon.java2.networkclient.user_repo;

import javafx.collections.FXCollections;

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

    public LinkedList<Message> getMSGsForView(User user){
        LinkedList<Message> temp2 = new LinkedList<>();
        List<String> temp = messageData.getOrDefault(user, null);
        if(temp!=null){
            for (String sk : temp) {
                temp2.add(new Message(sk));
            }
            return temp2;
        }
        return null;
    }

}
