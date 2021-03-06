package com.graduatio.project.takaful.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.graduatio.project.takaful.Actvities.DonationDetails;
import com.graduatio.project.takaful.Model.Advertising;
import com.graduatio.project.takaful.R;
import com.graduatio.project.takaful.Service.CloudMessagingNotificationsSender;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.AdminHolder> {

    Context context;
    List<Advertising> advertisings;
    CollectionReference firebaseFirestore = FirebaseFirestore.getInstance().collection("Advertising");
    CollectionReference userRef = FirebaseFirestore.getInstance().collection("Users");

    public AdminAdapter(Context context, List<Advertising> advertisings) {
        this.context = context;
        this.advertisings = advertisings;

    }

    @NonNull
    @Override
    public AdminAdapter.AdminHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_requset, parent, false);
        return new AdminAdapter.AdminHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminAdapter.AdminHolder holder, int position) {
        Advertising advertising = advertisings.get(position);
        holder.name_publisher.setText("By :" + advertising.getUserId());
        holder.target.setText("Target : $" + advertising.getTarget());
        holder.title.setText(advertising.getTitle());

        if (advertising.getImage() != null && !advertising.getImage().isEmpty()) {
            Picasso.get().load(advertising.getImage()).into(holder.ads_img);

        } else {
//                    Toast.makeText(context, "null image", Toast.LENGTH_SHORT).show();
            return;
        }

        holder.ads_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DonationDetails.class);
                i.putExtra("id",advertising.getAdd_ID());
                context.startActivity(i);
            }
        });
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.document(advertising.getAdd_ID()).update("isRejected", false , "isFinished",false).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "The Ads Had been published", Toast.LENGTH_SHORT).show();
                        userRef.document(advertising.getUserId()).update("isHasCharity", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Log.d("sss", "Success");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("sss", "Error :  " + e.getLocalizedMessage());

                            }
                        });
                    }
                });

            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.document(advertising.getAdd_ID()).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "The Ads Had been Deleted", Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });


    }

    @Override
    public int getItemCount() {
        return advertisings.size();
    }

    public class AdminHolder extends RecyclerView.ViewHolder {

        ImageView ads_img;
        TextView title, name_publisher, target;
        Button accept, delete;


        public AdminHolder(@NonNull View itemView) {
            super(itemView);
            ads_img = itemView.findViewById(R.id.ads_image);
            target = itemView.findViewById(R.id.target_ads);
            name_publisher = itemView.findViewById(R.id.ads_publisher);
            title = itemView.findViewById(R.id.title_ads);
            accept = itemView.findViewById(R.id.accepted);
            delete = itemView.findViewById(R.id.delete);


        }
    }
}
