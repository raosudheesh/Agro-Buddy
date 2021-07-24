package com.example.agroapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RequestListAdapter extends RecyclerView.Adapter<RequestListAdapter.MyViewHolder> {

    Context context;
    ArrayList<CropAccept> list;
    DatabaseReference dbref;

    public RequestListAdapter(Context context, ArrayList<CropAccept> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.request_crop_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CropAccept crop = list.get(getItemCount()-position-1);
        holder.t1.setText(crop.getCropname());
        holder.t2.setText(crop.getCroptype());
        holder.t3.setText(crop.getQuantity());
        dbref= FirebaseDatabase.getInstance().getReference("Users");
        dbref.child(crop.getBuyerid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                holder.t4.setText(dataSnapshot.child("name").getValue(String.class));
            }
        });
        holder.t5.setText(crop.getRequestdate());
        holder.acceptbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edt = new EditText(v.getContext());
                edt.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                AlertDialog.Builder requestalertbuilder = new AlertDialog.Builder(v.getContext());
                requestalertbuilder.setCancelable(false)
                        .setTitle("Accept "+crop.getCropname()+" Request")
                        .setMessage("Enter the Amount (in ₹):")
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

                AlertDialog requestalert= requestalertbuilder.create();
                requestalert.show();
                requestalert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String amount = edt.getText().toString();
                        if(amount.isEmpty()){
                            edt.setError("Quantity cannot be empty");
                            edt.requestFocus();
                        }
                        else if(Integer.parseInt(amount)<=0){
                            edt.setError("Amount must be greater than zero");
                            edt.requestFocus();
                        }
                        else if(!(amount.isEmpty()&&Integer.parseInt(amount)<=0)){
                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    FirebaseDatabase.getInstance().getReference("Requests").child(crop.getRequestid()).child("acceptstatus").setValue("TRUE");

                                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Trades");

                                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                    Date date = new Date();
                                    String tradedate= dateFormat.format(date);
                                    String farmerid= dataSnapshot.getKey();
                                    CropTrade cropTrade = new CropTrade(crop.getCropname(),crop.getQuantity(),amount,farmerid,crop.getBuyerid(),tradedate);
                                    dbRef.push().setValue(cropTrade);
                                    requestalert.dismiss();
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

        TextView t1,t2,t3,t4,t5;
        Button acceptbtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            t1=itemView.findViewById(R.id.requestcropname1);
            t2=itemView.findViewById(R.id.requestcroptype1);
            t3=itemView.findViewById(R.id.requestcropquantity1);
            t4=itemView.findViewById(R.id.requestbuyername);
            t5=itemView.findViewById(R.id.requestcropdate1);
            acceptbtn=itemView.findViewById(R.id.acceptcropbtn);
        }
    }

}
