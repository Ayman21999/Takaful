package com.graduatio.project.takaful.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.graduatio.project.takaful.R;

import java.util.HashMap;
import java.util.UUID;

public class SelectTypeCharityFragment extends DialogFragment {

    FirebaseFirestore firebaseFirestore;
    CollectionReference reference;
    ImageView food, home, smallproject, next, education, health;
    String typeOfAdd = "";
    long beginDate;
    long endDate;
    String TAG = "ttt";
    ProgressDialog progressDialog;
    TextView home_txt,food_txt,small_txt,edue_txt,health_txt;
    String id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogTheme);
    }

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
        edue_txt = view.findViewById(R.id.education);
        food_txt = view.findViewById(R.id.txtfood);
        health_txt = view.findViewById(R.id.health);
        home_txt =view.findViewById(R.id.home);
        small_txt = view.findViewById(R.id.small);

        progressDialog = new ProgressDialog(getContext());


        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfAdd = "Food";
                food_txt.setTextColor(getResources().getColor(R.color.background));
                home_txt.setTextColor(getResources().getColor(R.color.txt_color));
                edue_txt.setTextColor(getResources().getColor(R.color.txt_color));
                health_txt.setTextColor(getResources().getColor(R.color.txt_color));
                small_txt.setTextColor(getResources().getColor(R.color.txt_color));


            }
        });
        education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfAdd = "Education";
                food_txt.setTextColor(getResources().getColor(R.color.txt_color));
                home_txt.setTextColor(getResources().getColor(R.color.txt_color));
                edue_txt.setTextColor(getResources().getColor(R.color.background));
                health_txt.setTextColor(getResources().getColor(R.color.txt_color));
                small_txt.setTextColor(getResources().getColor(R.color.txt_color));

            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfAdd = "Home";
                food_txt.setTextColor(getResources().getColor(R.color.txt_color));
                home_txt.setTextColor(getResources().getColor(R.color.background));
                edue_txt.setTextColor(getResources().getColor(R.color.txt_color));
                health_txt.setTextColor(getResources().getColor(R.color.txt_color));
                small_txt.setTextColor(getResources().getColor(R.color.txt_color));
            }
        });
        smallproject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfAdd = "Small Projects";
                food_txt.setTextColor(getResources().getColor(R.color.txt_color));
                home_txt.setTextColor(getResources().getColor(R.color.txt_color));
                edue_txt.setTextColor(getResources().getColor(R.color.txt_color));
                health_txt.setTextColor(getResources().getColor(R.color.txt_color));
                small_txt.setTextColor(getResources().getColor(R.color.background));
            }
        });
        health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfAdd = "Health";
                food_txt.setTextColor(getResources().getColor(R.color.txt_color));
                home_txt.setTextColor(getResources().getColor(R.color.txt_color));
                edue_txt.setTextColor(getResources().getColor(R.color.txt_color));
                health_txt.setTextColor(getResources().getColor(R.color.background));
                small_txt.setTextColor(getResources().getColor(R.color.txt_color));
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeOfAdd.isEmpty()) {
                    Toast.makeText(getContext(), "Pleas select your add type", Toast.LENGTH_SHORT).show();
                } else {

                    CreateAdd(typeOfAdd);
                    DialogFragment fragment = RigsterStepOneFragment.Rfragment();
                    fragment.show(getChildFragmentManager(), "aa");
                    Bundle bundle = new Bundle();
                    bundle.putString("id", id);
                    fragment.setArguments(bundle);

                }
            }
        });
        return view;
    }

    public void CreateAdd(String typeOfAdd) {
        id = UUID.randomUUID().toString();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Type", typeOfAdd);
        hashMap.put("title", "");
        hashMap.put("add_ID", id);
        hashMap.put("description", "");
        hashMap.put("image", "");
        hashMap.put("remaining", 0);
        hashMap.put("target", 0);
        hashMap.put("address", "");
        hashMap.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap.put("beginDate", beginDate);
        hashMap.put("endDate", endDate);
        hashMap.put("isRejected", true);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();
        reference.document(id).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "Next Step", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error : " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Error : " + e.getLocalizedMessage());
                progressDialog.dismiss();

            }
        });

    }

    public static SelectTypeCharityFragment sFragment() {
        return new SelectTypeCharityFragment();
    }
}