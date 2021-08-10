 package com.graduatio.project.takaful.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.graduatio.project.takaful.Actvities.EditProfile;
import com.graduatio.project.takaful.Actvities.HomeActivity;
import com.graduatio.project.takaful.R;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class LastStepFragment extends DialogFragment {

    ImageView add_image, ads_image;
    Button publish;
    EditText phonenumber, desc;
    FirebaseFirestore firebaseFirestore;
    String image_url, cameraImageFilePath;
    FirebaseStorage storage;
    boolean isUploading;
    private Uri filePath;
    ProgressDialog mProgressDialog;
    StorageReference sreference;
    String adsid;
    String userid;
    CollectionReference userRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogTheme);
        Bundle bundle = this.getArguments();
        adsid = bundle.getString("adsid");
        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_last_step, container, false);
        add_image = view.findViewById(R.id.add_image);
        phonenumber = view.findViewById(R.id.phone_num);
        desc = view.findViewById(R.id.desc_txt);
        publish = view.findViewById(R.id.publish_btn);
        firebaseFirestore = FirebaseFirestore.getInstance();
        userRef = FirebaseFirestore.getInstance().collection("Users").document(userid).collection("UserAds");

        ads_image = view.findViewById(R.id.image_ads);
        mProgressDialog = new ProgressDialog(getContext());

        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage(getContext());
            }
        });

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phonenumber.getText().toString();
                String desc_txt = desc.getText().toString();
                if (image_url.isEmpty()) {
                    Toast.makeText(getContext(), getString(R.string.pickImage), Toast.LENGTH_SHORT).show();
                } else if (phone.isEmpty()) {
                    Toast.makeText(getContext(), getString(R.string.phoneEmpty), Toast.LENGTH_SHORT).show();

                } else if (!isValidMobile(phone) && phone.length() > 6 && phone.length() <= 13) {
                    Toast.makeText(getContext(), getString(R.string.validphone), Toast.LENGTH_SHORT).show();
                } else if (desc_txt.isEmpty()) {
                    Toast.makeText(getContext(), getString(R.string.addDecs), Toast.LENGTH_SHORT).show();

                } else {
                    UpdateUploadedData(image_url, desc_txt, phone);
                    UpdateUploadedDataforuser(image_url, desc_txt, phone);
                    showPostOptionsBottomSheet();

                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            /// GALLERY
            isUploading = true;
            filePath = data.getData();
            mProgressDialog.setMessage(getString(R.string.Uploading));
            mProgressDialog.show();
            sreference = FirebaseStorage.getInstance().getReference().child("Ads_img/" + UUID.randomUUID().toString());
            sreference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgressDialog.dismiss();
                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            image_url = uri.toString();
                            Picasso.get().load(image_url).fit().into(ads_image);
                            Log.d("ttt", image_url);
                            Toast.makeText(getContext(), image_url, Toast.LENGTH_SHORT).show();
                        }
                    });
                    Toast.makeText(getContext(), getString(R.string.UploadFinish), Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("aa", "Errrrrrrrrrrrrrrrrrrrrrrror" + e.getMessage());
                }
            });
        }
    }

    public void UpdateUploadedData(String img, String desc, String phone) {
        mProgressDialog.show();
        mProgressDialog.setMessage("Updating...");
        firebaseFirestore.collection("Advertising")
                .document(adsid).update("description", desc, "image", img, "userphone", phone)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), getString(R.string.success), Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Tag", e.getLocalizedMessage());
                Toast.makeText(getContext(), "Error : " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        });
    }

    public void UpdateUploadedDataforuser(String img, String desc, String phone) {
        mProgressDialog.show();
        mProgressDialog.setMessage(getString(R.string.update));
        userRef.document(adsid).update("description", desc, "image", img, "userphone", phone)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), getString(R.string.success), Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Tag", e.getLocalizedMessage());
                Toast.makeText(getContext(), "Error : " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        });
    }

    public void SelectImage(Context context) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            OpenImage();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);

        }

    }

    public void OpenImage() {
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 2);

    }

    public static LastStepFragment lastStepFragment() {
        return new LastStepFragment();
    }

    private void showPostOptionsBottomSheet() {

        final BottomSheetDialog bsd = new BottomSheetDialog(getContext(), R.style.SheetDialog);
        final View parentView = getLayoutInflater().inflate(R.layout.success_bottomsheet, null);
        parentView.setBackgroundColor(Color.TRANSPARENT);

        parentView.findViewById(R.id.home_btn).setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), HomeActivity.class);
            getActivity().startActivity(intent);
            bsd.dismiss();

        });
        bsd.setContentView(parentView);
        bsd.show();
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
}