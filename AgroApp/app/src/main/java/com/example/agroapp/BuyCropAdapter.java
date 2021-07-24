package com.example.agroapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class BuyCropAdapter extends RecyclerView.Adapter<BuyCropAdapter.MyViewHolder> {

    Context context;
    ArrayList<CropBuy> list;
    DatabaseReference dbref;

    public BuyCropAdapter(Context context, ArrayList<CropBuy> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.buy_crop_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CropBuy crop = list.get(getItemCount()-position-1);
        holder.t1.setText(crop.getCropname());
        holder.t2.setText(crop.getQuantity()+" "+crop.getUnit());
        holder.t3.setText(crop.getPrice());
        holder.t6.setText("Cost per ("+crop.getUnit()+")");
        dbref= FirebaseDatabase.getInstance().getReference("Users");
        dbref.child(crop.getFarmerid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                holder.t4.setText(dataSnapshot.child("name").getValue(String.class));
            }
        });
        holder.t5.setText(crop.getSelldate());
        holder.buybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edt = new EditText(v.getContext());
                edt.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                AlertDialog.Builder buyalertbuilder = new AlertDialog.Builder(v.getContext());
                buyalertbuilder.setCancelable(false)
                        .setTitle("Buy "+crop.getCropname())
                        .setMessage("Enter the quantity (in "+crop.getUnit()+"):")
                        .setView(edt)
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ///
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog buyalert= buyalertbuilder.create();
                buyalert.show();
                buyalert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String qty = edt.getText().toString();
                        if(qty.isEmpty()){
                            edt.setError("Quantity cannot be empty");
                            edt.requestFocus();
                        }
                        else if(Float.parseFloat(qty)<=0){
                            edt.setError("Quantity must be greater than zero");
                            edt.requestFocus();
                        }
                        else if(Float.parseFloat(qty)>Float.parseFloat(crop.getQuantity())){
                            edt.setError("Please check available quantity");
                            edt.requestFocus();
                        }
                        else if(!(Float.parseFloat(qty)<=0&&Float.parseFloat(qty)>Float.parseFloat(crop.getQuantity()))){

                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    String newqty = String.valueOf(Float.parseFloat(crop.getQuantity()) - Float.parseFloat(qty));
                                    FirebaseDatabase.getInstance().getReference("Crops").child(crop.getCropid()).child("availablequantity").setValue(newqty);

                                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Trades");
                                    String amount = String.valueOf(Math.round(Float.parseFloat(qty)*Float.parseFloat(crop.getPrice())));
                                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                    Date date = new Date();
                                    String tradedate= dateFormat.format(date);
                                    String supplierid= dataSnapshot.getKey();

                                    CropTrade cropTrade = new CropTrade(crop.getCropname(),qty+" "+crop.getUnit(),amount,crop.getFarmerid(),supplierid,tradedate);
                                    dbRef.push().setValue(cropTrade);
                                    buyalert.dismiss();
                                    successdialog();
                                }

                                private void successdialog() {
                                    Dialog dialog = new Dialog(context);
                                    dialog.setContentView(R.layout.success_alert);
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    Button b1= dialog.findViewById(R.id.alertbtn);
                                    b1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.show();
                                }
                            });
                        }
                        else {
                            Toast.makeText(context,"Error Occurred!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView t1,t2,t3,t4,t5,t6;
        Button buybtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.buycropname);
            t2 = itemView.findViewById(R.id.buycropquantity);
            t3 = itemView.findViewById(R.id.buycropprice);
            t4 = itemView.findViewById(R.id.buycropcontact);
            t5 = itemView.findViewById(R.id.buycropdate);
            t6 = itemView.findViewById(R.id.buycropunit);
            buybtn = itemView.findViewById(R.id.buycropbtn);


        }
    }

}
