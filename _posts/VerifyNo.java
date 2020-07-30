package com.example.customerplanmytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
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

    public static final String send_username = "com.example.planmytrip.send_username";
    public static final String send_email = "com.example.planmytrip.send_email";
    public static final String send_password = "com.example.planmytrip.send_password";
    public static final String send_phone = "com.example.planmytrip.send_phone";
    String _username, _email, _pasword, _phone, otpBySystem, whattodo;
    ProgressBar progressBar;
    Button btnVerify;
    EditText otp;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("customers");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_no);

        Intent intent = getIntent();
        _username = intent.getStringExtra(send_username);
        _email = intent.getStringExtra(send_email);
        _pasword = intent.getStringExtra(send_password);
        _phone = intent.getStringExtra(send_phone);
        whattodo = intent.getStringExtra("whattodo");

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        btnVerify = findViewById(R.id.verify_btn);
        otp = findViewById(R.id.otp);

        sendVerification(_phone);

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

    private void sendVerification(String phone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + _phone,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            otpBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpBySystem, code);
        signInbtCredentials(credential);
    }

    private void signInbtCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (whattodo.contains("updatePassword")) {
                                Intent intent = new Intent(getApplicationContext(),SetNewPassword.class);
                                intent.putExtra(send_username,_username);
                                startActivity(intent);
                            }else{
                                UserInfo user = new UserInfo(_username, _email, _pasword, _phone);
                                reference.child(_username).setValue(user);
                                Intent intent = new Intent(getApplicationContext(), OperationComplete.class);
                                intent.putExtra("activity", "verify");
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                });
    }
}