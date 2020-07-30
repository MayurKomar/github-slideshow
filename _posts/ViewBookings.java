package com.example.planmytrip;

public class ViewBookings {
    private String username;

    public ViewBookings(String username) {
        this.username = username;
    }

    public ViewBookings() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
