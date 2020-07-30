package com.example.customerplanmytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.util.ArrayList;

public class onHotelCardClicked extends AppCompatActivity {

    ImageView imageView;
    TextView _hotelnametext,phoneNo,city,rooms,price;
    Button hotellocation,bookRoom;
    String hotelNamefromIntent,phoneNofromIntent,cityfromIntent,roomNofromIntent,priceFromIntent,imageUrlfromIntent,usernameFromIntent;
    Double LatfromIntent,LongfromIntent;
    DatabaseReference hotelRefernce;
    int counter = 1,arraysize;
    ArrayList<BookingClass> arrayList;
    BookingClass book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_hotel_card_clicked);

        hotelRefernce = FirebaseDatabase.getInstance().getReference("users");

        imageView = findViewById(R.id.hotelImage);
        _hotelnametext = findViewById(R.id.hotelNametext);
        phoneNo = findViewById(R.id.hotelphoneNo);
        city = findViewById(R.id.hotelcity);
        rooms = findViewById(R.id.hotelrooms);
        price = findViewById(R.id.hotelroomprice);
        hotellocation = findViewById(R.id.LocationButton);
        bookRoom = findViewById(R.id.bookbutton);

        arrayList = new ArrayList<>();

        book = new BookingClass();

        setData();

        hotellocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                intent.putExtra("Lat",LatfromIntent);
                intent.putExtra("Long",LongfromIntent);
                startActivity(intent);
            }
        });

        bookRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookRoomInSelectedHotel();
            }
        });
    }

    private void bookRoomInSelectedHotel(){
        final DatabaseReference bookingReference = hotelRefernce.child(hotelNamefromIntent).child("Bookings");
        final int[] nextCounter = new int[1];
        //final int[] incrementedCounter = new int[1];
        final int[] firstCounter = new int[1];
        bookingReference.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int incrementedCounter;
                if (snapshot.hasChildren()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        book = ds.getValue(BookingClass.class);
                        arrayList.add(book);
                    }
                    arraysize = arrayList.size();
                    book = arrayList.get(0);
                    firstCounter[0] = book.getCounter();
                    for (int i = 0; i < arraysize; i++) {
                        book = arrayList.get(i);
                        nextCounter[0] = book.getCounter();
                        if (firstCounter[0] < nextCounter[0]) {
                            firstCounter[0] = nextCounter[0];
                        }
                    }
                    incrementedCounter = firstCounter[0] +1;
                    BookingClass bookClass = new BookingClass(incrementedCounter,usernameFromIntent);
                    bookingReference.child("Counter "+incrementedCounter).setValue(bookClass);
                    Toast.makeText(getApplicationContext(),"Request has been sent to the hotel and is waiting for approval",Toast.LENGTH_SHORT).show();
                }
                else{
                    BookingClass bookingClass = new BookingClass(counter,usernameFromIntent);
                    bookingReference.child("Counter "+counter).setValue(bookingClass);
                    Toast.makeText(getApplicationContext(),"Request has been sent to the hotel and is waiting for approval",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setData() {

        Intent intent = getIntent();
        hotelNamefromIntent = intent.getStringExtra("hotelname");
        phoneNofromIntent = intent.getStringExtra("phone");
        cityfromIntent = intent.getStringExtra("city");
        roomNofromIntent = intent.getStringExtra("room");
        priceFromIntent = intent.getStringExtra("price");
        imageUrlfromIntent = intent.getStringExtra("imageUrl");
        LatfromIntent = intent.getDoubleExtra("lat",0.00);
        LongfromIntent = intent.getDoubleExtra("long",0.00);
        usernameFromIntent = intent.getStringExtra("Username");

        _hotelnametext.setText(hotelNamefromIntent);
        phoneNo.setText(phoneNofromIntent);
        city.setText(cityfromIntent);
        rooms.setText(roomNofromIntent);
        price.setText(priceFromIntent);
        Glide.with(getApplicationContext()).load(imageUrlfromIntent).into(imageView);
    }
}