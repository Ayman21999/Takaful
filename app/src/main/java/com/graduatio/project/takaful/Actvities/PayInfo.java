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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.graduatio.project.takaful.R;

import java.util.HashMap;
import java.util.UUID;

public class PayInfo extends AppCompatActivity {

    EditText cardnumber;
    EditText cvvCode;
    EditText date;
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
                String card_txt = cardnumber.getText().toString();
                String cvvCode_txt = cvvCode.getText().toString();
                String date_txt = date.getText().toString();

                if (card_txt.length() < 12) {
                    Toast.makeText(PayInfo.this, "Please the card number  must be 12 number", Toast.LENGTH_SHORT).show();

                } else if (card_txt.isEmpty()) {
                    Toast.makeText(PayInfo.this, "Please write your card number ", Toast.LENGTH_SHORT).show();

                } else if (cvvCode_txt.length() < 4) {
                    Toast.makeText(PayInfo.this, "Please the CVV Code must be 4 number", Toast.LENGTH_SHORT).show();

                } else if (cvvCode_txt.isEmpty()) {
                    Toast.makeText(PayInfo.this, "Please write your CVV Code ", Toast.LENGTH_SHORT).show();

                } else if (date_txt.isEmpty()) {
                    Toast.makeText(PayInfo.this, "Please set end date for your card ", Toast.LENGTH_SHORT).show();

                } else {
                    AddMethod(card_txt, cvvCode_txt, date_txt);
                }

            }
        });

    }

    public void SetUpElement() {
        cardnumber = findViewById(R.id.cardnumber);
        cvvCode = findViewById(R.id.cvvcode);
        date = findViewById(R.id.date);
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("Pay");
        add = findViewById(R.id.add);

    }

    public void AddMethod(String cardnumber, String cvvCode, String date) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("cardNumber",cardnumber);
        hashMap.put("cvvCode",cvvCode);
        hashMap.put("enddate",date);
        String id  = UUID.randomUUID().toString();
        hashMap.put("id",id);
        collectionReference.document(id).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(PayInfo.this, "Add Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PayInfo.this ,EditPaymentMethod.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PayInfo.this, "Error :" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d("tt", e.getLocalizedMessage());
            }
        });

    }
}