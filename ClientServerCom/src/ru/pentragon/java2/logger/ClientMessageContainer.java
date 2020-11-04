package ru.pentragon.java2.logger;

import ru.pentragon.java2.clientserver.user.User;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ClientMessageContainer implements Logger{

    private File myLogFileDir;
    //private File myLogFile;

    @Override
    public boolean createLogFileDir(User user) {//
        myLogFileDir = new File(("NetworkClient//src//resources//"+user.getLogin()));//Test.txt
        if (!myLogFileDir.isDirectory()){
            return myLogFileDir.mkdir();
        } else {
            return true;
        }
    }

    @Override
    public boolean closeLogFile() {
        return false;
    }

    @Override
    public LinkedList<String> readFromLogFile(String sender) {
        File file = new File((myLogFileDir.toPath()+"//"+sender));
        List<String> messageHistory =  new ArrayList<>(100);
        if(file.exists()){
            try {
                //System.out.println("Линий= "+Files.);
                messageHistory = Files.readAllLines(file.toPath());
                return new LinkedList<>(messageHistory);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public boolean writeToLogFile(String sender, String message) {
        try
        {
            File file = new File((myLogFileDir.toPath()+"//"+sender));
            if (!file.exists()){
                boolean created = file.createNewFile();
                if(created){
                    System.out.println("File has been created");
                }else {
                    System.err.println("Creating the log message has occurred failed");
                    return false;
                }
            }else {
                System.out.println("File is exist in project");
            }
            Files.write(file.toPath(), (message+System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
            return true;
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        return false;
    }
}
