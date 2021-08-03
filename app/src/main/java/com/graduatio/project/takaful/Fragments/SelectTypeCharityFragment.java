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
    CollectionReference userRef;
    ImageView food, home, smallproject, next, education, health;
    String typeOfAdd = "";
    String TAG = "ttt";
    ProgressDialog progressDialog;
    View home_view,food_view,small_view,edue_view,health_view;
    String userAdsid;
    String adsid;

    CollectionReference updateuser;
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
        adsid = UUID.randomUUID().toString();
        updateuser = FirebaseFirestore.getInstance().collection("Users");
        edue_view = view.findViewById(R.id.view21);
        food_view = view.findViewById(R.id.view19);
        health_view = view.findViewById(R.id.textView22);
        home_view =view.findViewById(R.id.view20);
        small_view = view.findViewById(R.id.view23);
        userAdsid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            userRef = FirebaseFirestore.getInstance().collection("Users")
                    .document(userAdsid).collection("UserAds");
        progressDialog = new ProgressDialog(getContext());
//        userRef = FirebaseFirestore.getInstance().collection("UserAds");

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfAdd = "Food";
                food_view.setBackground(getResources().getDrawable(R.drawable.backgrounfselection));
                home_view.setBackground(getResources().getDrawable(R.drawable.selectshape));
                edue_view.setBackground(getResources().getDrawable(R.drawable.selectshape));
                health_view.setBackground(getResources().getDrawable(R.drawable.selectshape));
                small_view.setBackground(getResources().getDrawable(R.drawable.selectshape));
            }
        });
        education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfAdd = "Education";
                food_view.setBackground(getResources().getDrawable(R.drawable.selectshape));
                home_view.setBackground(getResources().getDrawable(R.drawable.selectshape));
                edue_view.setBackground(getResources().getDrawable(R.drawable.backgrounfselection));
                health_view.setBackground(getResources().getDrawable(R.drawable.selectshape));
                small_view.setBackground(getResources().getDrawable(R.drawable.selectshape));

            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfAdd = "Home";
                food_view.setBackground(getResources().getDrawable(R.drawable.selectshape));
                home_view.setBackground(getResources().getDrawable(R.drawable.backgrounfselection));
                edue_view.setBackground(getResources().getDrawable(R.drawable.selectshape));
                health_view.setBackground(getResources().getDrawable(R.drawable.selectshape));
                small_view.setBackground(getResources().getDrawable(R.drawable.selectshape));
            }
        });
        smallproject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfAdd = "Small Projects";
                food_view.setBackground(getResources().getDrawable(R.drawable.selectshape));
                home_view.setBackground(getResources().getDrawable(R.drawable.selectshape));
                edue_view.setBackground(getResources().getDrawable(R.drawable.selectshape));
                health_view.setBackground(getResources().getDrawable(R.drawable.selectshape));
                small_view.setBackground(getResources().getDrawable(R.drawable.backgrounfselection));
            }
        });
        health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfAdd = "Health";
                food_view.setBackground(getResources().getDrawable(R.drawable.selectshape));
                home_view.setBackground(getResources().getDrawable(R.drawable.selectshape));
                edue_view.setBackground(getResources().getDrawable(R.drawable.selectshape));
                health_view.setBackground(getResources().getDrawable(R.drawable.backgrounfselection));
                small_view.setBackground(getResources().getDrawable(R.drawable.selectshape));
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typeOfAdd.isEmpty()) {
                    Toast.makeText(getContext(), "Pleas select your add type", Toast.LENGTH_SHORT).show();
                } else {

                    CreateAdd(typeOfAdd);
                    CreateUserAds(typeOfAdd);
                    DialogFragment fragment = RigsterStepOneFragment.Rfragment();
                    fragment.show(getChildFragmentManager(), "aa");
                    Bundle bundle = new Bundle();
                    bundle.putString("adsid",adsid);
                    fragment.setArguments(bundle);

                }
            }
        });
        return view;
    }

    public void CreateAdd(String typeOfAdd) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Type", typeOfAdd);
        hashMap.put("title", "");
        hashMap.put("add_ID", adsid);
        hashMap.put("description", "");
        hashMap.put("image", "");
        hashMap.put("remaining", 0);
        hashMap.put("target", 0);
        hashMap.put("address", "");
        hashMap.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap.put("Daynumber", 0);
        hashMap.put("isRejected", true);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();
        reference.document(adsid).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        updateuser.document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .update("adsID",adsid).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    public void CreateUserAds(String typeOfAdd) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Type", typeOfAdd);
        hashMap.put("title", "");
        hashMap.put("add_ID", adsid);
        hashMap.put("description", "");
        hashMap.put("image", "");
        hashMap.put("remaining", 0);
        hashMap.put("target", 0);
        hashMap.put("address", "");
        hashMap.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap.put("Daynumber", 0);
        hashMap.put("isRejected", true);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();
        userRef.document(adsid).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
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