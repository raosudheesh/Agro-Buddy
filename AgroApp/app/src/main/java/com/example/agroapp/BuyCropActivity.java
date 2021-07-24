package com.example.agroapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BuyCropActivity extends AppCompatActivity {

    private AutoCompleteTextView s1;
    private RecyclerView r1;

    private ArrayAdapter<String> arrayAdapter;

    private DatabaseReference dbRef;
    private BuyCropAdapter adapter;
    private ArrayList<CropBuy> list;

    private boolean click=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_crop);

        s1 = (AutoCompleteTextView) findViewById(R.id.buycropspinner);
        r1 = (RecyclerView) findViewById(R.id.buycroplist);
        dbRef = FirebaseDatabase.getInstance().getReference("Crops");

        r1.setHasFixedSize(true);
        r1.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new BuyCropAdapter(this,list);
        r1.setAdapter(adapter);

        s1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                click=true;
                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            String croptype = dataSnapshot.child("croptype").getValue(String.class);
                            String cropid = dataSnapshot.getKey();
                            String farmerid = dataSnapshot.child("farmerid").getValue(String.class);
                            String cropname = dataSnapshot.child("cropname").getValue(String.class);
                            String quantity = dataSnapshot.child("availablequantity").getValue(String.class);
                            String unit = dataSnapshot.child("unit").getValue(String.class);
                            String price = dataSnapshot.child("price").getValue(String.class);
                            String listdate = dataSnapshot.child("selldate").getValue(String.class);
                            if(croptype.equals(arrayAdapter.getItem(position)) && Float.parseFloat(quantity)>0) {
                                CropBuy crop = new CropBuy(cropname, quantity, unit, price, cropid, farmerid,listdate);
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
        });

        if(click==false){
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        String cropid = dataSnapshot.getKey();
                        String farmerid = dataSnapshot.child("farmerid").getValue(String.class);
                        String cropname = dataSnapshot.child("cropname").getValue(String.class);
                        String quantity = dataSnapshot.child("availablequantity").getValue(String.class);
                        String unit = dataSnapshot.child("unit").getValue(String.class);
                        String price = dataSnapshot.child("price").getValue(String.class);
                        String listdate = dataSnapshot.child("selldate").getValue(String.class);
                        CropBuy crop = new CropBuy(cropname, quantity, unit, price, cropid, farmerid,listdate);
                        list.add(crop);
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onResume() {
        super.onResume();
        arrayAdapter = new ArrayAdapter<>(this,R.layout.dropdown_item,getResources().getStringArray(R.array.crop_type));
        s1.setAdapter(arrayAdapter);
    }

}