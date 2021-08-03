package com.graduatio.project.takaful.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.graduatio.project.takaful.Adapter.DonationAdapter;
import com.graduatio.project.takaful.Model.Donations;
import com.graduatio.project.takaful.R;

import java.util.ArrayList;
import java.util.List;

public class ActivityFragment extends Fragment {

    String userid;
    FirebaseFirestore firebaseFirestore;
    Task<DocumentSnapshot> reference;
CollectionReference donateRef;
    RecyclerView recyclerView ;
    DonationAdapter adapter;
    List<Donations>donations;
    ImageView imageView ;
    View backView ;
    TextView mss1 ,mss2;
    boolean isLoading;
    private PostsBottomScrollListener scrollListener;
    Query query;
    int donatoin_LIMIT = 10;
    private DocumentSnapshot lastDocSnap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseFirestore= FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.list);
        backView = view.findViewById(R.id.view17);
        query = firebaseFirestore.collection("Users").document(userid).collection("Donation");
        mss1 = view.findViewById(R.id.mss1);
        mss2 = view.findViewById(R.id.mss2);
        imageView = view.findViewById(R.id.add_activity);
        donations = new ArrayList<>();

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        adapter = new DonationAdapter(getContext() ,donations);
        recyclerView.setAdapter(adapter);
        ReadDonatoins(true);

        reference = firebaseFirestore.collection("Users").document(userid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        boolean isHasActivity =documentSnapshot.getBoolean("isHasActivity");
                        if (isHasActivity == true){
                            mss1.setVisibility(View.GONE);
                            mss2.setVisibility(View.GONE);
                            imageView.setVisibility(View.GONE);
                            backView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);


                        }else {
                            mss1.setVisibility(View.VISIBLE);
                            mss2.setVisibility(View.VISIBLE);
                            imageView.setVisibility(View.VISIBLE);
                            backView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    }
                });
        return view;
    }
    public void ReadDonatoins(boolean isInitial){
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
}