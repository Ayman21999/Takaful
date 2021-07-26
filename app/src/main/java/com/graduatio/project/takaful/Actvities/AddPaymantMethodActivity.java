package com.graduatio.project.takaful.Actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.graduatio.project.takaful.Model.PayMethod;
import com.graduatio.project.takaful.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AddPaymantMethodActivity extends AppCompatActivity {

    List<PayMethod>methods ;
    PayMethod payMethod ;
    FirebaseFirestore firebaseFirestore ;
    CollectionReference reference;
    Button visa_btn ,google_btn,paypal_btn;
    String typeMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_paymant_method);

        SetUpElement();
        ClickButtons();
        AddMethod();

    }


    public void SetUpElement(){
        methods = new ArrayList<>();
        payMethod = new PayMethod();
        firebaseFirestore = FirebaseFirestore.getInstance();
        reference = firebaseFirestore.collection("Pay");
        visa_btn = findViewById(R.id.visa);
        google_btn = findViewById(R.id.googlew);
        paypal_btn = findViewById(R.id.paypal);


    }
    public void AddMethod() {
        HashMap<String, Object> hashMap = new HashMap<>();

        String id = UUID.randomUUID().toString();
        hashMap.put("id", id);
        hashMap.put("cardNumber", "");
        hashMap.put("cvvCode", "");
        hashMap.put("enddate", "");
        hashMap.put("type", typeMethod);

        reference.document(id).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddPaymantMethodActivity.this, "Add Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("tt", e.getLocalizedMessage());
            }
        });

    }

    public void ClickButtons(){
        visa_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeMethod = "visa";
                Intent intent =new Intent(AddPaymantMethodActivity.this , PayInfoVisa.class);
                startActivity(intent);
            }
        });
        google_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeMethod = "googleW";
                Intent intent =new Intent(AddPaymantMethodActivity.this , AddGoogleWActivity2.class);
                startActivity(intent);
            }
        });
        paypal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeMethod = "pal";
                Intent intent =new Intent(AddPaymantMethodActivity.this , PayInfoVisa.class);
                startActivity(intent);
            }
        });
    }

}