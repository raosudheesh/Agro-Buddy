package com.example.agroapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private long backPressedTime;
    private Toast backToast;

    private ProgressBar progressBar;

    private EditText e1, e2, e3, e4, e5;
    private AutoCompleteTextView sp;
    private ArrayAdapter<String> a1;
    private RadioGroup rg;
    private RadioButton r1, r2;
    private Button registerbtn;
    private TextView alreadyuser;
    private TextInputLayout t1;

    private Member member;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        e1 = (EditText) findViewById(R.id.registeruser);
        e2 = (EditText) findViewById(R.id.registeremail);
        e3 = (EditText) findViewById(R.id.registermobile);
        e4 = (EditText) findViewById(R.id.registerpass);
        e5 = (EditText) findViewById(R.id.registeraddress);
        rg = (RadioGroup) findViewById(R.id.radiogroup);
        r1 = (RadioButton) findViewById(R.id.farmer);
        r2 = (RadioButton) findViewById(R.id.supplier);

        t1 = (TextInputLayout) findViewById(R.id.textInputLayout4);
        sp = (AutoCompleteTextView) findViewById(R.id.registerbuyertype);
        alreadyuser = (TextView) findViewById(R.id.alreadyuserlink);
        registerbtn = (Button) findViewById(R.id.registerbtn);

        progressBar = (ProgressBar) findViewById(R.id.registerprogress);
        progressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();

        t1.setVisibility(View.GONE);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.farmer) {
                    t1.setVisibility(View.GONE);
                } else {
                    t1.setVisibility(View.VISIBLE);
                }
            }
        });

        alreadyuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
            }
        });

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username, usertype, email, mobile, pass, address, buyertype;
                int selectedId = rg.getCheckedRadioButtonId();
                if (selectedId == r1.getId()) {
                    usertype = "FARMER";
                } else {
                    usertype = "BUYER";
                }
                username = e1.getText().toString().trim();
                email = e2.getText().toString().trim();
                mobile = e3.getText().toString();
                pass = e4.getText().toString().trim();
                address = e5.getText().toString().trim();
                buyertype = sp.getText().toString().trim();
                if (username.isEmpty()) {
                    e1.setError("Please enter your Full name");
                    e1.requestFocus();
                } else if (email.isEmpty()) {
                    e2.setError("Please enter EmailId");
                    e2.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    e2.setError("Please enter valid EmailId");
                    e2.requestFocus();
                } else if (mobile.isEmpty()) {
                    e3.setError("Please enter Mobile no.");
                    e3.requestFocus();
                } else if (mobile.length() != 10) {
                    e3.setError("Please enter valid Mobile no.");
                    e3.requestFocus();
                } else if (address.isEmpty()) {
                    e5.setError("Please enter Address");
                    e5.requestFocus();
                } else if (pass.isEmpty()) {
                    e4.setError("Please enter Password");
                    e4.requestFocus();
                } else if (username.isEmpty() && email.isEmpty() && mobile.isEmpty() && pass.isEmpty() && address.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Fields are empty!", Toast.LENGTH_SHORT).show();
                } else if (!(username.isEmpty() && email.isEmpty() && mobile.isEmpty() && pass.isEmpty() && address.isEmpty())) {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            if (usertype.equals("FARMER"))
                                member = new Member(username, email, mobile, address, usertype);
                            else
                                member = new Member(username, email, mobile, address, buyertype + " " + usertype);
                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(member).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "Registration Successful, please login.", Toast.LENGTH_SHORT).show();
                                    FirebaseAuth.getInstance().signOut();
                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(RegisterActivity.this, "Registration unsuccessful: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, "Registration unsuccessful: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onResume() {
        super.onResume();
        a1 = new ArrayAdapter<>(this, R.layout.dropdown_item, getResources().getStringArray(R.array.buyer_type));
        sp.setText(a1.getItem(0).toString(), false);
        sp.setAdapter(a1);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        finish();
    }
}