package com.graduatio.project.takaful.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.graduatio.project.takaful.Adapter.MyAdsAdapter;
import com.graduatio.project.takaful.Model.Advertising;
import com.graduatio.project.takaful.Model.Donations;
import com.graduatio.project.takaful.R;

import java.util.ArrayList;
import java.util.List;

public class DonationFragment extends DialogFragment {

    ImageView add_charity;
    FirebaseFirestore firebaseFirestore ;
    Task<DocumentSnapshot> collectionReference;
    FirebaseAuth auth ;
    TextView message ,message2;
    List<Advertising> advertisingList;
    MyAdsAdapter adsAdapter;
    boolean isLoading;
    private PostsBottomScrollListener scrollListener;
    Query query;
    int donatoin_LIMIT = 10;
    private DocumentSnapshot lastDocSnap;
    View view2;
    RecyclerView recyclerView ;
    String userid;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogTheme);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donation, container, false);
        add_charity = view.findViewById(R.id.add_charity);
        message = view.findViewById(R.id.message);
        message2 = view.findViewById(R.id.message2);
        view2 = view.findViewById(R.id.view18);
        advertisingList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        recyclerView = view.findViewById(R.id.list);
        firebaseFirestore = FirebaseFirestore.getInstance() ;
         userid = auth.getCurrentUser().getUid();
        query = firebaseFirestore.collection("Users").document(userid).collection("UserAds");

         adsAdapter =new MyAdsAdapter(getContext(),advertisingList);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adsAdapter);
        ReadAds(true);

        collectionReference =firebaseFirestore.collection("Users")
                .document(userid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        boolean isHasAds =documentSnapshot.getBoolean("isHasCharity");
                        if (isHasAds == true){
                            message.setVisibility(View.GONE);
                            message2.setVisibility(View.GONE);
                            add_charity.setVisibility(View.GONE);
                            view2.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);


                        }else {
                            message.setVisibility(View.VISIBLE);
                            message2.setVisibility(View.VISIBLE);
                            add_charity.setVisibility(View.VISIBLE);
                            view2.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    }
                });


        add_charity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment  = SelectTypeCharityFragment.sFragment();
                dialogFragment.show(getChildFragmentManager(),"dd");
            }
        });
        return view;

    }


    public void ReadAds(boolean isInitial){

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
                adsAdapter.notifyDataSetChanged();
                if (task.getResult().size() == donatoin_LIMIT && scrollListener == null) {
                    recyclerView.addOnScrollListener(scrollListener = new PostsBottomScrollListener());
                }
            } else {
                final int resultSize = task.getResult().size();
                adsAdapter.notifyItemRangeInserted(advertisingList.size() - resultSize, resultSize);
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

                ReadAds(false);

            }
        }
    }
}