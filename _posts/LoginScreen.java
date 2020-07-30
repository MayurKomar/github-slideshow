package com.example.customerplanmytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginScreen extends AppCompatActivity {

    TextInputLayout _username,_password;
    String username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        _username = findViewById(R.id.hotelname);
        _password = findViewById(R.id.password);

    }

    public void registerUser(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterUser.class));
        finish();
    }

    public void onLoginClicked(View view){
        if (!validateUsername() | !validatePassword()) {
            return;
        } else {
            isUser();
        }
    }

    private void isUser() {

        username = _username.getEditText().getText().toString().trim();
        password = _password.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("customers");

        Query checkUser = reference.orderByChild("user").equalTo(username);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    _username.setErrorEnabled(false);
                    _username.setError(null);
                    String passwordFromDb = snapshot.child(username).child("password").getValue(String.class);
                    if(passwordFromDb.equals(password)){
                        _password.setError(null);
                        _password.setErrorEnabled(false);
                        Intent intent = new Intent(getApplicationContext(),AfterLogin.class);
                        intent.putExtra("username",username);
                        startActivity(intent);
                    }else{
                        _password.setError("Wrong password");
                        _password.requestFocus();
                    }
                }else{
                    _username.setError("No user found");
                    _username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private Boolean validateUsername() {
        String val = _username.getEditText().getText().toString();
        //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            _username.setError("Field cannot be empty");
            return false;
        } else {
            _username.setError(null);
            _username.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = _password.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            _password.setError("Field cannot be empty");
            return false;
        } else {
            _password.setError(null);
            _password.setErrorEnabled(false);
            return true;
        }
    }

    public void onForgotClicked(View view){
        startActivity(new Intent(getApplicationContext(),ForgotPasswordScreen.class));
    }

}