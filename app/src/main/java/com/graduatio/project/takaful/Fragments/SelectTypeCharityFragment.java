package com.graduatio.project.takaful.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.graduatio.project.takaful.R;

public class SelectTypeCharityFragment extends Fragment {

    FirebaseFirestore firebaseFirestore;
    CollectionReference reference;
    ImageView food,home, smallproject,next,education,health ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_type_charity, container, false);

        return view;
    }
}