package com.jaroidx.chatapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jaroidx.chatapp.R;
import com.jaroidx.chatapp.model.UserData;
import com.jaroidx.chatapp.utils.Constants;
import com.jaroidx.chatapp.utils.DeviceUtils;
import com.jaroidx.chatapp.utils.PrefManager;
import com.jaroidx.chatapp.utils.SocketManager;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener  {
    @BindView(R.id.edtUserName)
    EditText edtUserName;
    @BindView(R.id.tvUserNameError)
    TextView tvUserNameError;
    @BindView(R.id.edtPassword)
    EditText edtPassword;
    @BindView(R.id.tvPasswordError)
    TextView tvPasswordError;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.clMain)
    ConstraintLayout clMain;
    @BindView(R.id.cvLogin)
    CardView cvLogin;
    @BindView(R.id.clLoading)
    ConstraintLayout clLoading;

    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mSocket = SocketManager.getSocketClient();
        initView();
    }

    private void initView() {
        ButterKnife.bind(this);
        btnLogin.setOnClickListener(this);

        clMain.setOnTouchListener(this);
        cvLogin.setOnTouchListener(this);

        edtUserName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                DeviceUtils.hideSoftKeyBoard(LoginActivity.this, v);
                String data = edtUserName.getText().toString().trim();
                if (!checkUserName(data)) {
                    showError(tvUserNameError, R.string.invalid_username);
                }
            } else {
                tvUserNameError.setVisibility(View.GONE);
            }
        });

        edtPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                DeviceUtils.hideSoftKeyBoard(LoginActivity.this, v);
                String data = edtPassword.getText().toString().trim();
                if (!checkPassword(data)) {
                    showError(tvPasswordError, R.string.invalid_password);
                }
            } else {
                tvPasswordError.setVisibility(View.GONE);
            }
        });

        //Lắng nghe event on_login sau khi login thành công từ server
        mSocket.on(Constants.LOGIN_SUCCESS, onLoginListener);
    }

    private void showError(TextView tvUserNameError, int invalid_username) {
        tvUserNameError.setVisibility(View.VISIBLE);
        tvUserNameError.setText(getResources().getString(invalid_username));
    }

    private boolean checkPassword(String data) {
        if (TextUtils.isEmpty(data) || data.equals("") || data.length() < 5) {
            return false;
        }
        return true;
    }

    private boolean checkUserName(String data) {
        if (TextUtils.isEmpty(data) || data.equals("")) {
            return false;
        }
        return true;
    }

    private Emitter.Listener onLoginListener = args -> {
        JSONObject data = (JSONObject) args[0];
        if (data != null) {
            String json = data.toString();
            UserData userData = new Gson().fromJson(json, UserData.class);
            PrefManager.saveString(LoginActivity.this, PrefManager.PrefKey.DATA_LOGIN, json);
            go2MainActivity(userData);
        }
    };

    private void showLoading(boolean isShow) {
        clLoading.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void login() {
        String userName = edtUserName.getText().toString().trim();
        if (!checkUserName(userName)) {
            showError(tvUserNameError, R.string.invalid_username);
            return;
        }
        String password = edtPassword.getText().toString().trim();
        if (!checkPassword(password)) {
            showError(tvPasswordError, R.string.invalid_password);
            return;
        }
        showLoading(true);
        if (!mSocket.connected()) mSocket.connect();
        //Gửi event login với user name
        mSocket.emit(Constants.EVENT_LOGIN, userName);
    }

    private void go2MainActivity(UserData userData) {
        PrefManager.saveBoolean(this, PrefManager.PrefKey.IS_LOGIN, true);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(PrefManager.PrefKey.DATA_LOGIN, userData);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                login();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        mSocket.off(Constants.LOGIN_SUCCESS, onLoginListener);
        super.onDestroy();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.cvLogin || v.getId() == R.id.clMain) {
            v.requestFocus();
        }
        return true;
    }
}