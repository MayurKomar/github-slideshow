package com.example.planmytrip;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class HotelProfile extends AppCompatActivity {

    TextInputLayout email,password,phone,price;
    String HOTEL_NAME,EMAIL,PASSWORD,PHONE,PRICE;
    ImageView hotelImage;
    TextView Name_hotel;

    DatabaseReference reference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_profile);

        reference = FirebaseDatabase.getInstance().getReference("users");
        storageReference = FirebaseStorage.getInstance().getReference("Hotel_Images");

        HOTEL_NAME = getIntent().getStringExtra("username");

        Intent intent = new Intent (this, HotelService.class);
        intent.putExtra("HotelName",HOTEL_NAME);
        startService(intent);

        email = findViewById(R.id.profile_email);
        password = findViewById(R.id.profile_password);
        phone = findViewById(R.id.profile_phone);
        price = findViewById(R.id.profile_price);
        Name_hotel = findViewById(R.id.main_name);
        hotelImage = findViewById(R.id.hotelImageView);

        if (!isConnected(this)) {
            showCustomDialog();
        }
        showUserData();
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

    private boolean isConnected(HotelProfile dashboard) {
        ConnectivityManager connectivityManager = (ConnectivityManager) dashboard.getSystemService(Context.CONNECTIVITY_SERVICE);


        NetworkInfo wificon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobilecon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wificon != null && wificon.isConnected()) || (mobilecon != null && mobilecon.isConnected())) {
            return true;
        } else {
            return false;
        }
    }

    private void showUserData() {

        Intent intent = getIntent();
        HOTEL_NAME = intent.getStringExtra("username");
        EMAIL = intent.getStringExtra(Dashboard.send_Email);
        PASSWORD = intent.getStringExtra("password");
        PHONE = intent.getStringExtra(Dashboard.send_phone);
        PRICE = intent.getStringExtra(Dashboard.send_price);

        email.getEditText().setText(EMAIL);
        password.getEditText().setText(PASSWORD);
        phone.getEditText().setText(PHONE);
        price.getEditText().setText(PRICE);
        Name_hotel.setText(HOTEL_NAME);
        StorageReference imageRef = storageReference.child(HOTEL_NAME+".jpg");
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(hotelImage);
            }
        });
    }

    public void updateInfo(View view){
         if(isEmailChanged() | isPasswordChanged() | isPhoneChanged() | isPriceChanged()){
             return;
         }
         else{
             Toast.makeText(this,"Data not changed",Toast.LENGTH_SHORT).show();
         }
    }


    private boolean isEmailChanged() {

        if(!EMAIL.equals(email.getEditText().getText().toString().trim())){
            reference.child(HOTEL_NAME).child("email").setValue(email.getEditText().getText().toString().trim());
            Toast.makeText(this,"Email changed successfully",Toast.LENGTH_SHORT).show();
            return true;
        }else{
            return false;
        }

    }


    private boolean isPasswordChanged(){

        if(!PASSWORD.equals(password.getEditText().getText().toString().trim())){
            reference.child(HOTEL_NAME).child("password").setValue(password.getEditText().getText().toString().trim());
            Toast.makeText(this,"Password changed successfully",Toast.LENGTH_SHORT).show();
            return true;
        }else{
            return false;
        }

    }

    private boolean isPhoneChanged(){
        String _phone = phone.getEditText().getText().toString().trim();
        if(!PHONE.equals(_phone)){
            Intent intent = new Intent(getApplicationContext(),VerifyNo.class);
            intent.putExtra("phoneNo",_phone);
            intent.putExtra("hotelname",HOTEL_NAME);
            intent.putExtra("whatToDo","updatePhone");
            startActivityForResult(intent,1);
            return true;
        }else{
            return false;
        }

    }

    private boolean isPriceChanged(){

    if(!PRICE.equals(price.getEditText().getText().toString().trim())){
        reference.child(HOTEL_NAME).child("price").setValue(price.getEditText().getText().toString().trim());
        Toast.makeText(this,"Price changed successfully",Toast.LENGTH_SHORT).show();
        return true;
    }else{
        return false;
    }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this,"Phone Number changed successfully",Toast.LENGTH_SHORT).show();
    }

    public void viewBookingClicked(View view) {
        Intent intent = new Intent(this,ApproveBookings.class);
        intent.putExtra("hotel_name",HOTEL_NAME);
        startActivity(intent);
    }
}