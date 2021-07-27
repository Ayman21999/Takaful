package com.graduatio.project.takaful.Actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.graduatio.project.takaful.Model.User;
import com.graduatio.project.takaful.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Donate_Payment_Method extends AppCompatActivity {

    ImageView ads_image;
    TextView ads_title, ads_userpublisher, total;
    Button donate, donateAsAnonymous;
    CollectionReference reference;
    Intent intent;
    RadioButton visa,google,paypal;
    CollectionReference update;
    ProgressDialog progressDialog ;
    String intExtra;
    int totalremaining;
    String  paymethod = "";
    String userid ;
    String userdonationid;
    Task<DocumentSnapshot> task;
    String id;
    String payid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donate_payment_method);
        SetUpElement();
        getIntents();
        AdsData();
        UserData();

//        donateAsAnonymous.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (donateAsAnonymous.isActivated()){
//                    donateAsAnonymous.setBackground(getResources().getDrawable(R.color.tabcolor));
//                    donateAsAnonymous.setText("You will donate in Anonymous name");
//                    donateAsAnonymous.setTextColor(Color.BLACK);
//                    DonateAsAnonymuos();
//                }
//
//            }
//        });
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visa.isChecked()){
                       paymethod ="Visa";
                }else if (paypal.isChecked()){
                    paymethod ="Paypal";

                }else if (google.isChecked()){
                    paymethod ="Google Wallet";

                }
                    if (paymethod.isEmpty()){
                        Toast.makeText(Donate_Payment_Method.this, "Pleas your Method", Toast.LENGTH_SHORT).show();
                    }else {
                        UpdaateDate(paymethod);
                        Donation();

                    }


            }
        });
    }

    public void SetUpElement() {
        ads_image = findViewById(R.id.ads_image);
        ads_title = findViewById(R.id.ads_title);
        ads_userpublisher = findViewById(R.id.ads_publisher);
        total = findViewById(R.id.totalnumber);
        donate = findViewById(R.id.donate_btn);
        visa = findViewById(R.id.visaradio);
        google = findViewById(R.id.googlewallet);
        paypal = findViewById(R.id.paypal);
        donateAsAnonymous = findViewById(R.id.anonymous_btn);
        reference = FirebaseFirestore.getInstance().collection("Advertising");
        userdonationid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        update = FirebaseFirestore.getInstance().collection("Users").
                document(userdonationid).collection("Donation");
        progressDialog = new ProgressDialog(this);
    }

    public void AdsData() {
            reference.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String title = documentSnapshot.getString("title");
                    String image = documentSnapshot.getString("image");

                     totalremaining = documentSnapshot.getLong("remaining").intValue();
                    ads_title.setText(title);
                    Picasso.get().load(image).into(ads_image);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Donate_Payment_Method.this, "Error " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });

    }

    public void UserData() {

            task = FirebaseFirestore.getInstance().collection("Users")
                    .document(userid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String usernare = documentSnapshot.getString("firstName");
                            ads_userpublisher.setText("By :" + usernare);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Donate_Payment_Method.this, "Error " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

    }


    private void showPostOptionsBottomSheet() {

        final BottomSheetDialog bsd = new BottomSheetDialog(this, R.style.SheetDialog);
        final View parentView = getLayoutInflater().inflate(R.layout.bottomsheet_success_pay, null);
        parentView.setBackgroundColor(Color.TRANSPARENT);

        parentView.findViewById(R.id.home_btn).setOnClickListener(view -> {
            Intent intent  = new Intent(this , HomeActivity.class);
            startActivity(intent);
            bsd.dismiss();

        });
        bsd.setContentView(parentView);
        bsd.show();
    }
//    public void DonateAsAnonymuos() {
//        HashMap hashMap = new HashMap();
//        hashMap.put("User name","Anonymous");
//        String id=  intent.getStringExtra("payid");
//                update.document(id).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        progressDialog.dismiss();
//                        showPostOptionsBottomSheet();
//                    }
//                });
//            }

    public void UpdaateDate(String paymethod){

        update.document(payid).update("paymethod",paymethod).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showPostOptionsBottomSheet();

            }
        });
    }
        public void Donation(){
            int total = Integer.parseInt(intExtra);
            int remaining = totalremaining - total;

            reference.document(id).update("remaining",remaining).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Donate_Payment_Method.this, "Error " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        }

        public void getIntents(){
        Intent intent = getIntent();
         id = intent.getStringExtra("id");
        userid = intent.getStringExtra("userid");
        intExtra = intent.getStringExtra("total");
        payid = intent.getStringExtra("payid");
        }
}

