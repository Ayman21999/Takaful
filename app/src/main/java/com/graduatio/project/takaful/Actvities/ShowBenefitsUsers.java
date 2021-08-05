package com.graduatio.project.takaful.Actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.graduatio.project.takaful.Adapter.AdsBenefitAdapter;
import com.graduatio.project.takaful.Adapter.MyAdsDonation;
import com.graduatio.project.takaful.Fragments.DonationFragment;
import com.graduatio.project.takaful.Fragments.MyAdsDetailsFragment;
import com.graduatio.project.takaful.Model.Advertising;
import com.graduatio.project.takaful.Model.Donations;
import com.graduatio.project.takaful.R;

import java.util.ArrayList;
import java.util.List;

public class ShowBenefitsUsers extends AppCompatActivity {

    List<Advertising> advertisings;
    boolean isLoading;
    private PostsBottomScrollListener scrollListener;
    Query query;
    AdsBenefitAdapter adapter;
    RecyclerView recyclerView;
    private DocumentSnapshot lastDocSnap;
    ImageView back;
    int donatoin_LIMIT = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_benefits_users);
        recyclerView = findViewById(R.id.list);
        back = findViewById(R.id.back);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        advertisings = new ArrayList<>();
        adapter = new AdsBenefitAdapter(ShowBenefitsUsers.this, advertisings);
        Intent intent =getIntent();
        String adsID = intent.getStringExtra("id");
        recyclerView.setAdapter(adapter);
        query = FirebaseFirestore.getInstance().
                collection("Advertising")
                .document(adsID)
                .collection("Requests");
        ReadDonatoins(true);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
                    advertisings.addAll(queryDocumentSnapshots.toObjects(Advertising.class));
                } else {
                    advertisings.addAll(advertisings.size(), queryDocumentSnapshots.toObjects(Advertising.class));
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
                adapter.notifyItemRangeInserted(advertisings.size() - resultSize, resultSize);
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