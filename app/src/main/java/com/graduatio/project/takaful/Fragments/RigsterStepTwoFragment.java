package com.graduatio.project.takaful.Fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.graduatio.project.takaful.R;

import java.util.Calendar;

public class RigsterStepTwoFragment extends DialogFragment {

    EditText title, whoarebenefit, target, deadline;
    Button perv, next;
    FirebaseFirestore firebaseFirestore;
    String homeid;
    String adsid;
    String userid;
    ProgressDialog progressDialog;
    CollectionReference userRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogTheme);
        Bundle bundle = this.getArguments();
        homeid = bundle.getString("id");
        adsid = bundle.getString("adsid");
//        Toast.makeText(getContext(), "ID : " + id, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rigster_step_two, container, false);
        title = view.findViewById(R.id.title);
        whoarebenefit = view.findViewById(R.id.name_of_ins);
        target = view.findViewById(R.id.target);
        deadline = view.findViewById(R.id.deadline);
        perv = view.findViewById(R.id.previous);
        next = view.findViewById(R.id.next);
        progressDialog = new ProgressDialog(getContext());
        firebaseFirestore =FirebaseFirestore.getInstance();
        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userRef = FirebaseFirestore.getInstance().collection("Users").document(userid).collection("UserAds");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title_txt = title.getText().toString();
                String whoarebenefit_txt = whoarebenefit.getText().toString();
                int target_txt = Integer.parseInt(target.getText().toString());
                int deaddate = Integer.parseInt((deadline.getText().toString()));
                if (title_txt.isEmpty()) {
                    Toast.makeText(getContext(), "Pleas Fill Title", Toast.LENGTH_SHORT).show();

                } else if (whoarebenefit_txt.isEmpty()) {
                    Toast.makeText(getContext(), "Pleas Fill who the Benefit ", Toast.LENGTH_SHORT).show();

                } else if (target_txt == 0) {
                    Toast.makeText(getContext(), "Pleas set your target", Toast.LENGTH_SHORT).show();

                } else {
                    UpdateUploadedData(title_txt, whoarebenefit_txt, target_txt , deaddate);
                    UpdateUploadedDataforuer(title_txt,whoarebenefit_txt, target_txt , deaddate);
                    Toast.makeText(getContext(), "Next Step", Toast.LENGTH_SHORT).show();
                    DialogFragment fragment = LastStepFragment.lastStepFragment();
                    fragment.show(getChildFragmentManager(), "aa");
                    Bundle bundle = new Bundle();
                    bundle.putString("id", homeid);
                    bundle.putString("adsid", adsid);
                    fragment.setArguments(bundle);

                }

            }
        });
        return view;
    }


    public void UpdateUploadedData(String title, String forWhoDonate, int  target , int beginDate) {

        progressDialog.show();
        progressDialog.setMessage("Updating...");
        firebaseFirestore.collection("Advertising").document(homeid)
                .update("title", title,
                        "whoarebenefit",
                        forWhoDonate,
                        "target", target ,
                        "Daynumber",beginDate
                        , "remaining" ,target)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Update Successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Tag", e.getLocalizedMessage());
                Toast.makeText(getContext(), "Error : " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    public void UpdateUploadedDataforuer(String title, String forWhoDonate, int  target , int beginDate) {

        progressDialog.show();
        progressDialog.setMessage("Updating...");
        FirebaseFirestore foruser = FirebaseFirestore.getInstance();

        userRef.document(adsid)
                .update("title", title,
                        "whoarebenefit",
                        forWhoDonate,
                        "target", target ,
                        "Daynumber",beginDate
                        , "remaining" ,target)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Update Successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Tag", e.getLocalizedMessage());
                Toast.makeText(getContext(), "Error : " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    public static RigsterStepTwoFragment RTwoFragment() {
        return new RigsterStepTwoFragment();
    }

}
//String title_txt = task.getResult().getString("title");
//                        String whobenefit_txt = task.getResult().getString("whoarebenefit");
//                        double targe_txt = Double.parseDouble(task.getResult().getString("target"));
//                        title.setText(title_txt);
//                        whoarebenefit.setText(whobenefit_txt);
//                        target.setText(targe_txt + "");