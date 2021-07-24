package com.example.agroapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class FarmerTradeAdapter extends RecyclerView.Adapter<FarmerTradeAdapter.MyViewHolder>{

    Context context;
    ArrayList<CropTrade> list;
    DatabaseReference dbref;

    public FarmerTradeAdapter(Context context, ArrayList<CropTrade> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.farmer_trade_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CropTrade crop = list.get(getItemCount()-position-1);
        dbref= FirebaseDatabase.getInstance().getReference("Users");
        holder.t1.setText(crop.getCropname());
        holder.t2.setText(crop.getQuantity());
        holder.t3.setText(crop.getAmount());
        holder.t5.setText(crop.getTradedate());
        dbref.child(crop.getSupplierid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                holder.t4.setText(dataSnapshot.child("name").getValue(String.class)+" - "+dataSnapshot.child("mobile").getValue(String.class));
                holder.t6.setText(dataSnapshot.child("address").getValue(String.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView t1,t2,t3,t4,t5,t6;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            t1=itemView.findViewById(R.id.ftcn);
            t2=itemView.findViewById(R.id.ftq);
            t3=itemView.findViewById(R.id.ftp);
            t4=itemView.findViewById(R.id.ftb);
            t5=itemView.findViewById(R.id.ftd);
            t6=itemView.findViewById(R.id.ftadr);
        }
    }
}
