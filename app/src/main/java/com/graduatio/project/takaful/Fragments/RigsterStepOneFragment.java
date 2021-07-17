package com.graduatio.project.takaful.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

public class RigsterStepOneFragment extends Fragment {

    EditText job, nameOFCharity, mediaAccount, address;
    Button next;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String userid;

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
        userid = firebaseAuth.getCurrentUser().getUid();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String job_txt = job.getText().toString();
                String media_txt = mediaAccount.getText().toString();
                String address_txt = address.getText().toString();
                String name_of_Inv_txt = job.getText().toString();

                if (job_txt.isEmpty() && !job_txt.trim().equals(" ")) {
                    Toast.makeText(getContext(), "Pleas Fill Your Profession", Toast.LENGTH_SHORT).show();
                } else if (name_of_Inv_txt.isEmpty() && !name_of_Inv_txt.trim().equals(" ")) {
                    Toast.makeText(getContext(), "Pleas Fill Name of Institution", Toast.LENGTH_SHORT).show();

                } else if (media_txt.isEmpty() && !media_txt.trim().equals(" ")) {
                    Toast.makeText(getContext(), "Pleas Fill Social Media Account", Toast.LENGTH_SHORT).show();

                } else if (address_txt.isEmpty() && !address_txt.trim().equals(" ")) {
                    Toast.makeText(getContext(), "Pleas Fill Your Address", Toast.LENGTH_SHORT).show();

                } else {
                    UpdateUploadedData();
                }

            }
        });
        return view;
    }

    public void UpdateUploadedData() {
        firebaseFirestore.collection("Advertising").document(userid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        String job_txt = task.getResult().getString("userJob");
                        String name_of_Inv_txt = task.getResult().getString("name_of_Charity");
                        String media_txt = task.getResult().getString("userJob");
                        String address_txt = task.getResult().getString("address");

                        job.setText(job_txt);
                        nameOFCharity.setText(name_of_Inv_txt);
                        mediaAccount.setText(media_txt);
                        address.setText(address_txt);

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