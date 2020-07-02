package com.example.planmytrip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HotelProfile extends AppCompatActivity {

    TextInputLayout email,password,phone,price;
    String HOTEL_NAME,EMAIL,PASSWORD,PHONE,PRICE;
    Button update_btn;
    TextView Name_hotel;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_profile);

        reference = FirebaseDatabase.getInstance().getReference("users");

        email = findViewById(R.id.profile_email);
        password = findViewById(R.id.profile_password);
        phone = findViewById(R.id.profile_phone);
        price = findViewById(R.id.profile_price);
        Name_hotel = findViewById(R.id.main_name);
        showUserData();
    }

    private void showUserData() {

        Intent intent = getIntent();
        HOTEL_NAME = intent.getStringExtra("username");
        EMAIL = intent.getStringExtra(Dashboard.send_Email);
        PASSWORD = intent.getStringExtra("password");
        PHONE = intent.getStringExtra(Dashboard.send_phone);
        PRICE = intent.getStringExtra(Dashboard.send_price);

        email.getEditText().setText(EMAIL);
        password.getEditText().setText(PASSWORD);
        phone.getEditText().setText(PHONE);
        price.getEditText().setText(PRICE);
        Name_hotel.setText(HOTEL_NAME);
    }

    public void updateInfo(View view){
         if(isEmailChanged() | isPasswordChanged() | isPhoneChanged() | isPriceChanged()){
             Toast.makeText(this,"Data updated successfully",Toast.LENGTH_SHORT).show();
         }
         else{
             Toast.makeText(this,"Data not changed",Toast.LENGTH_SHORT).show();
         }
    }


    private boolean isEmailChanged() {

        if(!EMAIL.equals(email.getEditText().getText().toString().trim())){
            reference.child(HOTEL_NAME).child("email").setValue(email.getEditText().getText().toString().trim());
            return true;
        }else{
            return false;
        }

    }

    private boolean isPasswordChanged(){

        if(!PASSWORD.equals(password.getEditText().getText().toString().trim())){
            reference.child(HOTEL_NAME).child("password").setValue(password.getEditText().getText().toString().trim());
            return true;
        }else{
            return false;
        }

    }

    private boolean isPhoneChanged(){

        if(!PHONE.equals(phone.getEditText().getText().toString().trim())){
            reference.child(HOTEL_NAME).child("phoneNo").setValue(phone.getEditText().getText().toString().trim());
            return true;
        }else{
            return false;
        }

    }

    private boolean isPriceChanged(){

    if(!PRICE.equals(price.getEditText().getText().toString().trim())){
        reference.child(HOTEL_NAME).child("price").setValue(price.getEditText().getText().toString().trim());
        return true;
    }else{
        return false;
    }

    }

}