package com.graduatio.project.takaful.Actvities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.graduatio.project.takaful.R;
import com.squareup.picasso.Picasso;

import java.util.Collection;

public class UserInformation extends AppCompatActivity {
    TextView name, age, identiy, familynumber, phone, salay, soail, work;
    ImageView iamge, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        setUpElement();
        ReadUserData();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void setUpElement() {
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        identiy = findViewById(R.id.identiy);
        familynumber = findViewById(R.id.familynum);
        phone = findViewById(R.id.phone);
        soail = findViewById(R.id.soialstuts);
        iamge = findViewById(R.id.useriamge);
        work = findViewById(R.id.work);
        salay = findViewById(R.id.salary);
        back = findViewById(R.id.back);
    }

    public void ReadUserData() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Task<DocumentSnapshot> reference = FirebaseFirestore.getInstance().collection("Users")
                .document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String nametxt = documentSnapshot.getString("firstName");
                        String agetxt = documentSnapshot.getString("age");
                        String identiy_txt = documentSnapshot.getString("identity");
                        String phonetxt = documentSnapshot.getString("phone");
                        String txtsoail = documentSnapshot.getString("social");
                        String familynum = documentSnapshot.getString("familynum");
                        String salary = documentSnapshot.getString("salary");
                        String havework = documentSnapshot.getString("havework");
                        String userImage = documentSnapshot.getString("userImage");
                        name.setText(getString(R.string.UserNname)+nametxt);

                        age.setText(getString(R.string.userAge)+agetxt);

                        identiy.setText(getString(R.string.Useridentity)+identiy_txt);

                        soail.setText(getString(R.string.UserSocialStatus)+txtsoail);

                        familynumber.setText(getString(R.string.Familynumber)+familynum);

                        salay.setText(getString(R.string.UserSalary)+salary);
                        phone.setText(getString(R.string.UserPhone)+phonetxt);
                        work.setText(getString(R.string.UserJob)+havework);
//                        Picasso.get().load(userImage).into(iamge);ٍ
                    }
                });
    }
}