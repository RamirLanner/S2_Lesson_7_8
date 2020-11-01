package ru.pentragon.java2.logger;

import ru.pentragon.java2.clientserver.user.User;

import java.util.LinkedList;
import java.util.List;

public interface Logger {
    boolean createLogFileDir(User user);
    boolean closeLogFile();
    LinkedList<String> readFromLogFile(String sender);
    boolean writeToLogFile(String sender, String message);
}
