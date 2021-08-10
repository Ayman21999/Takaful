package com.graduatio.project.takaful.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.graduatio.project.takaful.Actvities.DonationDetails;
import com.graduatio.project.takaful.Model.Advertising;
import com.graduatio.project.takaful.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHoleder> implements Filterable {

    Context context;
    List<Advertising> advertisings;
    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

    ArrayList<Advertising> filterads;
    private AdsFilter adsFilter;
    CollectionReference reference  = FirebaseFirestore.getInstance().collection("Uesrs");
    public SearchAdapter(Context context, List<Advertising> advertisings) {
        this.context = context;
        this.advertisings = advertisings;
        filterads = new ArrayList<>();
        filterads.addAll(advertisings);
    }

    @NonNull
    @Override
    public SearchHoleder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_item, parent, false);
        return new SearchAdapter.SearchHoleder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHoleder holder, int position) {
        Advertising advertising = filterads.get(position);
        holder.title.setText(advertising.getTitle());
        int remeining  = advertising.getTarget()-advertising.getRemaining();
        int percnt  = (remeining/1000)*10;
        holder.seekBar.setProgress(percnt);
        holder.percent.setText(percnt+"%");
        reference.document(advertising.getUserId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name  = documentSnapshot.getString("firstName");
                holder.usernamePublished.setText(name);
            }
        });
        holder.remingin.setText(advertising.getRemaining()+"");
        holder.dayleft.setText(advertising.getDaynumber()+context.getString(R.string.dayleft));
        Picasso.get().load(advertising.getImage()).into(holder.ad_image);
        holder.ad_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , DonationDetails.class);
                intent.putExtra("id",advertising.getAdd_ID());
                intent.putExtra("donerid",id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterads.size();
    }

    @Override
    public Filter getFilter() {
        if (adsFilter == null){
            adsFilter = new AdsFilter();
        }

        return adsFilter;
    }

    public class SearchHoleder extends RecyclerView.ViewHolder {

        ImageView ad_image;
        TextView title,usernamePublished,remingin,dayleft,percent;
        SeekBar seekBar ;
        public SearchHoleder(@NonNull View itemView) {
            super(itemView);

            ad_image = itemView.findViewById(R.id.ad_img);
            title = itemView.findViewById(R.id.title);
            usernamePublished = itemView.findViewById(R.id.username);
            remingin = itemView.findViewById(R.id.remenig);
            dayleft = itemView.findViewById(R.id.dayleft);
            percent = itemView.findViewById(R.id.percnet);
            seekBar = itemView.findViewById(R.id.seekBar);


        }
    }

    class AdsFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();

            ArrayList<Advertising> fAdvrtising = new ArrayList<>();
            if(constraint.length() ==0){
                filterResults.values = fAdvrtising;
                filterResults.count = fAdvrtising.size();
                return filterResults;
            }

            for (Advertising a : advertisings) {
                if (a.getTitle().contains(constraint)) {
                    fAdvrtising.add(a);
                }
            }

            filterResults.values = fAdvrtising;
            filterResults.count = fAdvrtising.size();

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filterads = (ArrayList<Advertising>) results.values;
            notifyDataSetChanged();
        }
    }
    public void update(ArrayList<Advertising> advertisings){
        this.advertisings.clear();
        this.advertisings.addAll(advertisings);
        notifyDataSetChanged();
    }
}

