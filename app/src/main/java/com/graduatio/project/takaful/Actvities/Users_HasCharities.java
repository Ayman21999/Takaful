package com.graduatio.project.takaful.Actvities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.graduatio.project.takaful.Adapter.AdminAdapter;
import com.graduatio.project.takaful.Adapter.UsersHasCharity;
import com.graduatio.project.takaful.Model.Advertising;
import com.graduatio.project.takaful.Model.User;
import com.graduatio.project.takaful.R;

import java.util.ArrayList;
import java.util.List;

public class Users_HasCharities extends AppCompatActivity {


    UsersHasCharity adapter ;
    RecyclerView list;
    List<User> advertisingList;

    Advertising advertising;
    FirebaseFirestore firebaseFirestore;
    CollectionReference reference;
    int LIMIT_REQUEST = 10;
    boolean isLoading;
    Query query;
    ScrollListener scrollListener;
    DocumentSnapshot lastDocSnap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users__has_charities);
        SetUpElement();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        list.setLayoutManager(manager);
        adapter = new UsersHasCharity(Users_HasCharities.this, advertisingList);
        list.setAdapter(adapter);
        ReadUsers(true);

    }
    public void SetUpElement() {
        list = findViewById(R.id.list);
        advertisingList = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
        reference = firebaseFirestore.collection("Users");
        query = reference.whereEqualTo("isHasCharity", true);

    }

    private class ScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (!isLoading && !recyclerView.canScrollVertically(1) &&
                    newState == RecyclerView.SCROLL_STATE_IDLE) {
                ReadUsers(false);
            }
        }
    }

    private void ReadUsers(boolean isInitial) {

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
                    advertisingList.addAll(queryDocumentSnapshots.toObjects(User.class));
                } else {
                    advertisingList.addAll(advertisingList.size(), queryDocumentSnapshots.toObjects(User.class));
                }
            }
        }).addOnCompleteListener(task -> {
            if (isInitial) {
                adapter.notifyDataSetChanged();

                if (task.getResult().size() == LIMIT_REQUEST && scrollListener == null) {
                    list.addOnScrollListener(scrollListener = new ScrollListener());
                }

//                reference.whereEqualTo("isRejected", true)
//                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                            @Override
//                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                                if (value != null) {
//                                    for (DocumentChange dc : value.getDocumentChanges()) {
//                                        if (dc.getType() == DocumentChange.Type.REMOVED) {
//                                            for (Advertising advertising : advertisingList) {
//                                                if (advertising.getAdd_ID().equals(dc.getDocument().getId())) {
//                                                    final int index = advertisingList.indexOf(advertising);
//                                                    advertisingList.remove(index);
//                                                    adapter.notifyItemRemoved(index);
//                                                    break;
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        });

            } else {

                final int resultSize = task.getResult().size();

                adapter.notifyItemRangeInserted(advertisingList.size() - resultSize, resultSize);
                if (resultSize < LIMIT_REQUEST && scrollListener != null) {
                    list.removeOnScrollListener(scrollListener);
                }
            }


            isLoading = false;
        });
    }

}