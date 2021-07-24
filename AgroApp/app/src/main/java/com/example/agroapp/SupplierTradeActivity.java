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

public class SupplierTradeActivity extends AppCompatActivity {

    RecyclerView r1;

    private DatabaseReference dbRef;
    private SupplierTradeAdapter adapter;
    private ArrayList<CropTrade> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_trade);

        r1 = (RecyclerView) findViewById(R.id.str);
        dbRef = FirebaseDatabase.getInstance().getReference("Trades");

        r1.setHasFixedSize(true);
        r1.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new SupplierTradeAdapter(this,list);
        r1.setAdapter(adapter);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String farmerid = dataSnapshot.child("farmerid").getValue(String.class);
                    String supplierid = dataSnapshot.child("supplierid").getValue(String.class);
                    String cropname = dataSnapshot.child("cropname").getValue(String.class);
                    String quantity = dataSnapshot.child("quantity").getValue(String.class);
                    String amount = dataSnapshot.child("amount").getValue(String.class);
                    String date = dataSnapshot.child("tradedate").getValue(String.class);

                    if(supplierid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        CropTrade crop = new CropTrade(cropname, quantity, amount, farmerid, supplierid,date);
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