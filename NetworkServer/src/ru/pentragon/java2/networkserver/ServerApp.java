package ru.pentragon.java2.networkserver;

import MyProfLogger.MyProfLogger;
import ru.pentragon.java2.networkserver.stmc.MyServer;

import java.io.IOException;

public class ServerApp {

    private static final int DEFAULT_PORT =8189;
    private static MyProfLogger myProfLogger;


    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        myProfLogger= new MyProfLogger(ServerApp.class.getName());
        myProfLogger.addMessageInfo("Start program.");
        if(args.length!=0){
            port = Integer.parseInt(args[0]);
        }
        try {
            new MyServer(port).start();
        }catch (IOException e){
            System.err.println("Failed to create Server(MyServerClass Object)");
            e.printStackTrace();
            System.exit(1);
        }
    }

}
