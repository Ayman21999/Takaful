package com.graduatio.project.takaful.Adapter;

import android.content.Context;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.graduatio.project.takaful.Model.Advertising;
import com.graduatio.project.takaful.R;
import com.graduatio.project.takaful.Service.CloudMessagingNotificationsSender;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RequsetAadpter extends RecyclerView.Adapter<RequsetAadpter.RequestVH> {
    List<Advertising> advertisings;
    Context context;
    CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Advertising");
    CollectionReference userRef = FirebaseFirestore.getInstance().collection("Users");

    public RequsetAadpter(Context context, List<Advertising> advertisings) {
        this.context = context;
        this.advertisings = advertisings;
    }

    @NonNull
    @Override
    public RequestVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request_item_donatios, parent, false);

        return new RequsetAadpter.RequestVH(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RequestVH holder, int position) {
        Advertising advertising = advertisings.get(position);
//        String id =FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef.document(advertising.getUserId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name = documentSnapshot.getString("firstName");
                String image = documentSnapshot.getString("userImage");
                holder.name.setText(name);
                Picasso.get().load(image).into(holder.image);

            }
        });
        collectionReference.document(advertising.getAdd_ID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String title = documentSnapshot.getString("title");

                holder.title.setText(title);
            }
        });
        holder.total.setText("$" + advertising.getTotal());


        holder.donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectionReference.document(advertising.getAdd_ID())
                        .collection("Requests").document(advertising.getUserId())
                        .update("donated", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Successful accepted request", Toast.LENGTH_SHORT).show();
                        CloudMessagingNotificationsSender.Data data =
                                new CloudMessagingNotificationsSender.Data
                                        ("ss", "asd", "asd", "ds", "Asd", 55);

                        CloudMessagingNotificationsSender.sendNotification(FirebaseAuth.getInstance().getCurrentUser().getUid(), data);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectionReference.document(advertising.getAdd_ID())
                        .collection("Requests").document(advertising.getUserId())
                        .update("isdeleted", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Deleted request", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return advertisings.size();
    }

    public class RequestVH extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, title, total;
        Button donate, reject;

        public RequestVH(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.userimage);
            name = itemView.findViewById(R.id.username);
            title = itemView.findViewById(R.id.ads_title);
            total = itemView.findViewById(R.id.total);
            donate = itemView.findViewById(R.id.donate);
            reject = itemView.findViewById(R.id.reject);

        }
    }
}
