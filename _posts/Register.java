package com.example.planmytrip;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.service.voice.AlwaysOnHotwordDetector;
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

    TextInputLayout eHotelName, eEmail, ePassword, ePhoneNo, eRoomNo, ePrice, eLocation;
    Button eMarker, eRegister;
    String Lat = null, Long = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        eHotelName = findViewById(R.id.regHotelname);
        eEmail = findViewById(R.id.regEmail);
        ePassword = findViewById(R.id.regPass);
        ePhoneNo = findViewById(R.id.regPhnNo);
        eRoomNo = findViewById(R.id.regRoomNo);
        ePrice = findViewById(R.id.regPrice);
        eLocation = findViewById(R.id.regLocation);
        eMarker = findViewById(R.id.regAddMarker);
        eRegister = findViewById(R.id.regBtn);

        if (!isConnected(Register.this)) {
            showCustomDialog();
        }

        eMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(), MapsActivity.class), 999);
            }
        });
    }

    private void showCustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please connect to internet to proceed further")
                .setCancelable(false)
                .setPositiveButton("connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean isConnected(Register dashboard) {
        ConnectivityManager connectivityManager = (ConnectivityManager) dashboard.getSystemService(Context.CONNECTIVITY_SERVICE);


        NetworkInfo wificon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobilecon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wificon != null && wificon.isConnected()) || (mobilecon != null && mobilecon.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    private boolean validateHotelname() {
        String val = eHotelName.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            eHotelName.setError("Field cannot be empty");
            return false;
        } else if (val.length() >= 25) {
            eHotelName.setError("Hotel name too long");
            return false;
        } else {
            eHotelName.setError(null);
            eHotelName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail() {
        String val = eEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            eEmail.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            eEmail.setError("Invalid email address");
            return false;
        } else {
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
        } else if (val.length() != 10) {
            ePhoneNo.setError("Invalid Phone No");
            return false;
        } else {
            ePhoneNo.setError(null);
            ePhoneNo.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = ePassword.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            ePassword.setError("Field cannot be empty");
            return false;
        } else {
            ePassword.setError(null);
            ePassword.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateRooms() {
        String val = eRoomNo.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            eRoomNo.setError("Field cannot be empty");
            return false;
        } else {
            eRoomNo.setError(null);
            eRoomNo.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePrice() {
        String val = ePrice.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            ePrice.setError("Field cannot be empty");
            return false;
        } else {
            ePrice.setError(null);
            ePrice.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateLocation() {
        String val = eLocation.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            eLocation.setError("Field cannot be empty");
            return false;
        } else {
            eLocation.setError(null);
            eLocation.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateMarker() {
        if (Lat == null || Long == null) {
            Toast.makeText(getApplicationContext(), "Please add marker on the map", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public void registerButton(View view) {

        if (!validateHotelname() | !validateEmail() | !validatePhoneNo() | !validatePassword() | !validateRooms() | !validatePrice() | !validateLocation() | !validateMarker()) {
            return;
        }

        Intent intent = new Intent(getApplicationContext(), VerifyNo.class);
        String HotelName = eHotelName.getEditText().getText().toString().trim();
        String email = eEmail.getEditText().getText().toString();
        String password = ePassword.getEditText().getText().toString().trim();
        final String phone = ePhoneNo.getEditText().getText().toString().trim();
        String room = eRoomNo.getEditText().getText().toString().trim();
        String price = ePrice.getEditText().getText().toString().trim();
        String city = eLocation.getEditText().getText().toString().trim();

        intent.putExtra("hotelname", HotelName);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        intent.putExtra("phoneNo", phone);
        intent.putExtra("room", room);
        intent.putExtra("price", price);
        intent.putExtra("city", city);
        intent.putExtra("latitude", Lat);
        intent.putExtra("longitude", Long);

        startActivity(intent);
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999 && resultCode == RESULT_OK) {
            Lat = String.valueOf(data.getDoubleExtra("Latitude", 0.00));
            Long = String.valueOf(data.getDoubleExtra("Longitude", 0.00));
            Toast.makeText(getApplicationContext(),"Marker added successfully",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Dashboard.class));
        finish();
    }
}