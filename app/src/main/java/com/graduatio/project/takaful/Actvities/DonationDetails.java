package com.graduatio.project.takaful.Actvities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.graduatio.project.takaful.Fragments.LastStepFragment;
import com.graduatio.project.takaful.Model.Advertising;
import com.graduatio.project.takaful.Model.Donations;
import com.graduatio.project.takaful.R;
import com.graduatio.project.takaful.Service.CloudMessagingNotificationsSender;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.UUID;

public class DonationDetails extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore, dFirebaseFirestore;
    ImageView ad_image;
    TextView targetnumber, desc, userPublisher, daynum, rimeing, title;
    Button donate, make_request, showBenefits;
    Advertising advertising;
    CollectionReference dreference, userRef, adsUserDonations, reference;
    String total;
    Intent i;
    private static String POST_DOCUMENT_REF = "Post-documents/";
    private static final String[] supportedMimeTypes = {"application/pdf", "application/msword",
            "text/*", "application/vnd.ms-excel", "application/zip"};
    private final static String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    static String adsId;
    static String donerid;
    ProgressDialog progressDialog;
    String userdasid, userpublisher, role, userid;
    Uri attchmentURI;
    String donationtotal;
    SeekBar seekBar;
    FirebaseStorage storage;
    ActivityResultLauncher<String> launcher;
    String attachmentUrld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_details);
        SetUpElement();
        getIntents();
        getAdData();
        Donate();


        launcher = registerForActivityResult
                (new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        if (result != null) {
                            Log.d("ddd", "has resulr");
                            attchmentURI = result;
                            final StorageReference reference = FirebaseStorage.getInstance().getReference()
                                    .child(POST_DOCUMENT_REF).child(UUID.randomUUID().toString() + "-" +
                                            System.currentTimeMillis());
                            final UploadTask uploadTask = reference.putFile(attchmentURI);
                            final StorageTask<UploadTask.TaskSnapshot> onSuccessListener =
                                    uploadTask.addOnSuccessListener(taskSnapshot -> {
                                        reference.getDownloadUrl().addOnSuccessListener(uri -> {
                                            attachmentUrld = uri.toString();

//                                            MakeRequestNumber(attachmentUrld);

                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(Exception e) {
                                                Log.d("aaa", e.getLocalizedMessage());
                                            }
                                        });
                                    });

                        }
                    }
                });
        make_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MakeRequestNumber();

            }
        });
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        userRef.document(userid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                role = documentSnapshot.getString("role");
                if (role.equals("beneficiary")) {
                    make_request.setVisibility(View.VISIBLE);
                    donate.setVisibility(View.INVISIBLE);
                    progressDialog.dismiss();
                } else if (role.equals("Donors")) {
                    donate.setVisibility(View.VISIBLE);
                    make_request.setVisibility(View.INVISIBLE);
                    progressDialog.dismiss();

                } else if (role.equals("Admin")) {
                    progressDialog.dismiss();
                    showBenefits.setVisibility(View.VISIBLE);
                    make_request.setVisibility(View.INVISIBLE);
                    donate.setVisibility(View.INVISIBLE);


                } else {
                    Toast.makeText(DonationDetails.this, "Nulll", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        Toast.makeText(this, "Ads ID:: " + donerid, Toast.LENGTH_SHORT).show();
//        Log.d("tt", "Doner ID" + donerid);
        showBenefits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonationDetails.this, ShowBenefitsUsers.class);
                intent.putExtra("id", adsId);
                startActivity(intent);
            }
        });
    }

    public void SetUpElement() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        reference = firebaseFirestore.collection("Advertising");
        dFirebaseFirestore = FirebaseFirestore.getInstance();
        i = getIntent();
        storage = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(this);
        adsUserDonations = FirebaseFirestore.getInstance().collection("Advertising");
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
        seekBar = findViewById(R.id.seekBar);
        userRef = FirebaseFirestore.getInstance().collection("Users");
        showBenefits = findViewById(R.id.adimn_btn);

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
                    int remeining = advertising.getTarget() - advertising.getRemaining();
                    int percnt = (remeining / 1000) * 10;
                    seekBar.setProgress(percnt);

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

                Toast.makeText(this, getText(R.string.emptynumber), Toast.LENGTH_SHORT).show();
            } else {
                donationOP(total);
                DonationRef(total);

                userRef.document(userid).update("isHasActivity", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DonationDetails.this, getString(R.string.thanksDonation), Toast.LENGTH_SHORT).show();


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
        final View parentView = getLayoutInflater().inflate(R.layout.make_request_bottm_sheet, null);
        parentView.setBackgroundColor(Color.TRANSPARENT);
        EditText number = parentView.findViewById(R.id.number);
        parentView.findViewById(R.id.donate_btn).setOnClickListener(view -> {
            donationtotal = number.getText() + "";
            if (donationtotal.isEmpty() && donationtotal.equals("")) {

                Toast.makeText(this, getString(R.string.emptynumber), Toast.LENGTH_SHORT).show();
            } else if (attchmentURI == null) {
                Toast.makeText(this, getString(R.string.emptfile), Toast.LENGTH_SHORT).show();
            } else {

                MakeRequest(donationtotal,attachmentUrld);
                CloudMessagingNotificationsSender.Data data =
                        new CloudMessagingNotificationsSender.Data
                                (userid, "Request", "New Request"
                                        , "ds", advertising.getUserId(), 55);
                CloudMessagingNotificationsSender.sendNotification(FirebaseAuth.getInstance().getCurrentUser().getUid(), data);
                bsd.dismiss();
            }


        });
        parentView.findViewById(R.id.attachment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkStoragePermissions(3)) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setType("*/*");
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, supportedMimeTypes);
                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    launcher.launch("*/*");
                }
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
        if (adsId.equals(null) && total.isEmpty()) {
            Toast.makeText(this, "NUlllllllllllll", Toast.LENGTH_SHORT).show();
        } else {
            HashMap map = new HashMap();
            map.put("donerid", donerid);
            map.put("total", total);
            map.put("Adsid", adsId);
            map.put("payid", did);
            adsUserDonations.document(adsId)
                    .collection("DonationsAds")
                    .document(donerid)
                    .set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(DonationDetails.this, getString(R.string.success), Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("qqq", e.getMessage());
                }
            });
        }
    }

    public void getIntents() {

        adsId = i.getStringExtra("id");
        donerid = i.getStringExtra("donerid");

    }

    public static DonationDetails lastStepFragment() {
        return new DonationDetails();
    }

    public void MakeRequest(String total, String attachmentUrl) {
        CollectionReference reference = FirebaseFirestore.getInstance()
                .collection("Advertising").document(adsId).collection("Requests");
        CollectionReference user = FirebaseFirestore.getInstance().collection("Users");
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        user.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String username = documentSnapshot.getString("firstName");
                String userimage = documentSnapshot.getString("userImage");
                progressDialog.show();
                progressDialog.setMessage("Loading...");
                HashMap hashMap = new HashMap();
                hashMap.put("userId", id);
                hashMap.put("add_ID", adsId);
                hashMap.put("firstName", username);
                hashMap.put("userImage", userimage);
                hashMap.put("isdeleted", false);
                hashMap.put("donated", false);
                hashMap.put("attachment", attachmentUrl);
                hashMap.put("total", total);
                reference.document(id).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DonationDetails.this, getString(R.string.requestsent), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();

                        Toast.makeText(DonationDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                Task<Void> reference1 = FirebaseFirestore.getInstance()
                        .collection("Users")
                        .document(id).update("attachment",attachmentUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure( Exception e) {
                            Log.d("ttt",e.getLocalizedMessage());
                            }
                        });
            }
        });

    }

    private boolean checkStoragePermissions(int requestCode) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(permissions[0]) != PackageManager.PERMISSION_GRANTED) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    //doesn't need to request write persmission
                    requestPermissions(new String[]{permissions[0]}, requestCode);
                } else {
                    //needs to request write persmission
                    requestPermissions(permissions, requestCode);

                }

                return false;
            } else {
                return true;
            }

        }
        return true;
    }
}