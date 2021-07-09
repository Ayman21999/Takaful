package com.graduatio.project.takaful.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.graduatio.project.takaful.R;

public class CategoryFragment extends Fragment {


    RecyclerView recyclerView ;
    TextView username ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View   view = inflater.inflate(R.layout.fragment_home, container, false);
       recyclerView = view.findViewById(R.id.recyclerView);
       username = view.findViewById(R.id.username);


        return view ;
    }
}