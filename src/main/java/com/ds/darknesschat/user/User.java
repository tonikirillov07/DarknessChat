package com.ds.darknesschat.user;

public class User {
    private final String userName;
    private final String userPassword;
    private final String userDateOfRegistration;
    private long id = -1L;

    public User(String userName, String userPassword, String userDateOfRegistration) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.userDateOfRegistration = userDateOfRegistration;
    }

    public User(String userName, String userPassword, String userDateOfRegistration, long id) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.userDateOfRegistration = userDateOfRegistration;
        this.id = id;
    }

    public long getId() {
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

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userDateOfRegistration='" + userDateOfRegistration + '\'' +
                ", id=" + id +
                '}';
    }
}
