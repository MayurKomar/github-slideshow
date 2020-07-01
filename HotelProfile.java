package com.example.planmytrip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class HotelProfile extends AppCompatActivity {

    TextInputLayout hotel_name,email,password,phone,price;
    String HOTEL_NAME,EMAIL,PASSWORD,PHONE,PRICE;
    Button update_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_profile);

        hotel_name = findViewById(R.id.profile_hotel_name);
        email = findViewById(R.id.profile_email);
        password = findViewById(R.id.profile_password);
        phone = findViewById(R.id.profile_phone);
        price = findViewById(R.id.profile_price);

        showUserData();
    }

    private void showUserData() {

        Intent intent = getIntent();
        HOTEL_NAME = intent.getStringExtra("username");
        EMAIL = intent.getStringExtra(Dashboard.send_Email);
        PASSWORD = intent.getStringExtra("password");
        PHONE = intent.getStringExtra(Dashboard.send_phone);
        PRICE = intent.getStringExtra(Dashboard.send_price);

        hotel_name.getEditText().setText(HOTEL_NAME);
        email.getEditText().setText(EMAIL);
        password.getEditText().setText(PASSWORD);
        phone.getEditText().setText(PHONE);
        price.getEditText().setText(PRICE);
    }
}