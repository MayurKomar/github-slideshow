package com.example.planmytrip;

public class UserInfo {

    String username,email,password,phoneNo,roomNo,price,Latitude,Longitude,city;

    public UserInfo(String username, String email, String password, String phoneNo, String roomNo, String price,String latitude, String longitude, String city) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNo = phoneNo;
        this.roomNo = roomNo;
        this.price = price;
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.city = city;
    }

    public String getUsername() {
        return username;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


}
