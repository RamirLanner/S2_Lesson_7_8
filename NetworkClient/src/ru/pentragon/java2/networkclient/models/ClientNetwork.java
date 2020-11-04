package ru.pentragon.java2.networkclient.models;

import javafx.application.Platform;
import ru.pentragon.java2.clientserver.Command;
import ru.pentragon.java2.clientserver.commands.*;
import ru.pentragon.java2.clientserver.user.User;
import ru.pentragon.java2.logger.ClientMessageContainer;
import ru.pentragon.java2.logger.Logger;
import ru.pentragon.java2.networkclient.ClientApp;
import ru.pentragon.java2.networkclient.controllers.MainViewController;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class ClientNetwork {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8189;

    private final String host;
    private final int port;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Socket socket;
    private User user;

    public Logger getMyMessagesLogger() {
        return myMessages;
    }

    private Logger myMessages;

    public User getUser() {
        return user;
    }

    public ClientNetwork(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public ClientNetwork() {
        this(SERVER_ADDRESS, SERVER_PORT);
    }

    public boolean connect() {
        try {
            socket = new Socket(host, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            return true;
        } catch (IOException e) {
            System.err.println("Соединение не было установлено!");
            e.printStackTrace();
            return false;
        }
    }

    //формирование запроса на аутентификацию и получение результата
    public boolean sendAuthCommand(String login, String password) {
        try {
            getOutputStream().writeObject(Command.authCommand(login, password));
            Command command = readCommand();
            if (command == null) {
                System.err.println("Server send null auth response");
                return false;
            }
            switch (command.getType()) {
                case AUTH_OK: {
                    AuthOkCommandData data = (AuthOkCommandData) command.getData();
                    this.user = data.getUser();
                    myMessages = new ClientMessageContainer();
                    myMessages.createLogFileDir(user);
                    return true;
                }
                case AUTH_ERROR: {
                    AuthErrorCommandData data = (AuthErrorCommandData) command.getData();
                    String message = data.getErrorMessage();
                    ClientApp.showErrorWindow(message, "Auth error");
                    return false;
                }
                default: {
                    System.err.println("Unknown type command from server " + command.getType());
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Send/recieve logpass problem!");
            return false;
        }
    }

    private Command readCommand() throws IOException {
        try {
            return (Command) getInputStream().readObject();
        } catch (ClassNotFoundException e) {
            String errorMessage = "Unknown type of object from server";
            System.err.println(errorMessage);
            e.printStackTrace();
            return null;
        }
    }

    //получение стримов
    public ObjectInputStream getInputStream() {
        return inputStream;
    }
    //получение стримов
    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    //закрытие сокета
    public void close() {
        try {
            getOutputStream().writeObject(Command.endCommand());
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void waitMessages(MainViewController viewController) {
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    Command command = readCommand();
                    if (command == null) {
                        System.err.println("Server send null auth response");
                        continue;
                    }
                    switch (command.getType()) {
                        case MESSAGE: {
                            MessageCommandData data = (MessageCommandData) command.getData();
                            String receiver = data.getReceiver();
                            String message = data.getMessage();
                            String sender = data.getSender();
                            System.out.println(sender+" to "+ receiver+": message="+message);
                            Platform.runLater(() -> viewController.showMessages());
                            user.saveMsgToDialog(sender, message);
                            myMessages.writeToLogFile(sender, message);
                            break;
                        }
                        case UPDATE_USERS_LIST: {
                            UpdateUsersListCommandData data = (UpdateUsersListCommandData) command.getData();
                            Map<String,String> message = data.getUsers();
                            Platform.runLater(() -> viewController.updateUserList(message));
                            break;
                        }
                        default: {
                            System.err.println("Unknown type command from server " + command.getType());
                        }
                        
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Соединение было потеряно!");
            }
        });
        thread.setDaemon(true);
        thread.start();
    }


}
