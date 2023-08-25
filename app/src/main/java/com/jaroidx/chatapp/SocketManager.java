package com.jaroidx.chatapp;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketManager {
    private static Socket socketClient;

    public static void initSocket(){
        try {
            socketClient = IO.socket(Constants.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static Socket getSocketClient() {
        return socketClient;
    }
}
