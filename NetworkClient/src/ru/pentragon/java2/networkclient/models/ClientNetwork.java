package ru.pentragon.java2.networkclient.models;

import javafx.application.Platform;
import ru.pentragon.java2.networkclient.controllers.MainViewController;
import ru.pentragon.java2.networkclient.user_repo.User;
import ru.pentragon.java2.networkclient.user_repo.Users;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientNetwork {
    private static final String AUTH_CMD_PREFIX = "/auth";
    private static final String AUTHOK_CMD_PREFIX = "/authok";
    private static final String AUTHERR_CMD_PREFIX = "/autherr";

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8189;

    private final String host;
    private final int port;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private Socket socket;
    private String username;
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

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
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            return true;
        } catch (IOException e) {
            System.err.println("Соединение не было установлено!");
            e.printStackTrace();
            return false;
        }
    }

    //формирование запроса на аутентификацию и получение результата
    public String sendAuthCommand(String login, String password) {
        try {
            getOutputStream().writeUTF(String.format("%s %s %s", AUTH_CMD_PREFIX, login, password));
            String response = getInputStream().readUTF();
            if (response.startsWith(AUTHOK_CMD_PREFIX)) {//проверяем с чего начинается ответ, если ок
                this.username = response.split("\\s+", 2)[1];//определяем юзернейм
                //нул значит хорошо
                user = Users.getUserByLogin(login);
                return null;
            } else {
                //получили не то что хотели передаем сообщение дальше
                return response.split("\\s+", 2)[1];
            }
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    //получение стримов
    public DataInputStream getInputStream() {
        return inputStream;
    }
    //получение стримов
    public DataOutputStream getOutputStream() {
        return outputStream;
    }

    //закрытие сокета
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void waitMessages(MainViewController viewController) {
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    String message = inputStream.readUTF();
                    System.out.println(message);

                    String[] parts = message.split("\\s+", 2);// один пробел и более
                    String login = parts[0];
                    String content = parts[1];

                    user.saveMsgToDialog(Users.getUserByLogin(login), content);

                        Platform.runLater(() -> {
                            //надо будет дописать логику обновления, пока не нашел приемлимый вариант, и пользователь должен перещелкивать что бы увидеть сообщение
                            //viewController.initialize();
                            //viewController.showMessages(user);
                        });
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
