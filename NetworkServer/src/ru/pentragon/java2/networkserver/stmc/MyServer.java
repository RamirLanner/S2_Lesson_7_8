package ru.pentragon.java2.networkserver.stmc;

import ru.pentragon.java2.networkserver.auth.AuthService;
import ru.pentragon.java2.networkserver.auth.DefaultAuthService;
import ru.pentragon.java2.networkserver.handler.ClientHandler;
import ru.pentragon.java2.networkserver.repo.Users;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyServer {
    private final ServerSocket serverSocket;
    private final AuthService authService;
    private final List<ClientHandler> clients = new ArrayList<>();
    private boolean serverOff;

    public MyServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        this.authService = new DefaultAuthService();
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

    public synchronized void subscribe(ClientHandler handler) {
        clients.add(handler);
    }

    public synchronized void unsubscribe(ClientHandler handler) {
        clients.remove(handler);
    }

    public synchronized boolean isNicknameAlreadyBusy(String login) {
        for (ClientHandler client : clients) {
            if (client.getUser().getLogin().equals(login)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void broadcastMessage(String message, ClientHandler sender) throws IOException {
        for (ClientHandler client : clients) {
            if (client == sender) {
                continue;
            }
            //privateMessage(client.getUser().getLogin(), message, sender);
            client.sendMessage("SERVER " + message);
        }
    }
    public synchronized void privateMessage(String receiver, String message, ClientHandler sender) throws IOException {
        for (ClientHandler client : clients) {
            if (client.getUser().getLogin().equals(receiver)) {
                client.sendMessage(sender.getUser().getLogin()+" "+message);
            }

        }
    }
}
