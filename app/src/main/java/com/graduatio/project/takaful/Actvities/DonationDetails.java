package com.graduatio.project.takaful.Actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.graduatio.project.takaful.Model.Advertising;
import com.graduatio.project.takaful.Model.Donations;
import com.graduatio.project.takaful.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.UUID;

public class DonationDetails extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    CollectionReference reference;
    ImageView ad_image;
    TextView targetnumber, desc, userPublisher, daynum, rimeing, title;
    Button donate;
    Advertising advertising;
    FirebaseFirestore dFirebaseFirestore;
    CollectionReference dreference;
    EditText number;
    String total;
    String userid;
    CollectionReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_details);
        SetUpElement();
        getAdData();
        Donate();

    }

    public void SetUpElement() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        reference = firebaseFirestore.collection("Advertising");
        dFirebaseFirestore = FirebaseFirestore.getInstance();
        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dreference = dFirebaseFirestore.collection("Users").document(userid).collection("Donation");
        ad_image = findViewById(R.id.add_image);
        targetnumber = findViewById(R.id.target_number);
        desc = findViewById(R.id.description);
        userPublisher = findViewById(R.id.user_published);
        daynum = findViewById(R.id.dayleft);
        rimeing = findViewById(R.id.remining_number);
        donate = findViewById(R.id.donate_bttn);
        title = findViewById(R.id.titleadd);
         userRef = FirebaseFirestore.getInstance().collection("Users");
    }


    private void getAdData() {


        if (getIntent() == null || !getIntent().hasExtra("id"))
            return;

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        DocumentReference adver_Ref;

        adver_Ref = FirebaseFirestore.getInstance().collection("Advertising")
                .document(id);


        adver_Ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    advertising = documentSnapshot.toObject(Advertising.class);
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful() && advertising != null) {

                    getUserInfo();
                    title.setText(advertising.getTitle());
                    desc.setText(advertising.getDescription());
                    targetnumber.setText("" + advertising.getTarget());
                    Picasso.get().load(advertising.getImage()).into(ad_image);
                    rimeing.setText("" + advertising.getRemaining());
                    daynum.setText("Day left" + advertising.getDaynumber());

                }

            }
        });


    }

    private void getUserInfo() {
        FirebaseFirestore.getInstance().collection("Users")
                .document(advertising.getUserId()).get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                userPublisher.setText("By : " + snapshot.getString("firstName"));
            }
        });
    }

    public void Donate() {
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDonationNumber();
            }
        });
    }

    public void donationOP(String total) {
        String payid = UUID.randomUUID().toString();
        HashMap hashMap = new HashMap();
        hashMap.put("userID", FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap.put("paymethod", "");
        hashMap.put("total", total);
        hashMap.put("payid", payid);
        hashMap.put("Adsid", advertising.getAdd_ID());

        hashMap.put("donateforAds", advertising.getName_of_Charity());

        dreference.document(payid).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(DonationDetails.this, Donate_Payment_Method.class);
                intent.putExtra("id", advertising.getAdd_ID());
                intent.putExtra("adsid", advertising.getAdd_ID());
                intent.putExtra("userid", advertising.getUserId());
                intent.putExtra("total", total);
                intent.putExtra("payid", payid);

                startActivity(intent);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ttt", e.getLocalizedMessage());
            }
        });
    }

    private void AddDonationNumber() {
        final BottomSheetDialog bsd = new BottomSheetDialog(DonationDetails.this, R.style.SheetDialog);
        final View parentView = getLayoutInflater().inflate(R.layout.bottom_sheet_number, null);
        parentView.setBackgroundColor(Color.TRANSPARENT);
        EditText number = parentView.findViewById(R.id.number);


        parentView.findViewById(R.id.donate_btn).setOnClickListener(view -> {
            total = number.getText() + "";
            if (total.isEmpty() && total.equals("")) {

                Toast.makeText(this, "Pleas insert number", Toast.LENGTH_SHORT).show();
            } else {
                donationOP(total);
                userRef.document(userid).update("isHasActivity", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DonationDetails.this, "Thanks For your donation ", Toast.LENGTH_SHORT).show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("sss","Errror :" + e.getLocalizedMessage());

                    }
                });
                bsd.dismiss();
            }


        });
        parentView.findViewById(R.id.close).setOnClickListener(view -> {
            bsd.dismiss();
        });
        bsd.setContentView(parentView);
        bsd.show();
    }

}