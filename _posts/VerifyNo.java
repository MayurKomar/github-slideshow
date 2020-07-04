
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerifyNo extends AppCompatActivity {

    Button btnVerify;
    EditText otp;
    ProgressBar progressBar;
    String otpsentbysystem;
    String hotel_name, e_mail, password, phoneNumber, room, price, lat, longi, city, whatToDo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_no);

        btnVerify = findViewById(R.id.verify_btn);
        otp = findViewById(R.id.otp);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        Intent intent = getIntent();
        hotel_name = intent.getStringExtra("hotelname");
        e_mail = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        phoneNumber = intent.getStringExtra("phoneNo");
        room = intent.getStringExtra("room");
        price = intent.getStringExtra("price");
        city = intent.getStringExtra("city");
        lat = intent.getStringExtra("latitude");
        longi = intent.getStringExtra("longitude");
        whatToDo = intent.getStringExtra("whatToDo");

        if (!isConnected(VerifyNo.this)) {
            showCustomDialog();
        }

        sendVerification(phoneNumber);

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = otp.getText().toString();
                if (code.isEmpty() | code.length() < 6) {
                    otp.setError("Wrong OTP");
                    otp.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        });
    }

    private void sendVerification(String phoneNo) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phoneNo,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            otpsentbysystem = s;

        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyNo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpsentbysystem, code);
        signInbyCresentials(credential);
    }

    private void signInbyCresentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (whatToDo == null) {
                                registerUser();
                            } else{
                                updatePassword();
                            }
                        } else {
                            Toast.makeText(VerifyNo.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void registerUser() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        UserInfo user = new UserInfo(hotel_name, e_mail, password, phoneNumber, room, price, lat, longi, city);
        reference.child(hotel_name).setValue(user);
        Toast.makeText(getApplicationContext(), "user registration successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), Dashboard.class));
        finish();
    }

    private void updatePhoneNo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(hotel_name).child("phoneNo").setValue(phoneNumber);
        Intent intent = new Intent(getApplicationContext(), HotelProfile.class);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void updatePassword() {
        if(whatToDo.contains("updatePhone")){
            updatePhoneNo();
            return;
        }
        Intent intent = new Intent(getApplicationContext(), setNewPassword.class);
        intent.putExtra("hotelname", hotel_name);
        startActivity(intent);
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

    private boolean isConnected(VerifyNo dashboard) {
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