package com.example.wattsappchat.Models;

import android.widget.EditText;

public class Users {
    String profilePic,userName,password,userId,email,lastMessage,status;

    public Users()
    {

    }

    public Users(String profilePic, String userName, String password, String userId, String email, String lastMessage, String status) {
        this.profilePic = profilePic;
        this.userName = userName;
        this.password = password;
        this.userId = userId;
        this.email = email;
        this.lastMessage = lastMessage;
        this.status = status;
    }

    public Users(String userName, String password,  String email) {
        this.userName = userName;
        this.password = password;
        this.email = email;
    }

    public Users(EditText txtuserName) {
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
