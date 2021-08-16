package com.graduatio.project.takaful.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.graduatio.project.takaful.Adapter.AddAadapter;
import com.graduatio.project.takaful.Adapter.SearchAdapter;
import com.graduatio.project.takaful.Model.Advertising;
import com.graduatio.project.takaful.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SearchFragment extends Fragment {

    CollectionReference reference;
    ArrayList<Advertising> advertisings = new ArrayList<>();
    SearchAdapter adapter;
    RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView noResultsTv;
    ImageView searchIV;
    EditText searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchView = view.findViewById(R.id.search_text);
        progressBar = view.findViewById(R.id.progressBar);
        noResultsTv = view.findViewById(R.id.noResultsTv);
        recyclerView = view.findViewById(R.id.list);
        reference = FirebaseFirestore.getInstance()
                .collection("Advertising");
        searchIV = view.findViewById(R.id.search_btn);

        adapter = new SearchAdapter(getContext(), advertisings);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager manager
                = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(manager);

        reference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Advertising> arrayList = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {

                    arrayList.add(documentSnapshot.toObject(Advertising.class));
                }
                adapter.update(arrayList);
//                Toast.makeText(getContext(), "Size "+ advertisings.size(), Toast.LENGTH_SHORT).show();
            }
        });

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                adapter.getFilter().filter(s);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }


}