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

import org.w3c.dom.Text;

import java.util.List;

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.DonationHolder> {

    Context context ;
    List<Donations>donations;
    CollectionReference reference = FirebaseFirestore.getInstance().collection("Advertising");
    public DonationAdapter( Context context ,List<Donations>donation){
        this.context= context ;
        this.donations = donation;

    }
    @NonNull
    @Override
    public DonationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.donation_my_activityies_item,parent,false);

        return new DonationAdapter.DonationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonationHolder holder, int position) {
    Donations donation = donations.get(position);
    holder.totla.setText("$"+donation.getTotal());
    holder.name.setText(donation.getDonateforAds());
    reference.document(donation.getAdsid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
        @Override
        public void onSuccess(DocumentSnapshot documentSnapshot) {
            String title = documentSnapshot.getString("title");
            holder.title.setText(title);

        }
    });

    }

    @Override
    public int getItemCount() {
        return donations.size();
    }

    public class DonationHolder extends RecyclerView.ViewHolder {
        TextView totla,name ,title;
        public DonationHolder(@NonNull View itemView) {
            super(itemView);
            totla = itemView.findViewById(R.id.donation_money);
            name = itemView.findViewById(R.id.ad_name);
            title = itemView.findViewById(R.id.ad_title);
        }
    }
}
