package com.ds.darknesschat.user;

public class User {
    private final String userName;
    private final String userPassword;
    private final String userDateOfRegistration;

    public User(String userName, String userPassword, String userDateOfRegistration) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.userDateOfRegistration = userDateOfRegistration;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserDateOfRegistration() {
        return userDateOfRegistration;
    }
}
