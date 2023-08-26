package com.jaroidx.chatapp.utils;

import io.socket.client.Socket;

public class Constants {
    public static final String CHAT_SERVER_URL = "http://192.168.1.74:3000";
    public static final String EVENT_LOGIN = "login";
    public static final String LOGIN_SUCCESS = "login_success";
    public static final String CONNECT = Socket.EVENT_CONNECT;
    public static final String DISCONNECT = Socket.EVENT_DISCONNECT;
    public static final String CONNECT_ERROR = Socket.EVENT_CONNECT_ERROR;
    public static final String NEW_MESSAGE = "new_message";
    public static final String USER_JOIN = "user_joined";
    public static final String LOGOUT = "logout";
    public static final String TYPING = "typing";
    public static final String STOP_TYPING = "stop_typing";
}
