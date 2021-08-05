package com.graduatio.project.takaful.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.graduatio.project.takaful.Actvities.DonationDetails;
import com.graduatio.project.takaful.Fragments.DonationInforamtiomFragment;
import com.graduatio.project.takaful.Fragments.LastStepFragment;
import com.graduatio.project.takaful.Fragments.MyAdsDetailsFragment;
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

        int remeining  = advertising.getTarget()-advertising.getRemaining();
        int percnt  = (remeining/1000)*10;
        holder.progressBar.setProgress(percnt);
        holder.percent.setText(percnt+"%");
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager =  ((FragmentActivity) v.getContext()).getSupportFragmentManager();
                DialogFragment fragment = MyAdsDetailsFragment.myAdsDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("adsId",advertising.getAdd_ID());
                fragment.setArguments(bundle);
                Toast.makeText(context, "IDD : "+ advertising.getAdd_ID(), Toast.LENGTH_SHORT).show();
                manager.beginTransaction().replace(R.id.container,fragment).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return advertisings.size();
    }

    public class MyAdsHolder extends RecyclerView.ViewHolder {
        TextView rimining,traget,title,percent;
        ImageView imageView ;
        ProgressBar progressBar;
        public MyAdsHolder(@NonNull View itemView) {
            super(itemView);
            rimining = itemView.findViewById(R.id.ads_riming);
            traget = itemView.findViewById(R.id.ads_trage);
            title = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.ads_image);
            percent = itemView.findViewById(R.id.percent);
            progressBar = itemView.findViewById(R.id.progress);

        }
    }
}
