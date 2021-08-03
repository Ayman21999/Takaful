package com.graduatio.project.takaful.Actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.graduatio.project.takaful.R;

import java.util.HashMap;
import java.util.UUID;

public class PayInfoVisa extends AppCompatActivity {

    EditText cardnumber;
    EditText cvvCode;
    EditText start;
    EditText end;
    FirebaseFirestore firebaseFirestore;
    CollectionReference collectionReference;
    Button add;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_info);
        SetUpElement();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String card_txt = cardnumber.getText()+"";
                String cvvCode_txt = cvvCode.getText()+"";
                String date_txt = start.getText()+"";
                String end_txt = start.getText()+"";

                if (card_txt.length() < 12) {
                    Toast.makeText(PayInfoVisa.this, "Please the card number  must be 12 number", Toast.LENGTH_SHORT).show();

                } else if (card_txt.isEmpty()) {
                    Toast.makeText(PayInfoVisa.this, "Please write your card number ", Toast.LENGTH_SHORT).show();

                } else if (cvvCode_txt.length() < 4) {
                    Toast.makeText(PayInfoVisa.this, "Please the CVV Code must be 4 number", Toast.LENGTH_SHORT).show();

                } else if (cvvCode_txt.isEmpty()) {
                    Toast.makeText(PayInfoVisa.this, "Please write your CVV Code ", Toast.LENGTH_SHORT).show();

                } else if (date_txt.isEmpty()) {
                    Toast.makeText(PayInfoVisa.this, "Please set end date for your card ", Toast.LENGTH_SHORT).show();

                } else {
                    AddMethod(card_txt, cvvCode_txt, date_txt,end_txt);
                }

            }
        });

    }

    public void SetUpElement() {
        cardnumber = findViewById(R.id.cardnumber);
        cvvCode = findViewById(R.id.cvvcode);
        start = findViewById(R.id.start);
        end = findViewById(R.id.end);
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("Users").
                document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("UserPayMethod");
        add = findViewById(R.id.add);
        progressDialog = new ProgressDialog(this);
    }

    public void AddMethod(String cardnumber, String cvvCode, String date, String end) {
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("cardNumber",cardnumber);
        hashMap.put("cvvCode",cvvCode);
        hashMap.put("start",date);
        hashMap.put("end",end);
        hashMap.put("type",type);
        String id  = UUID.randomUUID().toString();
        hashMap.put("id",id);
        progressDialog.setMessage("Loading");
        progressDialog.show();

        collectionReference.document(id).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(PayInfoVisa.this, "Add Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PayInfoVisa.this ,EditPaymentMethod.class);
                progressDialog.dismiss();
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PayInfoVisa.this, "Error :" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

                Log.d("tt", e.getLocalizedMessage());
            }
        });

    }
}