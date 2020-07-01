package com.example.planmytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity {

    TextInputLayout email,pass;
    private Button btnReg,loginBtn;
    public static final String send_Email = "com.example.planmytrip.send_Email";
    public static final String send_phone = "com.example.planmytrip.send_phone";
    public static final String send_price = "com.example.planmytrip.send_price";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnReg = findViewById(R.id.btnRegister);
        loginBtn = findViewById(R.id.btnLogin);
        email = findViewById(R.id.username);
        pass = findViewById(R.id.password);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateEmail() | !validatePassword()){
                    return;
                }
                else{
                    isUser();
                }
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this,Register.class));
            }
        });

    }

    private void isUser() {
        final String userName = email.getEditText().getText().toString().trim();
        final String Password = pass.getEditText().getText().toString().trim();
        final String[] eMail = new String[1];
        final String[] PhoneNo = new String[1];
        final String[] Price = new String[1];

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        Query checkUser = reference.orderByChild("username").equalTo(userName);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    email.setError(null);
                    email.setErrorEnabled(false);
                    String Passdb = dataSnapshot.child(userName).child("password").getValue(String.class);
                    if(Passdb.equals(Password)){
                        pass.setError(null);
                        pass.setErrorEnabled(false);
                        final Intent intent = new Intent(Dashboard.this,HotelProfile.class);
                        intent.putExtra("username",userName);
                        intent.putExtra("password",Password);
                        String eMailFromDb = dataSnapshot.child(userName).child("email").getValue(String.class);
                        String phoneFromDb = dataSnapshot.child(userName).child("phoneNo").getValue(String.class);
                        String priceFromDb = dataSnapshot.child(userName).child("price").getValue(String.class);
                        intent.putExtra(send_Email,eMailFromDb);
                        intent.putExtra(send_phone,phoneFromDb);
                        intent.putExtra(send_price,priceFromDb);
                        startActivity(intent);

                    }
                    else{
                        pass.setError("Wrong Password");
                        pass.requestFocus();
                    }
                }else{
                    email.setError("No such user found");
                    email.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private Boolean validateEmail() {
        String val = email.getEditText().getText().toString();
        //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        }else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword(){
        String val = pass.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            pass.setError("Field cannot be empty");
            return false;
        }else {
            pass.setError(null);
            pass.setErrorEnabled(false);
            return true;
        }
    }
}