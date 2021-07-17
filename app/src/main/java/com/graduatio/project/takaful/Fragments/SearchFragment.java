package com.graduatio.project.takaful.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.graduatio.project.takaful.Adapter.AddAadapter;
import com.graduatio.project.takaful.Model.Advertising;
import com.graduatio.project.takaful.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SearchFragment extends Fragment implements
        SearchView.OnQueryTextListener {

    CollectionReference reference;
    List<Advertising> advertisings;
    AddAadapter adapter;
    RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView noResultsTv;

    SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchView = view.findViewById(R.id.search);
        progressBar = view.findViewById(R.id.progressBar);
        noResultsTv = view.findViewById(R.id.noResultsTv);
        recyclerView = view.findViewById(R.id.list);
        reference = FirebaseFirestore.getInstance().collection("Advertising");

        advertisings = new ArrayList<>();

        adapter = new AddAadapter(getContext(), advertisings);
        recyclerView.setAdapter(adapter);
        searchView.setOnClickListener(v -> searchView.onActionViewCollapsed());
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                clearList();
                return false;
            }
        });
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(this);
        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        searchForPost(query);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        clearList();

        return true;
    }

    private void searchForPost(String query) {

//        progressBar.setVisibility(View.VISIBLE);
//        noResultsTv.setVisibility(View.GONE);

        Query searchQuery;

        searchQuery = reference
                .orderBy("endDate", Query.Direction.DESCENDING);

        final String[] splitArr = query.toLowerCase().trim().split(" ");

        if (splitArr.length == 0) {

            searchQuery = reference.whereArrayContains("keyWords", query);

        } else if (splitArr.length <= 10) {

            searchQuery = searchQuery.whereArrayContainsAny("keyWords",
                    Arrays.asList(splitArr));

        } else {

            searchQuery = searchQuery.whereArrayContainsAny("keyWords",
                    Arrays.asList(splitArr).subList(0, 10));

        }

        final AtomicInteger itemsAdded = new AtomicInteger();
        searchQuery.get().addOnSuccessListener(snapshots -> {

            if (!snapshots.isEmpty()) {
                Log.d("ttt", "search size: " + snapshots.size());
                advertisings.clear();
                advertisings.addAll(snapshots.toObjects(Advertising.class));
            }

        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && !advertisings.isEmpty()) {
                    adapter.notifyDataSetChanged();
                }
                if (advertisings.isEmpty()) {
//                    noResultsTv.setVisibility(View.VISIBLE);
                }
//                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void clearList() {
        if (advertisings != null && !advertisings.isEmpty()) {
            advertisings.clear();
            adapter.notifyDataSetChanged();
        }
    }

    private void initializeObjects() {


    }

}