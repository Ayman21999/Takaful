package com.graduatio.project.takaful.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.graduatio.project.takaful.Model.Advertising;
import com.graduatio.project.takaful.R;

import java.util.List;

public class AdsBenefitAdapter extends RecyclerView.Adapter<AdsBenefitAdapter.AdsHolder> {
    List<Advertising> advertisingList;
    Context context;
    CollectionReference reference = FirebaseFirestore.getInstance().collection("Advertising");
    public AdsBenefitAdapter(Context context, List<Advertising> advertisingList) {
        this.context = context;
        this.advertisingList = advertisingList;
    }

    @NonNull
    @Override
    public AdsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.donation_my_activityies_item, parent, false);
        return new AdsBenefitAdapter.AdsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdsHolder holder, int position) {
        Advertising advertising = advertisingList.get(position);
        Task<DocumentSnapshot> userrf = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(advertising.getUserId())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String name = documentSnapshot.getString("firstName");
                        holder.name.setText(name);
                        String total  = documentSnapshot.getString("total");

                    }
                });
//        reference.document(advertising.getAdd_ID()).collection("Requests").document(advertising.getAdd_ID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//           String name  = documentSnapshot.getString("firstName");
//           String total  = documentSnapshot.getString("total");
//            holder.money.setText("$"+total);
//            holder.name.setText(name);
//            }
//        });
        holder.money.setText("$"+advertising.getTotal());
        holder.title.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return advertisingList.size();
    }

    public class AdsHolder extends RecyclerView.ViewHolder {

        TextView name , money,title;
        public AdsHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.ad_name);
            money = itemView.findViewById(R.id.donation_money);
            title = itemView.findViewById(R.id.ad_title);
        }
    }
}
