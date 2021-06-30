package com.graduatio.project.takaful.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.graduatio.project.takaful.Model.Advertising;
import com.graduatio.project.takaful.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class AddAadapter extends RecyclerView.Adapter<AddAadapter.AddHolder> {

    Context context;
    List<Advertising> advertisings;

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
        holder.percent.setText(advertising.getRemaining() + "");
        holder.target.setText(advertising.getTarget() + "");
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

        public AddHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.ad_name);
            imageView = itemView.findViewById(R.id.add_image);
            target = itemView.findViewById(R.id.targetnum);
            percent = itemView.findViewById(R.id.percent);
            seekBar = itemView.findViewById(R.id.seekBar);

        }
    }
}
