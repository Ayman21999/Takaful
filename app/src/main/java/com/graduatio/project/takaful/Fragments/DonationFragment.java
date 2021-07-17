package com.graduatio.project.takaful.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.graduatio.project.takaful.R;

public class DonationFragment extends Fragment {

    ImageView add_charity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donation, container, false);
        add_charity = view.findViewById(R.id.add_charity);

        add_charity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;

    }
    public void replaceFragment(Fragment fragment) {

        getChildFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

    }
}