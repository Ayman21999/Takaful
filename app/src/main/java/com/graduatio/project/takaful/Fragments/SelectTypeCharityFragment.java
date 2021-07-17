package com.graduatio.project.takaful.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.graduatio.project.takaful.R;

import java.util.HashMap;
import java.util.UUID;

public class SelectTypeCharityFragment extends Fragment {

    FirebaseFirestore firebaseFirestore;
    CollectionReference reference;
    ImageView food, home, smallproject, next, education, health;
    String typeOfAdd = "";
    long beginDate;
   long endDate ;
    String TAG = "ttt";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_type_charity, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        reference = firebaseFirestore.collection("Advertising");
        food = view.findViewById(R.id.food);
        home = view.findViewById(R.id.home_ty);
        smallproject = view.findViewById(R.id.idea);
        education = view.findViewById(R.id.edu);
        health = view.findViewById(R.id.med);
        next = view.findViewById(R.id.next);

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfAdd = "Food";
                CreateAdd(typeOfAdd);


            }
        });
        education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfAdd = "Education";
                CreateAdd(typeOfAdd);

            }
        });
        health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfAdd = "Health";
                CreateAdd(typeOfAdd);

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new RigsterStepOneFragment());
            }
        });
        return view;
    }

    public void CreateAdd(String typeOfAdd) {
        String id = UUID.randomUUID().toString();
        HashMap<String , Object>hashMap = new HashMap<>();
        hashMap.put("Type",typeOfAdd);
        hashMap.put("title","");
        hashMap.put("add_ID",id);
        hashMap.put("description","");
        hashMap.put("image","");
        hashMap.put("remaining",0);
        hashMap.put("target",0);
        hashMap.put("address","");
        hashMap.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap.put("beginDate",beginDate);
        hashMap.put("endDate",endDate);
        hashMap.put("isRejected",true);
        reference.document(id).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "Next Step", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error : " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG,"Error : " + e.getLocalizedMessage());
            }
        });

    }
    public void replaceFragment(Fragment fragment) {

        getChildFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

    }
}