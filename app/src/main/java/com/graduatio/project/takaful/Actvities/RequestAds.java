package com.graduatio.project.takaful.Actvities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.graduatio.project.takaful.Adapter.AdminAdapter;
import com.graduatio.project.takaful.Adapter.RequsetAadpter;
import com.graduatio.project.takaful.Model.Advertising;
import com.graduatio.project.takaful.R;

import java.util.ArrayList;
import java.util.List;

public class RequestAds extends AppCompatActivity {

    RecyclerView recyclerView;
    RequsetAadpter aadpter;
    List<Advertising> advertisingList;
    Advertising advertising;
    FirebaseFirestore firebaseFirestore;
    CollectionReference reference;
    int LIMIT_REQUEST = 10;
    boolean isLoading;
    Query query;
    ScrollListener scrollListener;
    DocumentSnapshot lastDocSnap;
    CollectionReference userref;
    String adsid;
    Intent intent  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_ads);

        SetUpElement();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        aadpter = new RequsetAadpter(RequestAds.this, advertisingList);


        recyclerView.setAdapter(aadpter);
        ReadAds(true);
    }

    public void SetUpElement() {
//        back = findViewById(R.id.back_btn);
        recyclerView = findViewById(R.id.list);
        advertisingList = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
        intent =getIntent();
        if (intent.hasExtra("id")){
            adsid = intent.getStringExtra("id");
            reference = firebaseFirestore.collection("Advertising")
                    .document(adsid)
                    .collection("Requests");
            query  = reference.whereEqualTo("isdeleted", false);
            
        }else {
            Toast.makeText(this, "Null", Toast.LENGTH_SHORT).show();
        }
        
        userref = FirebaseFirestore.getInstance().collection("Users");

    }


    private class ScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (!isLoading && !recyclerView.canScrollVertically(1) &&
                    newState == RecyclerView.SCROLL_STATE_IDLE) {
                ReadAds(false);
            }
        }
    }

    private void ReadAds(boolean isInitial) {

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
                    advertisingList.addAll(queryDocumentSnapshots.toObjects(Advertising.class));
                } else {
                    advertisingList.addAll(advertisingList.size(), queryDocumentSnapshots.toObjects(Advertising.class));
                }
            }
        }).addOnCompleteListener(task -> {
            if (isInitial) {
                aadpter.notifyDataSetChanged();

                if (task.getResult().size() == LIMIT_REQUEST && scrollListener == null) {
                    recyclerView.addOnScrollListener(scrollListener = new ScrollListener());
                }

                reference.whereEqualTo("isdeleted", false)
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (value != null) {
                                    for (DocumentChange dc : value.getDocumentChanges()) {
                                        if (dc.getType() == DocumentChange.Type.REMOVED) {
                                            for (Advertising advertising : advertisingList) {
                                                if (advertising.getAdd_ID().equals(dc.getDocument().getId())) {
                                                    final int index = advertisingList.indexOf(advertising);
                                                    advertisingList.remove(index);
                                                    aadpter.notifyItemRemoved(index);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        });

            } else {

                final int resultSize = task.getResult().size();

                aadpter.notifyItemRangeInserted(advertisingList.size() - resultSize, resultSize);
                if (resultSize < LIMIT_REQUEST && scrollListener != null) {
                    recyclerView.removeOnScrollListener(scrollListener);
                }
            }


            isLoading = false;
        });
    }

//    public void ReadUserData() {
//        userref.document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                adsid = documentSnapshot.getString("adsID");
//
//             }
//        });
//    }
}