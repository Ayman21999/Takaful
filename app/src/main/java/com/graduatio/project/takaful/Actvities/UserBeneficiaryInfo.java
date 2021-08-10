package com.graduatio.project.takaful.Actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.graduatio.project.takaful.R;

public class UserBeneficiaryInfo extends AppCompatActivity {
    Button create;
    EditText salay, age, job, status, familynumber, identiy;

    String id;
    CollectionReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_beneficiary_info);
        setUpElement();


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String identity = identiy.getText().toString();
                String age_txt = age.getText().toString();
                String familynum_txt = familynumber.getText().toString();
                String salary_txt = salay.getText().toString();
                String job_txt = job.getText().toString();
                String status_txt = status.getText().toString();
                if (identity.isEmpty()) {
                    Toast.makeText(UserBeneficiaryInfo.this, getString(R.string.emptyIdentitiy), Toast.LENGTH_SHORT).show();
                } else if (age_txt.isEmpty()) {
                    Toast.makeText(UserBeneficiaryInfo.this, getString(R.string.emptyage), Toast.LENGTH_SHORT).show();

                } else if (familynum_txt.isEmpty()) {
                    Toast.makeText(UserBeneficiaryInfo.this, getString(R.string.familynum), Toast.LENGTH_SHORT).show();

                } else if (salary_txt.isEmpty()) {
                    Toast.makeText(UserBeneficiaryInfo.this, getString(R.string.salary), Toast.LENGTH_SHORT).show();

                } else if (status_txt.isEmpty()) {
                    Toast.makeText(UserBeneficiaryInfo.this, getString(R.string.status), Toast.LENGTH_SHORT).show();

                } else if (job_txt.isEmpty()) {
                    Toast.makeText(UserBeneficiaryInfo.this, getString(R.string.jobempty), Toast.LENGTH_SHORT).show();
                } else {
                    update(identity, familynum_txt, job_txt, status_txt, age_txt, salary_txt);

                }

            }
        });


    }

    public void setUpElement() {
        age = findViewById(R.id.age);
        identiy = findViewById(R.id.identiy);
        familynumber = findViewById(R.id.familynum);
        salay = findViewById(R.id.salary);
        create = findViewById(R.id.create_btn);
        job = findViewById(R.id.job);
        status = findViewById(R.id.status);
        reference = FirebaseFirestore.getInstance().collection("Users");

    }


    public void update(String identetiy, String family, String havew, String status, String age, String salart) {
        reference.document(FirebaseAuth.getInstance().getUid()).update("age", age,
                "familynum", family, "identity",
                identetiy, "havework", havew, "salary", salart, "social", status)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UserBeneficiaryInfo.this, getString(R.string.success), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(UserBeneficiaryInfo.this, Login.class);
                        startActivity(i);
                        FirebaseAuth.getInstance().signOut();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}