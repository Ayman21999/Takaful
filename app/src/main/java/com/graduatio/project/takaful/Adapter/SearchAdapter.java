package com.graduatio.project.takaful.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.graduatio.project.takaful.Model.Advertising;
import com.graduatio.project.takaful.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHoleder> {

    Context context;
    List<Advertising> advertisings;

    public SearchAdapter(Context context, List<Advertising> advertisings) {
        this.context = context;
        this.advertisings = advertisings;
    }

    @NonNull
    @Override
    public SearchHoleder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_item, parent, false);
        return new SearchAdapter.SearchHoleder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHoleder holder, int position) {
        Advertising advertising = advertisings.get(position);
        holder.title.setText(advertising.getTarget());
//        holder.usernamePublished.setText(advertising.ge);
        holder.percent.setText(advertising.getTarget());
        holder.dayleft.setText(advertising.getDaynumber());
        Picasso.get().load(advertising.getImage()).into(holder.ad_image);
    }

    @Override
    public int getItemCount() {
        return advertisings.size();
    }

    public class SearchHoleder extends RecyclerView.ViewHolder {

        ImageView ad_image ;
        TextView title ;
        TextView usernamePublished;
        TextView remingin ;
        TextView dayleft;
        TextView percent;


        public SearchHoleder(@NonNull View itemView) {
            super(itemView);

            ad_image = itemView.findViewById(R.id.ad_img);
            title = itemView.findViewById(R.id.title);
            usernamePublished = itemView.findViewById(R.id.username);
            remingin  = itemView.findViewById(R.id.remenig);
            dayleft = itemView.findViewById(R.id.dayleft);
            percent = itemView.findViewById(R.id.percnet);


        }
    }
}
