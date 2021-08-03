package com.graduatio.project.takaful.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.graduatio.project.takaful.Model.PayMethod;
import com.graduatio.project.takaful.R;

import java.util.List;

import static com.graduatio.project.takaful.R.*;

public class ShowPayAdapter extends RecyclerView.Adapter<ShowPayAdapter.ShowVH> {

    CollectionReference reference;
    Context context ;
    List<PayMethod>methods;


    public ShowPayAdapter( Context context,List<PayMethod>methods){
        this.context = context;
        this.methods = methods;


    }

    @NonNull
    @Override
    public ShowVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layout.pay_method_info_item,parent , false);

        return new ShowPayAdapter.ShowVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowVH holder, int position) {
        PayMethod method = methods.get(position);
        String type = method.getCardtype();
        if (type.equals("Visa")){
            Drawable placeholder = holder.imageView.getContext().getResources().getDrawable(R.drawable.ic_visa);
            holder.imageView.setImageDrawable(placeholder);
            holder.email.setText(method.getCardNumber());
            holder.password.setText(method.getCvvCode());

        }else {
                if (type.equals("pal")){
                    Drawable placeholder = holder.imageView.getContext().getResources().getDrawable(drawable.ic_paypal);
                    holder.imageView.setImageDrawable(placeholder);
                    holder.email.setText(method.getEmail());
                    holder.password.setText(method.getPass());

                }else {
                    Drawable placeholder = holder.imageView.getContext().getResources().getDrawable(drawable.ic_googlewallet);
                    holder.imageView.setImageDrawable(placeholder);
                    holder.email.setText(method.getEmail());
                    holder.password.setText(method.getPass());
                }


        }

    }

    @Override
    public int getItemCount() {
        return methods.size();
    }

    public class ShowVH extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView email,password;
        public ShowVH(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(id.type);
            email = itemView.findViewById(id.email);
            password = itemView.findViewById(id.password);

        }
    }
}
