package com.graduatio.project.takaful.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.graduatio.project.takaful.Actvities.EditProfile;
import com.graduatio.project.takaful.R;

public class RigsterStepOneFragment extends DialogFragment {

    EditText job, nameOFCharity, mediaAccount, address;
    Button next;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String homeid;
    CollectionReference userRef;

    ProgressDialog progressDialog;
    String userid ;
    String Adsid;
    String job_txt;
    String media_txt;
    String address_txt;
    String name_of_Inv_txt;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogTheme);
        Bundle bundle = this.getArguments();
        homeid = bundle.getString("id");
        Adsid = bundle.getString("adsid");
        userid = firebaseAuth.getInstance().getCurrentUser().getUid();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rigster_step_one, container, false);
        job = view.findViewById(R.id.job);
        nameOFCharity = view.findViewById(R.id.name_of_ins);
        mediaAccount = view.findViewById(R.id.media_account);
        address = view.findViewById(R.id.address);
        next = view.findViewById(R.id.next);
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(getContext());
        userRef = FirebaseFirestore.getInstance().collection("Users").document(userid).collection("UserAds");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              job_txt = job.getText().toString();
             media_txt = mediaAccount.getText().toString();
             address_txt = address.getText().toString();
             name_of_Inv_txt = job.getText().toString();
                if (job_txt.isEmpty() && !job_txt.trim().equals(" ")) {
                    Toast.makeText(getContext(), "Pleas Fill Your Profession", Toast.LENGTH_SHORT).show();
                } else if (name_of_Inv_txt.isEmpty() && !name_of_Inv_txt.trim().equals(" ")) {
                    Toast.makeText(getContext(), "Pleas Fill Name of Institution", Toast.LENGTH_SHORT).show();

                } else if (media_txt.isEmpty() && !media_txt.trim().equals(" ")) {
                    Toast.makeText(getContext(), "Pleas Fill Social Media Account", Toast.LENGTH_SHORT).show();

                } else if (address_txt.isEmpty() && !address_txt.trim().equals(" ")) {
                    Toast.makeText(getContext(), "Pleas Fill Your Address", Toast.LENGTH_SHORT).show();

                } else {
                    UpdateUploadedData(job_txt,name_of_Inv_txt,media_txt,address_txt);
                    UpdateUploadedDataforuser(job_txt,name_of_Inv_txt,media_txt,address_txt);
                    DialogFragment dialogFragment = RigsterStepTwoFragment.RTwoFragment();
                    Bundle bundle =new Bundle();
                    bundle.putString("id",homeid);
                    bundle.putString("adsid",Adsid);
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(getChildFragmentManager(), "dd");

                }

            }
        });
        return view;
    }

    public void UpdateUploadedData(String job ,String nameOfCharity,String mediaAccount,String address) {
            progressDialog.show();
            progressDialog.setMessage("Updating...");
            firebaseFirestore.collection("Advertising").document(homeid)
                    .update("job",job, "name_of_Charity", nameOfCharity ,
                            "media_account",mediaAccount , "address",address).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "Update Successfully", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Tag",e.getLocalizedMessage());
                Toast.makeText(getContext(), "Error : "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }
    public void UpdateUploadedDataforuser   (String job ,String nameOfCharity,String mediaAccount,String address) {

        progressDialog.show();
        progressDialog.setMessage("Updating...");
        userRef.document(Adsid)
                .update("job",job, "name_of_Charity", nameOfCharity ,
                        "media_account",mediaAccount , "address",address).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "Update Successfully", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Tag",e.getLocalizedMessage());
                Toast.makeText(getContext(), "Error : "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }

    public static RigsterStepOneFragment Rfragment() {
        return new RigsterStepOneFragment();
    }
}