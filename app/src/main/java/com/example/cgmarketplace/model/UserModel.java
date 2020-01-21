package com.example.cgmarketplace.model;

public class UserModel {
    private String userName, userAddress, userTelephone, userEmail, userPass, userImg;
    public UserModel() {
    }

    public UserModel(String userName, String userAddress, String userTelephone, String userEmail, String userPass, String userImg) {
        this.userName = userName;
        this.userAddress = userAddress;
        this.userTelephone = userTelephone;
        this.userEmail = userEmail;
        this.userPass = userPass;
        this.userImg = userImg;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserTelephone() {
        return userTelephone;
    }

    public void setUserTelephone(String userTelephone) {
        this.userTelephone = userTelephone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
