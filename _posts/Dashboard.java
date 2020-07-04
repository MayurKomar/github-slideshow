package com.example.planmytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
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

    TextInputLayout hotelName, pass;
    private Button btnReg, loginBtn;
    public static final String send_Email = "com.example.planmytrip.send_Email";
    public static final String send_phone = "com.example.planmytrip.send_phone";
    public static final String send_price = "com.example.planmytrip.send_price";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnReg = findViewById(R.id.btnRegister);
        loginBtn = findViewById(R.id.btnLogin);
        hotelName = findViewById(R.id.hotelname);
        pass = findViewById(R.id.password);

        if (!isConnected(Dashboard.this)) {
            showCustomDialog();
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!validateEmail() | !validatePassword()) {
                    return;
                } else {
                    isUser();
                }
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, Register.class));
                finish();
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

    private boolean isConnected(Dashboard dashboard) {
        ConnectivityManager connectivityManager = (ConnectivityManager) dashboard.getSystemService(Context.CONNECTIVITY_SERVICE);


        NetworkInfo wificon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobilecon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wificon != null && wificon.isConnected()) || (mobilecon != null && mobilecon.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    private void isUser() {
        final String HotelName = hotelName.getEditText().getText().toString().trim();
        final String Password = pass.getEditText().getText().toString().trim();

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        Query checkUser = reference.orderByChild("hotelname").equalTo(HotelName);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    hotelName.setError(null);
                    hotelName.setErrorEnabled(false);
                    String Passdb = dataSnapshot.child(HotelName).child("password").getValue(String.class);
                    if (Passdb.equals(Password)) {
                        pass.setError(null);
                        pass.setErrorEnabled(false);
                        final Intent intent = new Intent(Dashboard.this, HotelProfile.class);
                        intent.putExtra("username", HotelName);
                        intent.putExtra("password", Password);
                        String eMailFromDb = dataSnapshot.child(HotelName).child("email").getValue(String.class);
                        String phoneFromDb = dataSnapshot.child(HotelName).child("phoneNo").getValue(String.class);
                        String priceFromDb = dataSnapshot.child(HotelName).child("price").getValue(String.class);
                        intent.putExtra(send_Email, eMailFromDb);
                        intent.putExtra(send_phone, phoneFromDb);
                        intent.putExtra(send_price, priceFromDb);
                        startActivity(intent);

                    } else {
                        pass.setError("Wrong Password");
                        pass.requestFocus();
                    }
                } else {
                    hotelName.setError("No such user found");
                    hotelName.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private Boolean validateEmail() {
        String val = hotelName.getEditText().getText().toString();
        //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            hotelName.setError("Field cannot be empty");
            return false;
        } else {
            hotelName.setError(null);
            hotelName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = pass.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            pass.setError("Field cannot be empty");
            return false;
        } else {
            pass.setError(null);
            pass.setErrorEnabled(false);
            return true;
        }
    }

    public void onForgotClicked(View view) {
        startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
        finish();
    }
}