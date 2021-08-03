package com.graduatio.project.takaful.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.graduatio.project.takaful.Adapter.DonationAdapter;
import com.graduatio.project.takaful.Adapter.MyAdsAdapter;
import com.graduatio.project.takaful.Adapter.MyAdsDonation;
import com.graduatio.project.takaful.Model.Donations;
import com.graduatio.project.takaful.R;

import java.util.ArrayList;
import java.util.List;

public class MyAdsDetailsFragment extends DialogFragment {

    RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    CollectionReference collectionReference;
    List<Donations> donations;
    boolean isLoading;
    private PostsBottomScrollListener scrollListener;
    Query query;
    MyAdsDonation adapter;
    int donatoin_LIMIT = 10;
    String adsID;

    private DocumentSnapshot lastDocSnap;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
               Bundle bundle = this.getArguments();
        adsID = bundle.getString("adsId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_ads_details, container, false);
        recyclerView = view.findViewById(R.id.list);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        donations = new ArrayList<>();
        adapter = new MyAdsDonation(getContext(), donations);
        recyclerView.setAdapter(adapter);
        query = FirebaseFirestore.getInstance().
                collection("Advertising")
                .document(adsID)
                .collection("DonationsAds");
        ReadDonatoins(true);
        return view;
    }

    public void ReadDonatoins(boolean isInitial) {

        isLoading = true;
        Query updatedQuery = query;
        if (lastDocSnap != null) {
            updatedQuery = query.startAfter(lastDocSnap);
        }
        updatedQuery.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (!queryDocumentSnapshots.isEmpty()) {
                lastDocSnap = queryDocumentSnapshots.getDocuments().get(
                        queryDocumentSnapshots.size() - 1
                );
                if (isInitial) {
                    donations.addAll(queryDocumentSnapshots.toObjects(Donations.class));
                } else {
                    donations.addAll(donations.size(), queryDocumentSnapshots.toObjects(Donations.class));
                }
            }
        }).addOnCompleteListener(task -> {
            if (isInitial) {
                adapter.notifyDataSetChanged();
                if (task.getResult().size() == donatoin_LIMIT && scrollListener == null) {
                    recyclerView.addOnScrollListener(scrollListener = new PostsBottomScrollListener());
                }
            } else {
                final int resultSize = task.getResult().size();
                adapter.notifyItemRangeInserted(donations.size() - resultSize, resultSize);
                if (resultSize < donatoin_LIMIT && scrollListener != null) {
                    recyclerView.removeOnScrollListener(scrollListener);
                }
            }


            isLoading = false;
        });
    }

    private class PostsBottomScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (!isLoading &&
                    !recyclerView.canScrollVertically(1) &&
                    newState == RecyclerView.SCROLL_STATE_IDLE) {

                Log.d("ttt", "is at bottom");

                ReadDonatoins(false);

            }
        }
    }

    public static MyAdsDetailsFragment myAdsDetailsFragment() {
        return new MyAdsDetailsFragment();
    }
}