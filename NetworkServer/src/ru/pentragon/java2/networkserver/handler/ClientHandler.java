package ru.pentragon.java2.networkserver.handler;

import ru.pentragon.java2.clientserver.Command;
import ru.pentragon.java2.clientserver.CommandType;
import ru.pentragon.java2.clientserver.commands.AuthCommandData;
import ru.pentragon.java2.clientserver.commands.ChangeUsernameData;
import ru.pentragon.java2.clientserver.commands.MessageCommandData;
import ru.pentragon.java2.clientserver.user.User;
import ru.pentragon.java2.networkserver.stmc.MyServer;


import java.io.*;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class ClientHandler {

    private final MyServer myServer;
    private final Socket clientSocket;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    private String username;
    private User user;

    public User getUser() {
        return user;
    }

    public ClientHandler(MyServer myServer, Socket clientSocket) {
        this.myServer = myServer;
        this.clientSocket = clientSocket;
    }

    public void handle() throws IOException {
        in = new ObjectInputStream(clientSocket.getInputStream());
        out = new ObjectOutputStream(clientSocket.getOutputStream());

        new Thread(() -> {
            try {
                authentication();
                readMessages();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    closeConnection();
                } catch (IOException e) {
                    System.err.println("Failed to close connection!");
                }
            }
        }).start();
    }

    private void readMessages() throws IOException {
        while (true) {
        Command command = readCommand();
        if (command == null) {
            continue;
        }
        switch (command.getType()) {
            case END: {
                return;
            }
            case MESSAGE: {
                //System.out.println("Private");
                MessageCommandData data = (MessageCommandData) command.getData();
                String receiver = data.getReceiver();
                String message = data.getMessage();
                String sender = data.getSender();
                myServer.privateMessage(receiver, message, sender);
                break;
            }
            case PUBLIC_MESSAGE: {
                System.out.println("Public message, not ready");
                //если успею до сдачи то реализую, хочу что то свое сделать а не копипастом
                break;
            }
            case CHANGE_USERNAME:{
                ChangeUsernameData data = (ChangeUsernameData) command.getData();
                myServer.changeUsername(user, data.getNewUsername());
                break;
            }
            default: {
                System.err.println("Unknown type command from user " + command.getType());
            }
        }
    }

}

    public String getUsername() {
        return username;
    }

    private synchronized void authentication() throws IOException {
        //таймер
        userAuthTimer(120000L);

        while (true) {
            Command command = readCommand();
            if (command == null) {
                continue;
            }
            if (command.getType() == CommandType.AUTH) {
                AuthCommandData data = (AuthCommandData) command.getData();
                String login = data.getLogin();
                String password = data.getPassword();
                this.user = myServer.getAuthService().getUserByLoginAndPassword(login, password);
                if (user != null) {
                    if (myServer.isNicknameAlreadyBusy(user.getLogin())) {
                        sendMessage(Command.authErrorCommand("Login and password are already used!"));
                    } else {
                        this.username = user.getUsername();
                        sendMessage(Command.authOkCommand(this.user));
                        //myServer.broadcastMessage(username + " joined to chat!", this);
                        myServer.subscribe(this);
                        break;
                    }
                } else {
                    sendMessage(Command.authErrorCommand("Login and/or password are invalid! Please, try again"));
                }
            } else {
                sendMessage(Command.authErrorCommand("Auth command is required!"));
            }
        }
    }

    private void userAuthTimer(long delay) {
        new Thread(() -> {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if(username==null){
                        System.out.println("Неавторизованное подключение сброшено");
                        try {
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            },delay );//20000
        }).start();
    }

    private Command readCommand() throws IOException {
        try {
            return (Command) in.readObject();
        } catch (ClassNotFoundException e) {
            String errorMessage = "Unknown type of object from client";
            System.err.println(errorMessage);
            e.printStackTrace();
            sendMessage(Command.authErrorCommand(errorMessage));
            return null;
        }
    }

    private void closeConnection() throws IOException {
        myServer.unsubscribe(this);
        clientSocket.close();
    }

    public void sendMessage(Command message) throws IOException {
        out.writeObject(message);
    }

}
