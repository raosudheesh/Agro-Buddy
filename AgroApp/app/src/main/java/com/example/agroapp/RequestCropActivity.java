package com.example.agroapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RequestCropActivity extends AppCompatActivity {

    private EditText e2;
    private AutoCompleteTextView e1;
    private AutoCompleteTextView sp1,sp2;
    private ArrayAdapter<String> a1,a2;
    private ArrayAdapter<String> c1,c2,c3,c4,c5,c6;
    private Button submit;

    private DatabaseReference dbRef;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_crop);

        e1 = (AutoCompleteTextView) findViewById(R.id.requestcropname);
        e2 = (EditText) findViewById(R.id.requestcropquantity);
        submit = (Button) findViewById(R.id.requestcropsubmit);
        sp1 = (AutoCompleteTextView) findViewById(R.id.requestcroptype);
        sp2 = (AutoCompleteTextView) findViewById(R.id.requestcropunit);

        dbRef = FirebaseDatabase.getInstance().getReference().child("Requests");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        String buyerid,cropname,croptype,quantity,unit,requestdate,acceptstatus;
                        buyerid= dataSnapshot.getKey();
                        cropname=e1.getText().toString().trim();
                        croptype=sp1.getText().toString().trim();
                        quantity=e2.getText().toString().trim();
                        unit=sp2.getText().toString().trim();
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = new Date();
                        requestdate= dateFormat.format(date);
                        acceptstatus= "FALSE";

                        if(quantity.isEmpty()){
                            e2.setError("Quantity cannot be empty");
                            e2.requestFocus();
                        }
                        else {
                            CropRequest crop=new CropRequest(cropname,croptype,quantity,unit,buyerid,requestdate,acceptstatus);
                            dbRef.push().setValue(crop);
                            Toast.makeText(RequestCropActivity.this,"Request successful",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RequestCropActivity.this,RequestCropActivity.class));
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RequestCropActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onResume() {
        super.onResume();

        a1 = new ArrayAdapter<>(this,R.layout.dropdown_item,getResources().getStringArray(R.array.crop_type));
        sp1.setText(a1.getItem(0).toString(),false);
        sp1.setAdapter(a1);

        a2 = new ArrayAdapter<>(this,R.layout.dropdown_item,getResources().getStringArray(R.array.unit));
        sp2.setText(a2.getItem(0).toString(),false);
        sp2.setAdapter(a2);

        c1 = new ArrayAdapter<>(this,R.layout.dropdown_item,getResources().getStringArray(R.array.ctype1));
        c2 = new ArrayAdapter<>(this,R.layout.dropdown_item,getResources().getStringArray(R.array.ctype2));
        c3 = new ArrayAdapter<>(this,R.layout.dropdown_item,getResources().getStringArray(R.array.ctype3));
        c4 = new ArrayAdapter<>(this,R.layout.dropdown_item,getResources().getStringArray(R.array.ctype4));
        c5 = new ArrayAdapter<>(this,R.layout.dropdown_item,getResources().getStringArray(R.array.ctype5));
        c6 = new ArrayAdapter<>(this,R.layout.dropdown_item,getResources().getStringArray(R.array.ctype6));

        e1.setText("Select Item",false);
        e1.setAdapter(c1);

        sp1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                e1.setText("Select Item",false);
                if(a1.getItem(position).equals("FOOD GRAINS/CEREALS")){
                    e1.setAdapter(c1);
                }
                else if(a1.getItem(position).equals("OIL SEEDS")){
                    e1.setAdapter(c2);
                }
                else if(a1.getItem(position).equals("FRUITS")){
                    e1.setAdapter(c3);
                }
                else if(a1.getItem(position).equals("VEGETABLES")){
                    e1.setAdapter(c4);
                }
                else if(a1.getItem(position).equals("SPICES")){
                    e1.setAdapter(c5);
                }
                else {
                    e1.setAdapter(c6);
                }
            }
        });
    }
}