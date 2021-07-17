package com.graduatio.project.takaful.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.graduatio.project.takaful.R;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class LastStepFragment extends Fragment {

    ImageView add_image;
    Button publish;
    EditText phonenumber;
    EditText desc;
    FirebaseFirestore firebaseFirestore;
    String image_url;
    FirebaseStorage storage;
    boolean isUploading;
    String cameraImageFilePath;
    private final static int CAMERA_REQUEST_CODE = 1;

    private Uri filePath;
    ProgressDialog mProgressDialog;
    StorageReference sreference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_last_step, container, false);
        add_image = view.findViewById(R.id.add_image);
        phonenumber = view.findViewById(R.id.phone_num);
        desc = view.findViewById(R.id.desc_txt);
        publish = view.findViewById(R.id.publish_btn);

        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    Intent pikPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pikPhoto, 2);

                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);

                }
            }
        });

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phonenumber.getText().toString();
                String desc_txt = desc.getText().toString();
                if (image_url.isEmpty()) {
                    Toast.makeText(getContext(), "Pleas Add Image for your Advertising", Toast.LENGTH_SHORT).show();
                } else if (phone.isEmpty()) {
                    Toast.makeText(getContext(), "Pleas Add Phone Number", Toast.LENGTH_SHORT).show();

                } else if (desc_txt.isEmpty()) {
                    Toast.makeText(getContext(), "Pleas Add description", Toast.LENGTH_SHORT).show();

                } else {
                    UpdateUploadedData();
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            /// GALLERY
            isUploading = true;
            mProgressDialog.setMessage("Uploading ....");
            mProgressDialog.show();
            filePath = Uri.parse("file://" + cameraImageFilePath);
            sreference = FirebaseStorage.getInstance().getReference().child("img/" + UUID.randomUUID().toString());
            sreference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgressDialog.dismiss();
                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            image_url = uri.toString();
                            Picasso.get().load(image_url).fit().into(add_image);
                            Log.d("ttt", image_url);
                            Toast.makeText(getContext(), image_url, Toast.LENGTH_SHORT).show();
                        }
                    });
                    Toast.makeText(getContext(), "Upload Finish", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void UpdateUploadedData() {
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        firebaseFirestore.collection("Advertising").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        String txt_phone = task.getResult().getString("userphone");
                        String txt_desc = task.getResult().getString("description");
                        image_url = task.getResult().getString("image");
                        phonenumber.setText(txt_phone);
                        desc.setText(txt_desc);

                    } else {
                        Toast.makeText(getActivity(), "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}