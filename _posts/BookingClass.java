package com.example.customerplanmytrip;

public class BookingClass {
    private int counter;
    private String username;

    public BookingClass(int counter, String username) {
        this.counter = counter;
        this.username = username;
    }

    public BookingClass() {

    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
