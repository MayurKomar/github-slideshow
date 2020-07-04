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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class setNewPassword extends AppCompatActivity {

    TextInputLayout new_password, confirm_password;
    String new_pass, confirm_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        new_password = findViewById(R.id.new_password);
        confirm_password = findViewById(R.id.confirm_password);
        if (!isConnected(setNewPassword.this)) {
            showCustomDialog();
        }
    }


    public void onSubmitClicked(View view) {
        if (!validateNewPassword() | !validateConfirmPassword()) {
            return;
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        Intent intent = getIntent();
        String _hotel_name = intent.getStringExtra("hotelname");
        new_pass = new_password.getEditText().getText().toString();
        confirm_pass = confirm_password.getEditText().getText().toString();
        if (new_pass.equals(confirm_pass)) {
            confirm_password.setError(null);
            confirm_password.setErrorEnabled(false);
            reference.child(_hotel_name).child("password").setValue(new_pass);
            startActivity(new Intent(getApplicationContext(), UpdatedPassword.class));
            finish();
        } else {
            confirm_password.setError("Password does not match");
            confirm_password.requestFocus();
        }
    }

    private boolean validateNewPassword() {
        String val = new_password.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            new_password.setError("Field cannot be empty");
            return false;
        } else {
            new_password.setError(null);
            new_password.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        String val = confirm_password.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            confirm_password.setError("Field cannot be empty");
            return false;
        } else {
            confirm_password.setError(null);
            confirm_password.setErrorEnabled(false);
            return true;
        }
    }


    public void onBackClicked() {
        startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
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

    private boolean isConnected(setNewPassword dashboard) {
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