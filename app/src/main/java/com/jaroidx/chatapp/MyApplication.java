package com.jaroidx.chatapp;

import android.app.Application;

import com.jaroidx.chatapp.utils.SocketManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SocketManager.initSocket();
    }
}
