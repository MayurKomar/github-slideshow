package com.example.customerplanmytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AfterLogin extends AppCompatActivity {

    private TextInputLayout eSearchField;
    private Button eSearchButton;

    private String loggedinSuername;

    private RecyclerView recyclerView;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        eSearchField = findViewById(R.id.destination_text);
        eSearchButton = findViewById(R.id.Search_Btn);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        loggedinSuername = intent.getStringExtra("username");

        eSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = eSearchField.getEditText().getText().toString();

                firebaseHotelSearch(searchText);
            }
        });
    }

    private void firebaseHotelSearch(String searchText) {

        final Query firebaseQuery = databaseReference.orderByChild("city").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerAdapter<HotelInfo, HotelsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<HotelInfo, HotelsViewHolder>(
                HotelInfo.class,
                R.layout.card_layout,
                HotelsViewHolder.class,
                firebaseQuery
        ) {
            @Override
            protected void populateViewHolder(HotelsViewHolder hotelsViewHolder, final HotelInfo hotelInfo, int i) {
                final String hotelName = hotelInfo.getHotelname();
                final String city = hotelInfo.getCity();
                final String phone = hotelInfo.getPhoneNo();
                final String imageUrl = hotelInfo.getImageUrl();
                final String room = hotelInfo.getRoomNo();
                final String price = hotelInfo.getPrice();
                final String LatfromLogin = hotelInfo.getLatitude();
                final String LongfromLogin = hotelInfo.getLongitude();

                hotelsViewHolder.setDetails(getApplicationContext(),hotelName,hotelInfo.getCity(),hotelInfo.getPhoneNo(),hotelInfo.getImageUrl());
                hotelsViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(),onHotelCardClicked.class);
                        intent.putExtra("hotelname",hotelName);
                        intent.putExtra("city",city);
                        intent.putExtra("phone",phone);
                        intent.putExtra("imageUrl",imageUrl);
                        intent.putExtra("room",room);
                        intent.putExtra("price",price);
                        intent.putExtra("lat", Double.parseDouble(LatfromLogin));
                        intent.putExtra("long", Double.parseDouble(LongfromLogin));
                        intent.putExtra("Username",loggedinSuername);

                        startActivity(intent);
                    }
                });
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class HotelsViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public HotelsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setDetails(final Context context, final String Hotelname, String city, String phone,String ImageUrl){

            TextView hotel_name = mView.findViewById(R.id.textHotel_name);
            TextView hotel_phone = mView.findViewById(R.id.textHotel_phone);
            TextView hotel_city = mView.findViewById(R.id.textHotel_city);
            ImageView hotel_image = mView.findViewById(R.id.hotel_image);

            hotel_name.setText(Hotelname);
            hotel_phone.setText(phone);
            hotel_city.setText(city);
            Glide.with(context).load(ImageUrl).into(hotel_image);

        }
    }
}