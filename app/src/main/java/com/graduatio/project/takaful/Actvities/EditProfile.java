package com.graduatio.project.takaful.Actvities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.graduatio.project.takaful.Fragments.ProfileFragment;
import com.graduatio.project.takaful.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditProfile extends AppCompatActivity {

    EditText fname, lname, email, phone;
    ImageView userimage, edit_image_iv, back;
    Button save;
    FirebaseFirestore firebaseFirestore;
    CollectionReference reference;
    FirebaseAuth fAuth;
    FirebaseStorage storage;
    StorageReference sreference;
    ProgressDialog mProgressDialog;
    String userid;
    private Uri filePath;
    private Uri imageUri;

    String imageUrl;
    String cameraImageFilePath;
    private final static int CAMERA_REQUEST_CODE = 1;
    boolean uploading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        SetUpElement();

        final String[] fname_txt = new String[1];
        final String[] lname_txt = new String[1];
        final String[] email_txt = new String[1];
        final String[] phone_txt = new String[1];
        final String[] imageUrl = new String[1];

        firebaseFirestore.collection("Users").document(userid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    fname_txt[0] = documentSnapshot.getString("firstName");
                    lname_txt[0] = documentSnapshot.getString("lastName");
                    email_txt[0] = documentSnapshot.getString("email");
                    phone_txt[0] = documentSnapshot.getString("phone");
                    imageUrl[0] = documentSnapshot.getString("userImage");
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    fname.setText(fname_txt[0]);
                    lname.setText(lname_txt[0]);
                    email.setText(email_txt[0]);
                    phone.setText(phone_txt[0]);
                    if (imageUrl[0] != null && !imageUrl[0].isEmpty()) {
                        Picasso.get().load(imageUrl[0]).fit().into(userimage);
                    }

                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveUpdatedDate();
            }
        });

        edit_image_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage(EditProfile.this);
            }
        });

    }

    public void SetUpElement() {
        fname = findViewById(R.id.firstname);
        lname = findViewById(R.id.lastname);
        email = findViewById(R.id.emailedit);
        phone = findViewById(R.id.phone);
        edit_image_iv = findViewById(R.id.image_edit);
        userimage = findViewById(R.id.useriamge);
        save = findViewById(R.id.save);
        firebaseFirestore = FirebaseFirestore.getInstance();
        reference = firebaseFirestore.collection("Users");
        storage = FirebaseStorage.getInstance();
        sreference = storage.getReference();
        mProgressDialog = new ProgressDialog(this);
        fAuth = FirebaseAuth.getInstance();
        userid = fAuth.getCurrentUser().getUid();
        back = findViewById(R.id.back_btn);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
          /// GALLERY
            if (requestCode == 2   && resultCode == RESULT_OK) {
                imageUri = data.getData();
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Updating ....");
                progressDialog.setCancelable(false);
                progressDialog.show();
                Picasso.get().load(imageUri).fit().into(userimage);
                progressDialog.dismiss();
//                UploadImage();
            }

    }

    public void SelectImage(Context context) {
        if (ActivityCompat.checkSelfPermission(EditProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            OpenImage();
        } else {
            ActivityCompat.requestPermissions(EditProfile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);

        }

    }

    private void updateData(String txt_fname, String txt_lname,
                            String txt_email, String txt_phone, ProgressDialog progressDialog) {


        final DocumentReference userRef = FirebaseFirestore.getInstance().collection("Users")
                .document(fAuth.getCurrentUser().getUid());

        userRef.update("firstName", txt_fname,
                "lastName", txt_lname,
                "email", txt_email,
                "phone", txt_phone)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        if (imageUrl != null && !imageUrl.isEmpty()) {

                            userRef.update("userImage", imageUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(EditProfile.this, ProfileFragment.class);
                                    finish();
                                }
                            });
                        } else {
                            progressDialog.dismiss();
                            Intent intent = new Intent(EditProfile.this, ProfileFragment.class);
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfile.this, "Error " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Log.d("qqq", e.getLocalizedMessage());
            }
        });
    }

    public void SaveUpdatedDate() {
        String txt_fname = fname.getText().toString();
        String txt_lname = lname.getText().toString();
        String txt_email = email.getText().toString();
        String txt_phone = phone.getText().toString();
        if (TextUtils.isEmpty(txt_fname)
                || TextUtils.isEmpty(txt_lname) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_phone)) {
            Toast.makeText(EditProfile.this, "All field are required", Toast.LENGTH_SHORT).show();
        } else {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Updating ....");
            progressDialog.setCancelable(false);
            progressDialog.show();
            if (imageUri != null) {
                StorageReference filepath = sreference.child("Profile img")
                        .child(imageUri.getLastPathSegment());
                filepath.putFile(imageUri).
                        addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        imageUrl = uri.toString();
                                        updateData(txt_fname, txt_lname, txt_email, txt_phone, progressDialog);
                                        Picasso.get().load(imageUrl).fit().into(userimage);
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        });
            } else {
                updateData(txt_fname, txt_lname, txt_email, txt_phone, progressDialog);
                progressDialog.dismiss();

            }

        }
    }

    public void OpenImage() {
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 2);

    }}