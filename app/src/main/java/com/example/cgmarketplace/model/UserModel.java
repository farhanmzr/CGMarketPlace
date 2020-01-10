package com.example.cgmarketplace.model;

public class UserModel {
    private String userName, userAddress, userTelephone, userEmail;
    public UserModel() {
    }

    public UserModel(String userName, String userAddress, String userTelephone, String userEmail) {
        this.userName = userName;
        this.userAddress = userAddress;
        this.userTelephone = userTelephone;
        this.userEmail = userEmail;
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
