package com.example.customerplanmytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ForgotPasswordScreen extends AppCompatActivity {

    TextInputLayout _username;
    Button btn_next;
    String username,phone_from_database;
    public static final String send_username = "com.example.planmytrip.send_username";
    public static final String send_phone = "com.example.planmytrip.send_phone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_screen);

        _username = findViewById(R.id.forgot_password_username);
        btn_next = findViewById(R.id.forgot_password_nextbtn);
    }

    public void onNextClicked(View view){
        if(!validateUsername()){
            return;
        }
        username = _username.getEditText().getText().toString().trim();
        Query checkUser = FirebaseDatabase.getInstance().getReference("customers").orderByChild("user").equalTo(username);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    _username.setError(null);
                    _username.setErrorEnabled(false);
                    phone_from_database = snapshot.child(username).child("phone").getValue(String.class);
                    Intent intent = new Intent(getApplicationContext(),VerifyNo.class);
                    intent.putExtra(send_username,username);
                    intent.putExtra(send_phone,phone_from_database);
                    intent.putExtra("whattodo","updatePassword");
                    startActivity(intent);
                    finish();
                }else{
                    _username.setError("No such user exists");
                    _username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean validateUsername() {
        String val = _username.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            _username.setError("Field cannot be empty");
            return false;
        } else if (val.length() >= 25) {
            _username.setError("Hotel name too long");
            return false;
        } else {
            _username.setError(null);
            _username.setErrorEnabled(false);
            return true;
        }
    }

    public void onbackClicked(View view){
        startActivity(new Intent(getApplicationContext(),LoginScreen.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),LoginScreen.class));
        finish();
    }
}