package ru.pentragon.java2.networkserver.stmc;

import ru.pentragon.java2.clientserver.Command;
import ru.pentragon.java2.clientserver.user.User;
import ru.pentragon.java2.clientserver.user.Users;
import ru.pentragon.java2.networkserver.auth.AuthService;
import ru.pentragon.java2.networkserver.auth.DefaultAuthService;
import ru.pentragon.java2.networkserver.auth.MSSqlAuthService;
import ru.pentragon.java2.networkserver.auth.SqlLiteAuthService;
import ru.pentragon.java2.networkserver.handler.ClientHandler;
import ru.pentragon.java2.networkserver.repo.MyRepo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyServer {
    private final ServerSocket serverSocket;
    private final AuthService authService;
    private final List<ClientHandler> clients = new ArrayList<>();
    private boolean serverOff;
    private Users users;

    public synchronized Users getUsers() {
        return this.users;
    }

    public MyServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        //this.authService = new DefaultAuthService();
        //users = MyRepo.createDataBase();
        this.authService = new MSSqlAuthService();
        //this.authService = new SqlLiteAuthService();
    }

    public void start() throws IOException{
        System.out.println("Server started.");

        authService.start();
        try{
            while(!serverOff){
                waitAndProcessNewClientConnection();
            }
        }catch (IOException e){
            System.err.println("Failed to accept new connection");
            e.printStackTrace();
        } finally {
            authService.stop();
            serverSocket.close();
        }
    }

    private void waitAndProcessNewClientConnection() throws IOException{
        System.out.println("Ожидание нового подключения....");
        Socket clientSocket = serverSocket.accept();
        System.out.println("Клиент подключился");// /auth login password
        processClientConnection(clientSocket);
    }

    private void processClientConnection(Socket clientSocket) throws IOException {
        ClientHandler clientHandler = new ClientHandler(this, clientSocket);
        clientHandler.handle();
    }

    public AuthService getAuthService() {
        return authService;
    }

    public synchronized void subscribe(ClientHandler handler) throws IOException {
        clients.add(handler);
        Map<String, String> mapU= listConnectedUsers();
        broadcastMessage(null, Command.updateUserListCommand(mapU));
        //users.addUser(handler.getUser());
    }

    public synchronized void unsubscribe(ClientHandler handler) throws IOException {
        clients.remove(handler);
        Map<String, String> mapU= listConnectedUsers();
        broadcastMessage(null, Command.updateUserListCommand(mapU));
        //users.removeUser(handler.getUser());
    }

    private Map<String, String> listConnectedUsers() {
        Map<String, String> mapU = new HashMap<>();
        for (ClientHandler client : clients) {
            mapU.put(client.getUser().getLogin(), client.getUsername());
        }
        return mapU;
    }

    public synchronized boolean isNicknameAlreadyBusy(String login) {
        for (ClientHandler client : clients) {
            if (client.getUser().getLogin().equals(login)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void broadcastMessage(ClientHandler sender, Command command) throws IOException {
        for (ClientHandler client : clients) {
            if (client == sender) {
                continue;
            }
            client.sendMessage(command);
        }
    }

    public synchronized void privateMessage(String receiver, String message, String sender) throws IOException {
        for (ClientHandler client : clients) {
            if (client.getUser().getLogin().equals(receiver)) {
                client.sendMessage(Command.messageCommand(receiver,message,sender));

                //сохраняем личную переписку
                //client.getUser().saveMsgToDialog(users.getUserByLogin(sender).getLogin(), message);//записали сообщение получателю
                //users.getUserByLogin(sender).saveMsgToDialog(client.getUser().getLogin(),message);//записали сообщение отправителю
            }
        }
    }

    public synchronized void changeUsername(User user, String newUsername) throws IOException {
        authService.updateUsername(user, newUsername);
        Map<String, String> mapU= listConnectedUsers();
        broadcastMessage(null, Command.updateUserListCommand(mapU));
    }
}
