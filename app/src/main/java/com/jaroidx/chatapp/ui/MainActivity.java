package com.jaroidx.chatapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.jaroidx.chatapp.R;
import com.jaroidx.chatapp.utils.SocketManager;

import io.socket.client.Socket;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Socket mSocket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSocket = SocketManager.getSocketClient();
        mSocket.connect();


    }
}