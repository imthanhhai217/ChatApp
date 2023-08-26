package com.jaroidx.chatapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.gson.Gson;
import com.jaroidx.chatapp.R;
import com.jaroidx.chatapp.model.UserData;
import com.jaroidx.chatapp.utils.Constants;
import com.jaroidx.chatapp.utils.PrefManager;
import com.jaroidx.chatapp.utils.SocketManager;

import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SplashActivity extends AppCompatActivity {

    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mSocket = SocketManager.getSocketClient().connect();
        mSocket.on(Constants.LOGIN_SUCCESS, onLoginListener);

        new Handler().postDelayed(() -> checkLoginState(), 1000);
    }

    private void checkLoginState() {
        boolean isLogin = PrefManager.getBoolean(this, PrefManager.PrefKey.IS_LOGIN);
        //Nếu đã login thì tự động emit event login
        if (isLogin) {
            String data = PrefManager.getString(this, PrefManager.PrefKey.DATA_LOGIN);
            if (data != null) {
                UserData userData = new Gson().fromJson(data, UserData.class);
                mSocket.emit(Constants.EVENT_LOGIN, userData.getUsername());
            }else {
                go2LoginActivity();
            }
        } else {
            go2LoginActivity();
        }
    }

    private Emitter.Listener onLoginListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            if (data != null) {
                String json = data.toString();
                UserData userData = new Gson().fromJson(json, UserData.class);
                PrefManager.saveString(SplashActivity.this, PrefManager.PrefKey.DATA_LOGIN, json);
                go2MainActivity(userData);
            }
        }
    };

    private void go2MainActivity(UserData userData) {
        PrefManager.saveBoolean(this, PrefManager.PrefKey.IS_LOGIN, true);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(PrefManager.PrefKey.DATA_LOGIN, userData);
        startActivity(intent);
        finish();
    }

    private void go2LoginActivity() {
        PrefManager.saveBoolean(this, PrefManager.PrefKey.IS_LOGIN, false);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        mSocket.off(Constants.LOGIN_SUCCESS, onLoginListener);
        super.onDestroy();
    }
}