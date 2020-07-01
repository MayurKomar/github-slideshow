package com.example.planmytrip;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    TextInputLayout euserName,eEmail,ePassword,ePhoneNo,eRoomNo,ePrice,eLocation;
    Button eMarker,eRegister;
    String Lat,Long;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        euserName = findViewById(R.id.regUsername);
        eEmail = findViewById(R.id.regEmail);
        ePassword = findViewById(R.id.regPass);
        ePhoneNo = findViewById(R.id.regPhnNo);
        eRoomNo = findViewById(R.id.regRoomNo);
        ePrice = findViewById(R.id.regPrice);
        eLocation = findViewById(R.id.regLocation);
        eMarker = findViewById(R.id.regAddMarker);
        eRegister = findViewById(R.id.regBtn);

        eMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(),MapsActivity.class),999);
            }
        });
    }

    private boolean validateUsername(){
        String val = euserName.getEditText().getText().toString().trim();
        if(val.isEmpty()){
            euserName.setError("Field cannot be empty");
            return false;
        }else if (val.length() >= 25) {
            euserName.setError("Username too long");
            return false;
        }
        else{
            euserName.setError(null);
            euserName.setErrorEnabled(false);
            return true;
        }
    }
        private Boolean validateEmail() {
            String val = eEmail.getEditText().getText().toString();
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

            if (val.isEmpty()) {
                eEmail.setError("Field cannot be empty");
                return false;
            }else if (!val.matches(emailPattern)) {
                eEmail.setError("Invalid email address");
                return false;
            }else {
                eEmail.setError(null);
                eEmail.setErrorEnabled(false);
                return true;
            }
        }

    private Boolean validatePhoneNo() {
        String val = ePhoneNo.getEditText().getText().toString();

        if (val.isEmpty()) {
            ePhoneNo.setError("Field cannot be empty");
            return false;
        }else if (val.length() != 10) {
            ePhoneNo.setError("Invalid Phone No");
            return false;
        }
        else {
            ePhoneNo.setError(null);
            ePhoneNo.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword(){
        String val = ePassword.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            ePassword.setError("Field cannot be empty");
            return false;
        }else {
            ePassword.setError(null);
            ePassword.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateRooms(){
        String val = eRoomNo.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            eRoomNo.setError("Field cannot be empty");
            return false;
        }else {
            eRoomNo.setError(null);
            eRoomNo.setErrorEnabled(false);
            return true;
        }
    }

    private  boolean validatePrice(){
        String val = ePrice.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            ePrice.setError("Field cannot be empty");
            return false;
        }else {
            ePrice.setError(null);
            ePrice.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateLocation(){
        String val = eLocation.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            eLocation.setError("Field cannot be empty");
            return false;
        }else {
            eLocation.setError(null);
            eLocation.setErrorEnabled(false);
            return true;
        }
    }

    public void registerButton(View view) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("users");

        if (!validateUsername() | !validateEmail() | !validatePhoneNo() | !validatePassword() | !validateRooms() | !validatePrice() | !validateLocation()) {
            return;
        }
        String userName = euserName.getEditText().getText().toString().trim();
        String email = eEmail.getEditText().getText().toString();
        String password = ePassword.getEditText().getText().toString().trim();
        final String phone = ePhoneNo.getEditText().getText().toString().trim();
        String room = eRoomNo.getEditText().getText().toString().trim();
        String price = ePrice.getEditText().getText().toString().trim();
        String city = eLocation.getEditText().getText().toString().trim();
        UserInfo user = new UserInfo(userName, email, password, phone, room, price,Lat,Long,city);

        reference.child(userName).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(Register.this,VerifyNo.class);
                    intent.putExtra("phoneNo",phone);
                    startActivity(intent);
                } else {
                    Toast.makeText(Register.this, "Failed to register. Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999 && resultCode == RESULT_OK) {
            Lat = String.valueOf(data.getDoubleExtra("Latitude", 0.00));
            Long = String.valueOf(data.getDoubleExtra("Longitude", 0.00));
        }
    }
}