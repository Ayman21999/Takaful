package com.graduatio.project.takaful.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.graduatio.project.takaful.Actvities.DonationDetails;
import com.graduatio.project.takaful.Fragments.DonationInforamtiomFragment;
import com.graduatio.project.takaful.Model.Advertising;
import com.graduatio.project.takaful.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AddAadapter extends RecyclerView.Adapter<AddAadapter.AddHolder> {
    Context context;
    List<Advertising> advertisings;
    String id = FirebaseAuth.getInstance().getUid();
    public List<Integer> loadingItems = new ArrayList<>();
    public AddAadapter(Context context, List<Advertising> advertisings) {
        this.context = context;
        this.advertisings = advertisings;
    }

    @NonNull
    @Override
    public AddHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adversiting_item, parent, false);

        return new AddAadapter.AddHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddHolder holder, int position) {
        Advertising advertising = advertisings.get(position);
        holder.title.setText(advertising.getTitle());
        Picasso.get().load(advertising.getImage()).into(holder.imageView);
        int remeining  = advertising.getTarget()-advertising.getRemaining();
        int percnt  = (remeining/1000)*10;
//       holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//           @Override
//           public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//           }
//
//           @Override
//           public void onStartTrackingTouch(SeekBar seekBar) {
//
//           }
//
//           @Override
//           public void onStopTrackingTouch(SeekBar seekBar) {
//
//           }
//       });
        holder.seekBar.setProgress(percnt);
        holder.percent.setText("(%"+percnt+")");
//        holder.percent.setText(advertising.getRemaining() + "");
        holder.target.setText("$ "+advertising.getTarget());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, advertising.getAdd_ID(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context , DonationDetails.class);
                    intent.putExtra("donerid", id);
                intent.putExtra("id", advertising.getAdd_ID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return advertisings.size();
    }

    public class AddHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imageView;
        TextView target;
        TextView percent;
        SeekBar seekBar;
        ConstraintLayout constraintLayout;
        public AddHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.ad_name);
            imageView = itemView.findViewById(R.id.add_image);
            target = itemView.findViewById(R.id.targetnum);
            percent = itemView.findViewById(R.id.percent);
            seekBar = itemView.findViewById(R.id.seekBar);
            constraintLayout = itemView.findViewById(R.id.item);

        }
    }
}
