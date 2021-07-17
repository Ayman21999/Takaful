package com.graduatio.project.takaful.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.graduatio.project.takaful.Adapter.AddAadapter;
import com.graduatio.project.takaful.DataBase.SessionManager;
import com.graduatio.project.takaful.Model.Advertising;
import com.graduatio.project.takaful.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    RecyclerView recyclerView;
    TextView usernameTv;
    CollectionReference reference;
    FirebaseFirestore firebaseFirestore;
    private static final int Adverstitong_LIMIT = 10;
    private boolean isLoadingMessages;
    private Query query;
    //    private SwipeRefreshLayout swipeRefresh;
    private DocumentSnapshot lastDocSnap;
    AddAadapter addAadapter;
    private PostsBottomScrollListener scrollListener;

    List<Advertising> advertisings;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        advertisings = new ArrayList<>();
        addAadapter = new AddAadapter(getContext(), advertisings);
        Toast.makeText(getContext(), "Size of Array" + advertisings, Toast.LENGTH_SHORT).show();

        firebaseFirestore = FirebaseFirestore.getInstance();
        query = firebaseFirestore.collection("Advertising")
                //.orderBy("endDate", Query.Direction.DESCENDING)
                .limit(Adverstitong_LIMIT);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        usernameTv = view.findViewById(R.id.username);
//        swipeRefresh = view.findViewById(R.id.swipeRefresh);
//        swipeRefresh.setOnRefreshListener(this);
        SessionManager sessionManager = new SessionManager(getContext());
        HashMap<String, String> hashMap = sessionManager.getUserDetailsSession();
        String username = hashMap.get(SessionManager.KEY_EMAIL);

        usernameTv.setText("Hello " + username);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null && !user.isAnonymous()) {
//            if(GlobalVariables.getInstance().getRole()!=null){
//                if (GlobalVariables.getInstance().getRole().equals("Admin") ||
//                        GlobalVariables.getInstance().getRole().equals("Publisher")) {
//                    floatingButton.setVisibility(View.VISIBLE);
        //               }
//          }
        //      }
        recyclerView.setAdapter(addAadapter);
        ReadAdverting(true);
    }

    private void ReadAdverting(boolean isInitial) {

        isLoadingMessages = true;


//        swipeRefresh.setRefreshing(true);

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

                //    addNewsDeleteListener();

                addAadapter.notifyDataSetChanged();

                if (task.getResult().size() == Adverstitong_LIMIT && scrollListener == null) {
                    recyclerView.addOnScrollListener(scrollListener = new PostsBottomScrollListener());
                }

            } else {

                final int resultSize = task.getResult().size();

                addAadapter.notifyItemRangeInserted(advertisings.size() - resultSize, resultSize);
                if (resultSize < Adverstitong_LIMIT && scrollListener != null) {
                    recyclerView.removeOnScrollListener(scrollListener);
                }
            }

            //  swipeRefresh.setRefreshing(false);

            isLoadingMessages = false;
        });
    }

    @Override
    public void onRefresh() {
        advertisings.clear();
        addAadapter.notifyDataSetChanged();
        addAadapter.loadingItems.clear();
        lastDocSnap = null;
        ReadAdverting(true);

    }

    private class PostsBottomScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (!isLoadingMessages &&
                    !recyclerView.canScrollVertically(1) &&
                    newState == RecyclerView.SCROLL_STATE_IDLE) {

                Log.d("ttt", "is at bottom");

                ReadAdverting(false);

            }
        }
    }


}