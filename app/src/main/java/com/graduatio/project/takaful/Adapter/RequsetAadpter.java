package com.graduatio.project.takaful.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.graduatio.project.takaful.Actvities.DonationDetails;
import com.graduatio.project.takaful.Actvities.UserInformation;
import com.graduatio.project.takaful.Model.Advertising;
import com.graduatio.project.takaful.R;
import com.graduatio.project.takaful.Service.CloudMessagingNotificationsSender;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class RequsetAadpter extends RecyclerView.Adapter<RequsetAadpter.RequestVH> {
    List<Advertising> advertisings;
    Context context;
    CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("Advertising");
    CollectionReference userRef = FirebaseFirestore.getInstance().collection("Users");
    String donationtotal;
    String userid;
    String ads_id;

    public RequsetAadpter(Context context, List<Advertising> advertisings) {
        this.context = context;
        this.advertisings = advertisings;
    }

    @NonNull
    @Override
    public RequestVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.request_item_donatios,
                parent, false);

        return new RequsetAadpter.RequestVH(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RequestVH holder, int position) {
        Advertising advertising = advertisings.get(position);
//        String id =FirebaseAuth.getInstance().getCurrentUser().getUid();
        userid = advertising.getUserId();
        ads_id = advertising.getAdd_ID();
        userRef.document(userid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name = documentSnapshot.getString("firstName");
                String image = documentSnapshot.getString("userImage");
                holder.name.setText(name);
//                if (image.equals("")) {
//                    Toast.makeText(context, "null image", Toast.LENGTH_SHORT).show();
//                }
//                Picasso.get().load(image).into(holder.image);

            }
        });
        collectionReference.document(ads_id).
                get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String title = documentSnapshot.getString("title");

                holder.title.setText(title);
            }
        });
        holder.total.setText("$" + advertising.getTotal());

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(context, UserInformation.class);
                intent.putExtra("id",userid);
                context.startActivity(intent);
            }
        });

        holder.donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MakeRequestNumber();

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
                        Toast.makeText(context, context.getString(R.string.deleteReq), Toast.LENGTH_SHORT).show();
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

    public void MakeRequestNumber() {
        final BottomSheetDialog bsd = new BottomSheetDialog(context, R.style.SheetDialog);
        final View parentView = LayoutInflater.from(context).
                inflate(R.layout.bottom_sheet_number, null, false);
        parentView.setBackgroundColor(Color.TRANSPARENT);
        EditText number = parentView.findViewById(R.id.number);

        parentView.findViewById(R.id.donate_btn).setOnClickListener(view -> {
            donationtotal = number.getText() + "";
            if (donationtotal.isEmpty() && donationtotal.equals("")) {

                Toast.makeText(context, context.getString(R.string.emptynumber), Toast.LENGTH_SHORT).show();
            } else {
                MakeRequest(donationtotal);
                bsd.dismiss();

            }


        });
        parentView.findViewById(R.id.close).setOnClickListener(view -> {
            bsd.dismiss();
        });
        bsd.setContentView(parentView);
        bsd.show();
    }

    public void MakeRequest(String total) {
        CollectionReference reference = FirebaseFirestore.getInstance()
                .collection("Advertising").document(ads_id).collection("Requests");
        reference.document(userid).update("total", donationtotal).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                collectionReference.document(ads_id)
                        .collection("Requests").document(userid)
                        .update("donated", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, context.getString(R.string.success), Toast.LENGTH_SHORT).show();
                        CloudMessagingNotificationsSender.Data data =
                                new CloudMessagingNotificationsSender.Data
                                        (FirebaseAuth.getInstance().getCurrentUser().getUid()
                                                , "Message ",
                                                "Your request is Accepted please contact us to get your money ",
                                                "", userid, 55);


                        CloudMessagingNotificationsSender.sendNotification(FirebaseAuth.getInstance().getCurrentUser().getUid(), data);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });


    }

}
