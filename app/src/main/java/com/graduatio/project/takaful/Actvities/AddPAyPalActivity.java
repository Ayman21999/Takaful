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

public class AddPAyPalActivity extends AppCompatActivity {
    EditText email ,pass;
    Button add;
    FirebaseFirestore firebaseFirestore;
    CollectionReference collectionReference;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_p_ay_pal);
        SetUpElement();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_txt = email.getText()+"";
                String pass_txt = pass.getText()+"";

                if (email_txt.isEmpty()) {
                    Toast.makeText(AddPAyPalActivity.this, "Please write your Email ", Toast.LENGTH_SHORT).show();

                } else if (pass_txt.isEmpty()) {
                    Toast.makeText(AddPAyPalActivity.this, "Please write your password", Toast.LENGTH_SHORT).show();

                }else{
                    AddMethod(email_txt, pass_txt);

                }

            }
        });
    }

    public void SetUpElement() {
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("Users").
                document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("UserPayMethod");
        add = findViewById(R.id.add);
        progressDialog = new ProgressDialog(this);

    }

    public void AddMethod(String cardnumber, String cvvCode) {
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("email",cardnumber);
        hashMap.put("pass",cvvCode);
        hashMap.put("type",type);
        String id  = UUID.randomUUID().toString();
        hashMap.put("id",id);
        collectionReference.document(id).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddPAyPalActivity.this, "Add Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddPAyPalActivity.this ,EditPaymentMethod.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddPAyPalActivity.this, "Error :" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d("tt", e.getLocalizedMessage());
            }
        });

    }
}