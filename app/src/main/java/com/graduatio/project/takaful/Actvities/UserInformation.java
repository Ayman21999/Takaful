package com.graduatio.project.takaful.Actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.graduatio.project.takaful.R;
import com.squareup.picasso.Picasso;

import java.util.Collection;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class UserInformation extends AppCompatActivity {
    TextView name, age, identiy, familynumber, phone, salay, soail, work;
    ImageView iamge, back, pdf;
    String attachment;
    StorageReference storage, ref;
    private static String POST_DOCUMENT_REF = "Post-documents/";
    String filename = "new File";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        setUpElement();
        ReadUserData();


        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                download();
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(attachmen);
//                startActivity(intent);
                downloadFiles(UserInformation.this,DIRECTORY_DOWNLOADS,attachment,filename);
                Log.d("ttt","The Url is : "+attachment);
                Toast.makeText(UserInformation.this, "Downloading .....", Toast.LENGTH_SHORT).show();
            }
        });
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
        pdf = findViewById(R.id.dpdf);
        storage = FirebaseStorage.getInstance().getReference().child(POST_DOCUMENT_REF);

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
                        attachment = documentSnapshot.getString("attachment");
                        name.setText(nametxt);

                        age.setText(agetxt);

                        identiy.setText(identiy_txt);

                        soail.setText(txtsoail);

                        familynumber.setText(familynum);

                        salay.setText(salary);
                        phone.setText(phonetxt);
                        work.setText(havework);
                        if (userImage != null && !userImage.isEmpty()) {
                            Picasso.get().load(userImage).into(iamge);

                        } else {
//                    Toast.makeText(context, "null image", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (attachment != null && !attachment.isEmpty()) {

                        }

                    }
                });
    }

//    public void download() {
//
//        ref = storage.child(POST_DOCUMENT_REF);
//
//        refٍ.addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                downloadFiles(UserInformation.this, DIRECTORY_DOWNLOADS, attachment);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(Exception e) {
//                Log.d("ttt", "The Errror File" + e.getLocalizedMessage());
//            }
//        });
//    }
    public void downloadFiles(Context context, String destination, String url ,String filename) {
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destination, filename);
        manager.enqueue(request);
    }
}