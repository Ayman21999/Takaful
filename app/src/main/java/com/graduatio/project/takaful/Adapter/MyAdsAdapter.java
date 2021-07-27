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

public class MyAdsAdapter extends RecyclerView.Adapter<MyAdsAdapter.MyAdsHolder> {
    Context context;
    List<Advertising> advertisings;

    public MyAdsAdapter(Context context, List<Advertising> advertisings) {
        this.context = context;
        this.advertisings = advertisings
        ;
    }

    @NonNull
    @Override
    public MyAdsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.charity_item,parent,false);
        return new MyAdsAdapter.MyAdsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdsHolder holder, int position) {
        Advertising advertising = advertisings.get(position);
        holder.title.setText(advertising.getTitle());
        holder.traget.setText(advertising.getTarget()+"");
        holder.rimining.setText(advertising.getRemaining()+"");
        Picasso.get().load(advertising.getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return advertisings.size();
    }

    public class MyAdsHolder extends RecyclerView.ViewHolder {
        TextView rimining,traget,title;
        ImageView imageView ;
        public MyAdsHolder(@NonNull View itemView) {
            super(itemView);
            rimining = itemView.findViewById(R.id.ads_riming);
            traget = itemView.findViewById(R.id.ads_trage);
            title = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.ads_image);

        }
    }
}
