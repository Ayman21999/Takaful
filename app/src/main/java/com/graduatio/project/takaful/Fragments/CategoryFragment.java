package com.graduatio.project.takaful.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.graduatio.project.takaful.Actvities.AdminNotificationActivity;
import com.graduatio.project.takaful.Actvities.RequestAds;
import com.graduatio.project.takaful.Adapter.AddAadapter;
import com.graduatio.project.takaful.Model.Advertising;
import com.graduatio.project.takaful.Model.User;
import com.graduatio.project.takaful.R;
import com.graduatio.project.takaful.Service.CloudMessagingNotificationsSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoryFragment extends DialogFragment implements SwipeRefreshLayout.OnRefreshListener {


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
    ImageView request,notify,newpost;
    ScrollListener scrollListener;
    String id ;
    List<Advertising> advertisings;
    ProgressDialog progressDialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        advertisings = new ArrayList<>();
        addAadapter = new AddAadapter(getContext(), advertisings);
        firebaseFirestore = FirebaseFirestore.getInstance();
        query = firebaseFirestore.collection("Advertising")
                .whereEqualTo("isRejected", false)
                .limit(Adverstitong_LIMIT);

         id = FirebaseAuth.getInstance().getCurrentUser().getUid();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        request = view.findViewById(R.id.request);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        addAadapter = new AddAadapter(getContext(), advertisings);
        recyclerView.setAdapter(addAadapter);
        progressDialog = new ProgressDialog(getContext());
        usernameTv = view.findViewById(R.id.username);
       newpost = view.findViewById(R.id.newads);
        View view1 = view.findViewById(R.id.ads_back);
        notify = view.findViewById(R.id.notify);
        reference = FirebaseFirestore.getInstance().collection("Advertising");
        User[] users = new User[1];
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        //////////send Notification
//        CloudMessagingNotificationsSender.Data data =
//                new CloudMessagingNotificationsSender.Data
//                        ("ss","asd","asd","ds","Asd",55);
//        CloudMessagingNotificationsSender.sendNotification(FirebaseAuth.getInstance().getCurrentUser().getUid(),data);

      if (FirebaseAuth.getInstance().getCurrentUser() != null){
          FirebaseFirestore.getInstance().collection("Users")
                  .document(id)
                  .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
              @Override
              public void onSuccess(DocumentSnapshot documentSnapshot) {
                  users[0] = documentSnapshot.toObject(User.class);
              }
          }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
              @Override
              public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                  usernameTv.setText("Hello " + users[0].getFirstName());
                  String role = task.getResult().getString("role");
                  if (role.equals("Admin")) {
                      newpost.setVisibility(View.VISIBLE);
                      view1.setVisibility(View.VISIBLE);
                      request.setVisibility(View.INVISIBLE);
                      progressDialog.dismiss();

                  } else if (role.equals("Donors")){
                      newpost.setVisibility(View.INVISIBLE);
                      view1.setVisibility(View.INVISIBLE);
                      request.setVisibility(View.VISIBLE);

                      progressDialog.dismiss();
                  }else {
                      progressDialog.dismiss();

                  }
              }
          });
      }else {
          Toast.makeText(getContext(), "null", Toast.LENGTH_SHORT).show();
      }

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), RequestAds.class);
                String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                CollectionReference reference = FirebaseFirestore.getInstance().collection("Users");
                reference.document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String adsid =  documentSnapshot.getString("adsID");
                        i.putExtra("id",adsid);
                        startActivity(i);
                    }
                });

            }
        });
//        swipeRefresh = view.findViewById(R.id.swipeRefresh);
//        swipeRefresh.setOnRefreshListener(this);
//        SessionManager sessionManager = new SessionManager(getContext());
//        HashMap<String, String> hashMap = sessionManager.getUserDetailsSession();

        newpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AdminNotificationActivity.class);
                getContext().startActivity(intent);
            }
        });
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
                        addAadapter.notifyDataSetChanged();

                        if (task.getResult().size() == Adverstitong_LIMIT && scrollListener == null) {
                            recyclerView.addOnScrollListener(scrollListener = new ScrollListener());
                        }
                        reference.whereEqualTo("isRejected", false)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                        if (value != null) {
                                            for (DocumentChange dc : value.getDocumentChanges()) {
                                                if (dc.getType() == DocumentChange.Type.REMOVED) {
                                                    for (Advertising suggestion : advertisings) {
                                                        if (suggestion.getAdd_ID().equals(dc.getDocument().getId())) {
                                                            final int index = advertisings.indexOf(suggestion);
                                                            advertisings.remove(index);
                                                            addAadapter.notifyItemRemoved(index);
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

                        addAadapter.notifyItemRangeInserted(advertisings.size() - resultSize, resultSize);
                        if (resultSize < Adverstitong_LIMIT && scrollListener != null) {
                            recyclerView.removeOnScrollListener(scrollListener);
                        }
                    }
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

    private class ScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (!isLoadingMessages && !recyclerView.canScrollVertically(1) &&
                    newState == RecyclerView.SCROLL_STATE_IDLE) {
                ReadAdverting(false);
            }
        }
    }

    public static CategoryFragment cFragment() {
        return new CategoryFragment();
    }
}