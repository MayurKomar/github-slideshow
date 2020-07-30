package com.example.customerplanmytrip;

public class UserInfo {
    private String user;
    private String password;
    private String phone;
    private String email;

    public UserInfo(String user, String password, String phone, String email) {
        this.user = user;
        this.password = password;
        this.phone = phone;
        this.email = email;
    }

    public UserInfo() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
