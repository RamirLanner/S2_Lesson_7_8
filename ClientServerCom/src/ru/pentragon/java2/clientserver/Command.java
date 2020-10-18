package ru.pentragon.java2.clientserver;

import ru.pentragon.java2.clientserver.commands.*;
import ru.pentragon.java2.clientserver.user.User;

import java.io.Serializable;
import java.util.Map;

public class Command implements Serializable {
    private CommandType type;
    private Object data;

    public CommandType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public static Command authCommand(String login, String password) {
        Command command = new Command();
        command.type = CommandType.AUTH;
        command.data = new AuthCommandData(login, password);
        return command;
    }

    public static Command authErrorCommand(String authErrorMessage) {
        Command command = new Command();
        command.type = CommandType.AUTH_ERROR;
        command.data = new AuthErrorCommandData(authErrorMessage);
        return command;
    }

    public static Command authOkCommand(User user) {
        Command command = new Command();
        command.type = CommandType.AUTH_OK;
        command.data = new AuthOkCommandData(user);
        return command;
    }

    public static Command messageCommand(String receiver, String message, String sender) {
        Command command = new Command();
        command.type = CommandType.MESSAGE;
        command.data = new MessageCommandData(receiver, message, sender);
        return command;
    }

    public static Command publicMessageCommand(String sender, String message) {
        Command command = new Command();
        command.type = CommandType.AUTH_ERROR;
        command.data = new PublicMessageCommandData(sender, message);
        return command;
    }

    public static Command serverMessageCommand(String sender, String message) {
        Command command = new Command();
        command.type = CommandType.AUTH_OK;
        command.data = new ServerMessageCommandData(sender, message);
        return command;
    }

    public static Command updateUserListCommand(Map<String, String> users) {
        Command command = new Command();
        command.type = CommandType.UPDATE_USERS_LIST;
        command.data = new UpdateUsersListCommandData(users);
        return command;
    }

    public static Command endCommand() {
        Command command = new Command();
        command.type = CommandType.END;
        return command;
    }

}
