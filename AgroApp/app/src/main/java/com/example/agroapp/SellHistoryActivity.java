package com.example.agroapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SellHistoryActivity extends AppCompatActivity {

    RecyclerView r1;
    DatabaseReference dbRef;

    SellHistoryAdapter adapter;
    ArrayList<SellHistory> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_history);

        r1 = (RecyclerView) findViewById(R.id.sellhistorylist);
        dbRef = FirebaseDatabase.getInstance().getReference("Crops");

        r1.setHasFixedSize(true);
        r1.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new SellHistoryAdapter(this,list);
        r1.setAdapter(adapter);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String fid = dataSnapshot.child("farmerid").getValue(String.class);
                    String cropname = dataSnapshot.child("cropname").getValue(String.class);
                    String selldate = dataSnapshot.child("selldate").getValue(String.class);
                    String quantity = dataSnapshot.child("totalquantity").getValue(String.class)+" "+dataSnapshot.child("unit").getValue(String.class);
                    String price = dataSnapshot.child("price").getValue(String.class);
                    if(fid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        SellHistory crop = new SellHistory(cropname, quantity, price, selldate);
                        list.add(crop);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                ///
            }
        });
    }
}