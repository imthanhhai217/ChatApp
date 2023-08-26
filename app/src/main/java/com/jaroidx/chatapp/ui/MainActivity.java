package com.jaroidx.chatapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jaroidx.chatapp.R;
import com.jaroidx.chatapp.adapter.ChattingAdapter;
import com.jaroidx.chatapp.model.ChatMessage;
import com.jaroidx.chatapp.model.MessageData;
import com.jaroidx.chatapp.model.UserData;
import com.jaroidx.chatapp.utils.Constants;
import com.jaroidx.chatapp.utils.PrefManager;
import com.jaroidx.chatapp.utils.SocketManager;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvCountUser)
    TextView tvCountUser;
    @BindView(R.id.imgAvatar)
    ImageView imgAvatar;
    @BindView(R.id.clLoading)
    ConstraintLayout clLoading;
    @BindView(R.id.rvMessage)
    RecyclerView rvMessage;
    @BindView(R.id.imgSend)
    ImageView imgSend;
    @BindView(R.id.edtMessage)
    EditText edtMessage;

    private volatile boolean isConnected = false;
    private volatile boolean mTyping = false;
    private UserData userData;

    private ChattingAdapter mChattingAdapter;
    private ArrayList<ChatMessage> mListChatMessages;

    private Socket mSocket;

    private Handler typingHandler = new Handler();
    private static final int TYPING_TIMEOUT = 800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initSocket();
        UserData userData = (UserData) getIntent().getSerializableExtra(PrefManager.PrefKey.DATA_LOGIN);
        if (userData != null) {
            bindData(userData);
        } else {
            go2Login();
        }
    }

    private void initSocket() {
        mSocket = SocketManager.getSocketClient();
        mSocket.on(Constants.DISCONNECT, onDisconnect);
        mSocket.on(Constants.CONNECT_ERROR, onConnectError);
        mSocket.on(Constants.NEW_MESSAGE, onNewMessage);
        mSocket.on(Constants.USER_JOIN, onUserJoin);
        mSocket.on(Constants.LOGOUT, onLogout);
        mSocket.on(Constants.TYPING, onTyping);
        mSocket.on(Constants.STOP_TYPING, onStopTyping);
        isConnected = mSocket.connected();
    }

    @Override
    protected void onDestroy() {
        mSocket.disconnect();
        mSocket.off(Constants.DISCONNECT, onDisconnect);
        mSocket.off(Constants.CONNECT_ERROR, onConnectError);
        mSocket.off(Constants.NEW_MESSAGE, onNewMessage);
        mSocket.off(Constants.USER_JOIN, onUserJoin);
        mSocket.off(Constants.LOGOUT, onLogout);
        mSocket.off(Constants.TYPING, onTyping);
        mSocket.off(Constants.STOP_TYPING, onStopTyping);
        super.onDestroy();
    }

    private void go2Login() {
        PrefManager.saveBoolean(MainActivity.this, PrefManager.PrefKey.IS_LOGIN, false);
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void bindData(UserData userData) {
        this.userData = userData;
        tvUserName.setText(userData.getUsername());
        updateCountUsers(userData.getNumUsers());
        mListChatMessages = new ArrayList<>();
        mListChatMessages.clear();
        mChattingAdapter = new ChattingAdapter(mListChatMessages, userData.getUserId());
        rvMessage.setAdapter(mChattingAdapter);
    }

    private void updateCountUsers(Integer number) {
        tvCountUser.setText(number + " users");
    }

    private Emitter.Listener onConnect = args -> {
        Log.d(TAG, "onConnect : success");
    };
    private Emitter.Listener onDisconnect = args -> {
        runOnUiThread(() -> {
            isConnected = false;
            Toast.makeText(this, R.string.disconnect, Toast.LENGTH_LONG).show();
            PrefManager.saveBoolean(MainActivity.this, PrefManager.PrefKey.IS_LOGIN, false);
            go2Login();
        });
    };
    private Emitter.Listener onConnectError = args -> {
        runOnUiThread(() -> {
            Toast.makeText(this, getResources().getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
            PrefManager.saveBoolean(MainActivity.this, PrefManager.PrefKey.IS_LOGIN, false);
            go2Login();
        });
    };
    private Emitter.Listener onNewMessage = args -> {
        runOnUiThread(() -> {
            JSONObject data = (JSONObject) args[0];
            if (data != null) {
                MessageData messageData = new Gson().fromJson(data.toString(), MessageData.class);
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setMessage(messageData.getMessage());
                chatMessage.setUserName(messageData.getUsername());
                chatMessage.setMessageType((userData.getUserId().equals(messageData.getUserId())) ? ChattingAdapter.MESSAGE_SELF : ChattingAdapter.MESSAGE_FRIEND);
                removeMessage(messageData.getUserId());
                addNewMessage(chatMessage);
            }
        });
    };
    private Emitter.Listener onUserJoin = args -> {
        runOnUiThread(() -> {
            JSONObject data = (JSONObject) args[0];
            if (data != null) {
                UserData userJoin = new Gson().fromJson(data.toString(), UserData.class);
                updateCountUsers(userJoin.getNumUsers());
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setMessage(userJoin.getUsername() + " has joined the chat room.");
                chatMessage.setMessageType(ChattingAdapter.MESSAGE_LOG);
                addNewMessage(chatMessage);
            }
        });
    };
    private Emitter.Listener onLogout = args -> {
        runOnUiThread(() -> {
            JSONObject data = (JSONObject) args[0];
            if (data != null) {
                UserData userLeft = new Gson().fromJson(data.toString(), UserData.class);
                updateCountUsers(userLeft.getNumUsers());
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setMessage(userLeft.getUsername() + " has left the chat room.");
                chatMessage.setMessageType(ChattingAdapter.MESSAGE_LOG);
                removeMessage(userLeft.getUserId());
                addNewMessage(chatMessage);
            }
        });
    };
    private Emitter.Listener onTyping = args -> {
        runOnUiThread(() -> {
            JSONObject data = (JSONObject) args[0];
            if (data != null) {
                UserData userTyping = new Gson().fromJson(data.toString(), UserData.class);
                if (userTyping.getUserId().equals(userData.getUserId())) {
                    return;
                }
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setMessageType(ChattingAdapter.MESSAGE_TYPING);
                chatMessage.setUserName(userTyping.getUsername());
                chatMessage.setUid(userTyping.getUserId());
                chatMessage.setMessage(userTyping.getUsername() + " is typing ...");
                addNewMessage(chatMessage);
            }
        });
    };

    private Emitter.Listener onStopTyping = args -> {
        runOnUiThread(() -> {
            JSONObject data = (JSONObject) args[0];
            if (data != null) {
                UserData userStopTyping = new Gson().fromJson(data.toString(), UserData.class);
                if (userStopTyping.getUserId().equals(userData.getUserId())) {
                    return;
                }
                removeMessage(userStopTyping.getUserId());
            }
        });
    };

    private void removeMessage(String userId) {
        mChattingAdapter.removeItem(userId);
        rvMessage.smoothScrollToPosition(0);
    }

    private void addNewMessage(ChatMessage chatMessage) {
        mListChatMessages.add(0, chatMessage);
        mChattingAdapter.notifyItemInserted(0);
        rvMessage.smoothScrollToPosition(0);
    }


    private void initView() {
        ButterKnife.bind(this);

        imgSend.setOnClickListener(v -> {
            mTyping = false;
            String message = edtMessage.getText().toString().trim();
            if (TextUtils.isEmpty(message)) {
                edtMessage.requestFocus();
                return;
            }
            edtMessage.setText("");
            mSocket.emit(Constants.NEW_MESSAGE, message);
            edtMessage.clearFocus();
        });

        edtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.equals("")) return;

                if (!mTyping) {
                    mTyping = true;
                    mSocket.emit(Constants.TYPING);
                }
                typingHandler.removeCallbacks(runnableStopTyping);
                typingHandler.postDelayed(runnableStopTyping, TYPING_TIMEOUT);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private Runnable runnableStopTyping = new Runnable() {
        @Override
        public void run() {
            if (!mTyping) return;
            mTyping = false;
            mSocket.emit(Constants.STOP_TYPING);
        }
    };

    private void showLoading(boolean isShow) {
        clLoading.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}