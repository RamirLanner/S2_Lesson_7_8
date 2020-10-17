package ru.pentragon.java2.networkserver.handler;

import ru.pentragon.java2.networkserver.stmc.MyServer;
import ru.pentragon.java2.networkserver.stmc.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private static final String AUTH_CMD_PREFIX = "/auth";
    private static final String AUTHOK_CMD_PREFIX = "/authok";
    private static final String AUTHERR_CMD_PREFIX = "/autherr";

    private final MyServer myServer;
    private final Socket clientSocket;

    private DataInputStream in;
    private DataOutputStream out;

    private String username;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ClientHandler(MyServer myServer, Socket clientSocket) {
        this.myServer = myServer;
        this.clientSocket = clientSocket;
    }

    public void handle() throws IOException {
        in  = new DataInputStream(clientSocket.getInputStream());
        out = new DataOutputStream(clientSocket.getOutputStream());

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
            String message = in.readUTF();
            System.out.println(message);
            String[] parts = message.split("\\s+", 2);

            String  receiver= parts[0];
            String contentMsg = parts[1];
//            System.out.println(receiver);
//            System.out.println(contentMsg);
            myServer.privateMessage(receiver, contentMsg, this);

            // по хорошему надо бы добавить корректный выход, если пришла команда. Оставлю для новой версии.
        }
    }

    public String getUsername() {
        return username;
    }

    private synchronized void  authentication() throws IOException {
        while (true) {
            String message = in.readUTF();
            if (message.startsWith(AUTH_CMD_PREFIX)) {
                String[] parts = message.split("\\s+", 3);// один пробел и более
                String login = parts[1];
                String password = parts[2];

                this.user  = myServer.getAuthService().getUserByLoginAndPassword(login, password);
                if (user != null) {

                    if (myServer.isNicknameAlreadyBusy(user.getLogin())) {
                        out.writeUTF(AUTHERR_CMD_PREFIX + " Login and password are already used!");
                    }
                    else{
                        this.username = user.getUsername();
                        out.writeUTF(AUTHOK_CMD_PREFIX + " " + username);

                        myServer.broadcastMessage(username + " joined to chat!", this);
                        myServer.subscribe(this);
                        break;
                    }

                } else {
                    out.writeUTF(AUTHERR_CMD_PREFIX + " Login and/or password are invalid! Please, try again");
                }
            } else {
                out.writeUTF(AUTHERR_CMD_PREFIX + " /auth command is required!");
            }
        }
    }

    private void closeConnection() throws IOException {
        myServer.unsubscribe(this);
        clientSocket.close();
    }

    public void sendMessage(String message) throws IOException {
        out.writeUTF(message);
    }

}
