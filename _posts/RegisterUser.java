package com.example.customerplanmytrip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity {

    TextInputLayout _username, _email, _password, _phoneNo;
    String username, email, password, phoneNo;
    public static final String send_username = "com.example.planmytrip.send_username";
    public static final String send_email = "com.example.planmytrip.send_email";
    public static final String send_password = "com.example.planmytrip.send_password";
    public static final String send_phone = "com.example.planmytrip.send_phone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        _username = findViewById(R.id.regUsername);
        _email = findViewById(R.id.regEmail);
        _password = findViewById(R.id.regPass);
        _phoneNo = findViewById(R.id.regPhnNo);

    }

    public void registerButton(View view) {
        if (!validateUsername() | !validateEmail() | !validatePassword() | !validatePhoneNo()) {
            return;
        }

        username = _username.getEditText().getText().toString().trim();
        email = _email.getEditText().getText().toString().trim();
        password = _password.getEditText().getText().toString().trim();
        phoneNo = _phoneNo.getEditText().getText().toString().trim();

        Intent intent = new Intent(getApplicationContext(), VerifyNo.class);
        intent.putExtra(send_username, username);
        intent.putExtra(send_email, email);
        intent.putExtra(send_password, password);
        intent.putExtra(send_phone, phoneNo);
        startActivity(intent);
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

    private Boolean validateEmail() {
        String val = _email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            _email.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            _email.setError("Invalid email address");
            return false;
        } else {
            _email.setError(null);
            _email.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePhoneNo() {
        String val = _phoneNo.getEditText().getText().toString();

        if (val.isEmpty()) {
            _phoneNo.setError("Field cannot be empty");
            return false;
        } else if (val.length() != 10) {
            _phoneNo.setError("Invalid Phone No");
            return false;
        } else {
            _phoneNo.setError(null);
            _phoneNo.setErrorEnabled(false);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,LoginScreen.class));
        finish();
    }
}