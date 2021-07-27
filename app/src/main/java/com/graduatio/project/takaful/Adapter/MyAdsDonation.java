package com.graduatio.project.takaful.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.graduatio.project.takaful.Model.Donations;
import com.graduatio.project.takaful.R;

import java.util.List;

public class MyAdsDonation extends RecyclerView.Adapter<MyAdsDonation.MyAdsDonationHolder> {
    Context context ;
    List<Donations>donations;
    CollectionReference reference = FirebaseFirestore.getInstance().collection("Users");
    public MyAdsDonation( Context context ,List<Donations>donation){
        this.context= context ;
        this.donations = donation;

    }
    @NonNull
    @Override
    public MyAdsDonationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.donation_my_activityies_item,parent,false);

        return new MyAdsDonation.MyAdsDonationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdsDonationHolder holder, int position) {
        Donations donation = donations.get(position);
        holder.totla.setText("$"+donation.getTotal());
        reference.document(donation.getUserID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
            String name = documentSnapshot.getString("firstName");
            holder.title.setText(name);

            }
        });

    }

    @Override
    public int getItemCount() {
        return donations.size();
    }

    public class MyAdsDonationHolder extends RecyclerView.ViewHolder {
        TextView totla ,title;
        public MyAdsDonationHolder(@NonNull View itemView) {
            super(itemView);
            totla = itemView.findViewById(R.id.donation_money);
            title = itemView.findViewById(R.id.ad_title);
        }
    }
}
