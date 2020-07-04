package com.example.planmytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.accessibilityservice.GestureDescription;
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

public class ForgotPasswordActivity extends AppCompatActivity {

    TextInputLayout txtHotel_name;
    Button next_btn;
    String hotel_name;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    String phoneNo_from_Database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        txtHotel_name = findViewById(R.id.forgot_password_hotel_name);
        next_btn = findViewById(R.id.forgot_password_nextbtn);

        if (!isConnected(ForgotPasswordActivity.this)) {
            showCustomDialog();
        }
    }

    public void onNextClicked(View view) {
        if(!validateHotelname()){
            return;
        }
        hotel_name = txtHotel_name.getEditText().getText().toString();
        Query checkUser = FirebaseDatabase.getInstance().getReference("users").orderByChild("hotelname").equalTo(hotel_name);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    txtHotel_name.setError(null);
                    txtHotel_name.setErrorEnabled(false);
                    phoneNo_from_Database = dataSnapshot.child(hotel_name).child("phoneNo").getValue(String.class);
                    Intent intent = new Intent(getApplicationContext(), VerifyNo.class);
                    intent.putExtra("phoneNo", phoneNo_from_Database);
                    intent.putExtra("hotelname",hotel_name);
                    intent.putExtra("whatToDo", "updatePassword");
                    startActivity(intent);
                } else {
                    txtHotel_name.setError("No such hotel exists");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private boolean validateHotelname() {
        String val = txtHotel_name.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            txtHotel_name.setError("Field cannot be empty");
            return false;
        } else if (val.length() >= 25) {
            txtHotel_name.setError("Hotel name too long");
            return false;
        } else {
            txtHotel_name.setError(null);
            txtHotel_name.setErrorEnabled(false);
            return true;
        }
    }

    public void onbackClicked(View view){
        startActivity(new Intent(getApplicationContext(),Dashboard.class));
        finish();
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

    private boolean isConnected(ForgotPasswordActivity dashboard) {
        ConnectivityManager connectivityManager = (ConnectivityManager) dashboard.getSystemService(Context.CONNECTIVITY_SERVICE);


        NetworkInfo wificon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobilecon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wificon != null && wificon.isConnected()) || (mobilecon != null && mobilecon.isConnected())) {
            return true;
        } else {
            return false;
        }
    }
}