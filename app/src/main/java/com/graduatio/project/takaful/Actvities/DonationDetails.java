package com.graduatio.project.takaful.Actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.graduatio.project.takaful.Fragments.LastStepFragment;
import com.graduatio.project.takaful.Model.Advertising;
import com.graduatio.project.takaful.Model.Donations;
import com.graduatio.project.takaful.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.UUID;

public class DonationDetails extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore,dFirebaseFirestore;
    ImageView ad_image;
    TextView targetnumber, desc, userPublisher, daynum, rimeing, title;
    Button donate,make_request;
    Advertising advertising;
    CollectionReference dreference,userRef,adsUserDonations,reference;
    static String total;
    Intent i;
    static String adsId;
    static String donerid;
    ProgressDialog progressDialog;
    String userdasid,userpublisher,role,userid;
    String donationtotal ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_details);
        SetUpElement();
        getIntents();
        getAdData();
        Donate();
      make_request.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              MakeRequestNumber();
          }
      });
        progressDialog.setMessage("Loading...!");
        progressDialog.show();
        userRef.document(userid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
           role = documentSnapshot.getString("role");
           if (role.equals("beneficiary")){
            make_request.setVisibility(View.VISIBLE);
            donate.setVisibility(View.INVISIBLE);
            progressDialog.dismiss();
           }else if (role.equals("Donors")){
               donate.setVisibility(View.VISIBLE);
               make_request.setVisibility(View.INVISIBLE);
               progressDialog.dismiss();

           }else {
               progressDialog.dismiss();
           }
            }
        });
        Toast.makeText(this, "Ads ID:: " + donerid, Toast.LENGTH_SHORT).show();
        Log.d("tt", "Doner ID" + donerid);
    }

    public void SetUpElement() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        reference = firebaseFirestore.collection("Advertising");
        dFirebaseFirestore = FirebaseFirestore.getInstance();
        i = getIntent();
        progressDialog = new ProgressDialog(this);
        adsUserDonations =FirebaseFirestore.getInstance().collection("Advertising") ;
        make_request = findViewById(R.id.request);
        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dreference = dFirebaseFirestore.collection("Users")
                .document(userid)
                .collection("Donation");
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

        DocumentReference adver_Ref;

        adver_Ref = FirebaseFirestore.getInstance().collection("Advertising")
                .document(adsId);


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
                userpublisher = snapshot.getString("firstName");
                userPublisher.setText("By : " + userpublisher);
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
        hashMap.put("Adsid", adsId);

        hashMap.put("donateforAds", advertising.getName_of_Charity());
        dreference.document(payid).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(DonationDetails.this, Donate_Payment_Method.class);
                intent.putExtra("id", adsId);
                intent.putExtra("userid", userid);
                intent.putExtra("total", total);
                userdasid = advertising.getUserId();

                intent.putExtra("payid", payid);
                intent.putExtra("userAds", userdasid);

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
                DonationRef(total);

                userRef.document(userid).update("isHasActivity", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DonationDetails.this, "Thanks For your donation ", Toast.LENGTH_SHORT).show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("sss", "Errror :" + e.getLocalizedMessage());

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


    private void MakeRequestNumber() {
        final BottomSheetDialog bsd = new BottomSheetDialog(DonationDetails.this, R.style.SheetDialog);
        final View parentView = getLayoutInflater().inflate(R.layout.bottom_sheet_number, null);
        parentView.setBackgroundColor(Color.TRANSPARENT);
        EditText number = parentView.findViewById(R.id.number);


        parentView.findViewById(R.id.donate_btn).setOnClickListener(view -> {
            donationtotal = number.getText() + "";
            if (donationtotal.isEmpty() && donationtotal.equals("")) {

                Toast.makeText(this, "Pleas insert number", Toast.LENGTH_SHORT).show();
            } else {
                MakeRequest(donationtotal);
                bsd.dismiss();
            }


        });
        parentView.findViewById(R.id.close).setOnClickListener(view -> {
            bsd.dismiss();
        });
        bsd.setContentView(parentView);
        bsd.show();
    }
    public void DonationRef(String total) {
        String did = UUID.randomUUID().toString();
        if (adsId.equals(null)&& total.isEmpty()){
            Toast.makeText(this, "NUlllllllllllll", Toast.LENGTH_SHORT).show();
        }else {
            HashMap map = new HashMap();
            map.put("donerid", donerid);
            map.put("total", total);
            map.put("Adsid", adsId);
            map.put("payid", did);
            adsUserDonations.document(adsId)
                    .collection("DonationsAds").document(donerid).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(DonationDetails.this, "Successful", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("qqq",e.getMessage());
                }
            });
        }
    }

    public void getIntents() {

        adsId = i.getStringExtra("id");
        donerid = i.getStringExtra("donerid");

    }

    public static DonationDetails lastStepFragment(){
        return new DonationDetails();
    }
    public void MakeRequest(String  total){
        CollectionReference reference = FirebaseFirestore.getInstance()
                .collection("Advertising").document(adsId).collection("Requests");
        CollectionReference user = FirebaseFirestore.getInstance().collection("Users");
        String id =    FirebaseAuth.getInstance().getCurrentUser().getUid();
        user.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String username = documentSnapshot.getString("firstName");
                String userimage = documentSnapshot.getString("userImage");
                progressDialog.show();
                progressDialog.setMessage("Loading...");
                HashMap hashMap = new HashMap();
                hashMap.put("userId" , id);
                hashMap.put("add_ID",adsId);
                hashMap.put("firstName",username);
                hashMap.put("userImage",userimage);
                hashMap.put("isdeleted",false);
                hashMap.put("donated",false);
                hashMap.put("total",total);
                reference.document(id).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DonationDetails.this, "Your Request had been sent", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();

                        Toast.makeText(DonationDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
}