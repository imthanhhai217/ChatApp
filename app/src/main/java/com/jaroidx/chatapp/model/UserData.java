package com.jaroidx.chatapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserData implements Serializable {
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("numUsers")
    @Expose
    private Integer numUsers;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getNumUsers() {
        return numUsers;
    }

    public void setNumUsers(Integer numUsers) {
        this.numUsers = numUsers;
    }

    @Override
    public String toString() {
        return "LoginData{" +
                "username='" + username + '\'' +
                ", userId='" + userId + '\'' +
                ", numUsers=" + numUsers +
                '}';
    }
}
