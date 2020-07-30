package com.example.planmytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.contentcapture.ContentCaptureCondition;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.data.DataBufferSafeParcelable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ApproveBookings extends AppCompatActivity {

    ArrayList<ViewBookings> arrayList;
    ViewBookings viewBookings;
    int arraySize;

    DatabaseReference reference;

    String _hotel_name_;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_bookings);

        recyclerView = findViewById(R.id.recyclerView);

        _hotel_name_ = getIntent().getStringExtra("hotel_name");

        reference = FirebaseDatabase.getInstance().getReference("users").child(_hotel_name_).child("Bookings");

        viewBookings = new ViewBookings();
        arrayList = new ArrayList<>();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    for(DataSnapshot ds :snapshot.getChildren()){
                        viewBookings = ds.getValue(ViewBookings.class);
                        arrayList.add(viewBookings);
                    }
                    arraySize = arrayList.size();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        MyAdapter myAdapter = new MyAdapter(arrayList,getApplicationContext());
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

        ArrayList<ViewBookings> arrayListMyAdapter;
        Context context;

        public MyAdapter(ArrayList<ViewBookings> arrayListMyAdapter,Context context) {
            this.arrayListMyAdapter = arrayListMyAdapter;
            this.context = context;
        }

        @NonNull
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.card_layout,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
            holder.usernameText.setText((CharSequence) arrayListMyAdapter.get(position));
        }

        @Override
        public int getItemCount() {
            return arrayListMyAdapter.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView usernameText;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                usernameText = itemView.findViewById(R.id.usernameText);
            }
        }
    }
}