package com.ds.darknesschat.user;

public class User {
    private final String userName;
    private final String userPassword;
    private final String userDateOfRegistration;
    private int id = -1;

    public User(String userName, String userPassword, String userDateOfRegistration) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.userDateOfRegistration = userDateOfRegistration;
    }

    public User(String userName, String userPassword, String userDateOfRegistration, int id) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.userDateOfRegistration = userDateOfRegistration;
        this.id = id;
    }

    public int getId() {
        return id;
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
