package com.graduatio.project.takaful.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.graduatio.project.takaful.Actvities.Users_HasCharities;
import com.graduatio.project.takaful.Model.Advertising;
import com.graduatio.project.takaful.Model.User;
import com.graduatio.project.takaful.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UsersHasCharity extends RecyclerView.Adapter<UsersHasCharity.UserHasCharityVH> {
    List<User> users;
    Context context;
    CollectionReference userref = FirebaseFirestore.getInstance().collection("Users");
    boolean isblocked = false;
    CollectionReference reference = FirebaseFirestore.getInstance().collection("Advertising");

    public UsersHasCharity(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public UserHasCharityVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_block_item, parent, false);

        return new UsersHasCharity.UserHasCharityVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHasCharityVH holder, int position) {
        ProgressDialog progressDialog = new ProgressDialog(context);

        User user = users.get(position);
        String userid = user.getUserId();
        holder.username.setText(user.getFirstName());
//        Picasso.get().load(user.getUserImage()).into(holder.userimage);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();

        userref.document(userid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                isblocked = documentSnapshot.getBoolean("isBlocked");
                progressDialog.dismiss();
                if (isblocked == true) {
                    holder.active_btn.setClickable(false);
                    holder.block_btn.setBackground(context.getResources().getDrawable(R.drawable.disable_btn));
                    holder.active_btn.setBackground(context.getResources().getDrawable(R.drawable.accept_brn));
                } else if (isblocked == false) {
                    holder.block_btn.setClickable(true);
                    holder.active_btn.setBackground(context.getResources().getDrawable(R.drawable.disable_btn));
                    holder.block_btn.setBackground(context.getResources().getDrawable(R.drawable.reject_btn));
                }
            }
        });

        holder.block_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Loading ...");
                progressDialog.show();
                userref.document(userid).update("isBlocked", true,
                        "isSuspend", true,
                        "isHasCharity",false)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                userref.document(userid).collection("UserAds").document(user.getAdsID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        reference.document(user.getAdsID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(context, "The Users has been blocked", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                                holder.block_btn.setClickable(false);
                                                holder.active_btn.setClickable(true);
                                                holder.block_btn.setBackground(context.getResources().getDrawable(R.drawable.disable_btn));
                                                holder.active_btn.setBackground(context.getResources().getDrawable(R.drawable.accept_brn));
                                                Toast.makeText(context, "The user is Blocked now", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error :" + e.getMessage(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();

                    }
                });
            }
        });
        holder.active_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Loading ...");
                progressDialog.show();
                userref.document(userid).update("isBlocked", false, "isSuspend", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(context, "The user is Activated now", Toast.LENGTH_SHORT).show();
                        holder.active_btn.setClickable(false);
                        holder.block_btn.setClickable(true);
                        holder.active_btn.setBackground(context.getResources().getDrawable(R.drawable.disable_btn));
                        holder.block_btn.setBackground(context.getResources().getDrawable(R.drawable.reject_btn));
                        progressDialog.dismiss();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error :" + e.getMessage(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserHasCharityVH extends RecyclerView.ViewHolder {
        TextView username;
        ImageView userimage;
        Button block_btn, active_btn;

        public UserHasCharityVH(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            userimage = itemView.findViewById(R.id.imageuser);
            block_btn = itemView.findViewById(R.id.blockbtn);
            active_btn = itemView.findViewById(R.id.active_btn);
        }
    }
}
