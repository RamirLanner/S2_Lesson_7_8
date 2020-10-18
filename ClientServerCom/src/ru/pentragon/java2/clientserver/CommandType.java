package ru.pentragon.java2.clientserver;

public enum CommandType {
    AUTH,
    AUTH_OK,
    AUTH_ERROR,
    MESSAGE,
    PUBLIC_MESSAGE,
    SERVER_INFO,
    UPDATE_USERS_LIST,
    END
}
