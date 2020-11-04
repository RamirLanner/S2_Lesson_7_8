package ru.pentragon.java2.clientserver.user;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Messages implements Serializable {

    private Map<String, LinkedList<String>> messageData;//Map<User, LinkedList<String>>

    public Messages() {
        this.messageData = new HashMap<>();
    }

    public void addMsg(String user, String msg){//User user
        if(messageData.containsKey(user)){
            messageData.get(user).addFirst(msg);
        }
        else {
            LinkedList<String> myList = new LinkedList<>();
            myList.add(msg);
            messageData.put(user, myList);
        }
    }

    public Map<String, LinkedList<String>> getMessageData() {
        return messageData;
    }
//    public LinkedList<String> getMSGs(User user){
//        return messageData.getOrDefault(user, null);
//    }

    public LinkedList<Message> getMSGsForView(String user){
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
