
package com.example.agroapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private TextView t1, t2, t3, t4, t5, t6, t7;
    private EditText e1, e2, e3, e4;
    private Button b1, b2, b3, logout;

    ShimmerFrameLayout s1, s2;

    DatabaseReference dbref;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        s1 = (ShimmerFrameLayout) findViewById(R.id.psf1);
        s2 = (ShimmerFrameLayout) findViewById(R.id.psf2);

        t1 = (TextView) findViewById(R.id.ptname);
        t2 = (TextView) findViewById(R.id.ptype);
        e1 = (EditText) findViewById(R.id.pename);
        e1.setEnabled(false);
        e2 = (EditText) findViewById(R.id.pemail);
        e2.setEnabled(false);
        e3 = (EditText) findViewById(R.id.pmobile);
        e3.setEnabled(false);
        e4 = (EditText) findViewById(R.id.paddress);
        e4.setEnabled(false);
        b1 = (Button) findViewById(R.id.editprofilebtn);
        b2 = (Button) findViewById(R.id.updateprofilebtn);
        b2.setVisibility(View.GONE);
        b3 = (Button) findViewById(R.id.updatepassbtn);
        logout = (Button) findViewById(R.id.plogoutbtn);
        t3 = (TextView) findViewById(R.id.pearnings);
        t4 = (TextView) findViewById(R.id.ptradecount);
        t5 = (TextView) findViewById(R.id.ppayment);
        t6 = (TextView) findViewById(R.id.ptradetitle);
        t7 = (TextView) findViewById(R.id.prupee);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        dbref = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                t1.setText(snapshot.child("name").getValue(String.class));
                t2.setText(snapshot.child("usertype").getValue(String.class));
                e1.setText(snapshot.child("name").getValue(String.class));
                e2.setText(user.getEmail());
                e3.setText(snapshot.child("mobile").getValue(String.class));
                e4.setText(snapshot.child("address").getValue(String.class));
                String uid;
                if (snapshot.child("usertype").getValue(String.class).equals("FARMER")) {
                    uid = "farmerid";
                } else {
                    uid = "supplierid";
                }
                FirebaseDatabase.getInstance().getReference("Trades").orderByChild(uid).equalTo(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int total = 0;
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String amount = ds.child("amount").getValue(String.class);
                            total += Integer.parseInt(amount);
                        }
                        t3.setText(String.valueOf(total));
                        t4.setText(String.valueOf(dataSnapshot.getChildrenCount()));
                        t5.setText("Spendings");
                        t6.setText("Total Trades");
                        t7.setText("₹");
                        s1.stopShimmer();
                        s2.stopShimmer();
                        s1.setVisibility(View.GONE);
                        s2.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setEnabled(true);
                e3.setEnabled(true);
                e4.setEnabled(true);
                b1.setVisibility(View.GONE);
                b2.setVisibility(View.VISIBLE);
                b3.setVisibility(View.VISIBLE);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.setText(e1.getText().toString());
                dbref.child("name").setValue(e1.getText().toString());
                dbref.child("mobile").setValue(e3.getText().toString());
                dbref.child("address").setValue(e4.getText().toString());
                e1.setEnabled(false);
                e3.setEnabled(false);
                e4.setEnabled(false);
                b2.setVisibility(View.GONE);
                b1.setVisibility(View.VISIBLE);
                b3.setVisibility(View.VISIBLE);
                Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            }
        });


        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetPass = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setCancelable(false)
                        .setTitle("Reset Password?")
                        .setMessage("Enter the new password")
                        .setView(resetPass)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ///
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog psd = passwordResetDialog.create();
                psd.show();
                psd.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newpass = resetPass.getText().toString();
                        if (newpass.isEmpty()) {
                            resetPass.setError("Email cannot be empty");
                            resetPass.requestFocus();
                        } else {
                            user.updatePassword(newpass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ProfileActivity.this, "Password reset successful", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            psd.dismiss();
                        }
                    }
                });

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(ProfileActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                finishAffinity();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        s1.startShimmer();
        s2.startShimmer();
    }

    @Override
    protected void onPause() {
        s1.stopShimmer();
        s2.stopShimmer();
        super.onPause();
    }
}