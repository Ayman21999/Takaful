package com.graduatio.project.takaful.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.graduatio.project.takaful.Actvities.EditProfile;
import com.graduatio.project.takaful.Actvities.Login;
import com.graduatio.project.takaful.Actvities.EditPaymentMethod;
import com.graduatio.project.takaful.Actvities.Users_HasCharities;
import com.graduatio.project.takaful.Model.User;
import com.graduatio.project.takaful.R;
import com.squareup.picasso.Picasso;

public class  ProfileFragment extends Fragment {

    ImageView userimage, editprofilr, addPayMethod, edit_paymothed;
    TextView name, phonenumber, email, paymethod;
    Button loguot,adminBtn;
    ProgressDialog progressDialog ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        userimage = view.findViewById(R.id.userimage);
        editprofilr = view.findViewById(R.id.editprofile);
        addPayMethod = view.findViewById(R.id.editpay_method);
        name = view.findViewById(R.id.username);
        email = view.findViewById(R.id.useremail);
        phonenumber = view.findViewById(R.id.usernphone);
        paymethod = view.findViewById(R.id.pay_method);
        loguot = view.findViewById(R.id.logout);
        edit_paymothed = view.findViewById(R.id.editpay_method);
        adminBtn = view.findViewById(R.id.adminbtn);
        progressDialog = new ProgressDialog(getContext());
        User[] user = new User[1];

        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user[0] = documentSnapshot.toObject(User.class);

                    }
                }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    name.setText(user[0].getFirstName());
                    email.setText(user[0].getEmail());
                    phonenumber.setText(user[0].getPhone());
                    paymethod.setText(user[0].getPayMethod());

                    if (user[0].getUserImage().isEmpty()) {
                        Toast.makeText(getContext(), getString(R.string.EmptyImage), Toast.LENGTH_SHORT).show();
                    } else {
                        Picasso.get().load(user[0].getUserImage()).centerCrop().fit().into(userimage);

                    }
                    if (user[0].getRole().equals("Admin")){
                        adminBtn.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                    }else {
                        adminBtn.setVisibility(View.INVISIBLE);
                        progressDialog.dismiss();

                    }
                }
            }
        });

        edit_paymothed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditPaymentMethod.class);
                startActivity(intent);
            }
        });
        editprofilr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfile.class);
                startActivity(intent);
            }
        });

        loguot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Users_HasCharities.class);
                startActivity(intent);
            }
        });
        return view;
    }


    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getContext(), Login.class);
        startActivity(i);

    }

}
