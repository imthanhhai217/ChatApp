package com.jaroidx.chatapp.model;

public class ChatMessage {
    private String uid;
    private String userName;
    private String message;
    private String time;
    private int messageType;

    public ChatMessage(String uid, String userName, String message, String time, int messageType) {
        this.uid = uid;
        this.userName = userName;
        this.message = message;
        this.time = time;
        this.messageType = messageType;
    }

    public ChatMessage() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "uid='" + uid + '\'' +
                ", userName='" + userName + '\'' +
                ", message='" + message + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
