package com.example.customerplanmytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetNewPassword extends AppCompatActivity {

    public static final String send_username = "com.example.planmytrip.send_username";
    String username,_new_password,_confirm_password;
    TextInputLayout new_password,confirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        new_password = findViewById(R.id.new_password);
        confirm_password = findViewById(R.id.confirm_password);
    }

    public void onSubmitClicked(View view){

        _new_password = new_password.getEditText().getText().toString().trim();
        _confirm_password = confirm_password.getEditText().getText().toString().trim();

        Intent intent = getIntent();
        username = intent.getStringExtra(send_username);

        if(!validateNewPassword() || !validateConfirmPassword()){
            return;
        }else {
            if(_new_password.equals(_confirm_password)) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("customers");
                reference.child(username).child("password").setValue(_new_password)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(getApplicationContext(),UpdatedPassword.class));
                                finish();
                            }
                        });
            }else{
                confirm_password.setError("Passwords do not match");
                confirm_password.requestFocus();
            }
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

    public void onBackClicked(View view){
        startActivity(new Intent(this,ForgotPasswordScreen.class));
        finish();
    }
}