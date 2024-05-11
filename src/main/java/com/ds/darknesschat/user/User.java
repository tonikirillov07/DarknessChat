package com.ds.darknesschat.user;

public class User {
    private String userName;
    private String userPassword;
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

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
