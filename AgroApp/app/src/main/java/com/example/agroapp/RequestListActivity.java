package com.example.agroapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestListActivity extends AppCompatActivity {

    private AutoCompleteTextView s1;
    private RecyclerView r1;

    private ArrayAdapter<String> arrayAdapter;

    private DatabaseReference dbRef;
    private RequestListAdapter adapter;
    private ArrayList<CropAccept> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);

        r1 = (RecyclerView) findViewById(R.id.requestcroplist);
        dbRef = FirebaseDatabase.getInstance().getReference("Requests");

        r1.setHasFixedSize(true);
        r1.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new RequestListAdapter(this,list);
        r1.setAdapter(adapter);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    String requestid = dataSnapshot.getKey();
                    String croptype = dataSnapshot.child("croptype").getValue(String.class);
                    String cropname = dataSnapshot.child("cropname").getValue(String.class);
                    String quantity = dataSnapshot.child("quantity").getValue(String.class)+" "+dataSnapshot.child("unit").getValue(String.class);
                    String buyerid = dataSnapshot.child("buyerid").getValue(String.class);
                    String requestdate = dataSnapshot.child("requestdate").getValue(String.class);
                    String acceptstatus = dataSnapshot.child("acceptstatus").getValue(String.class);
                    if(acceptstatus.equals("FALSE")) {
                        CropAccept crop = new CropAccept(requestid, cropname, croptype, quantity, buyerid, requestdate, acceptstatus);
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