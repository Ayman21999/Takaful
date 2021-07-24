package com.graduatio.project.takaful.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.graduatio.project.takaful.Model.PayMethod;
import com.graduatio.project.takaful.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PayAdapter extends RecyclerView.Adapter<PayAdapter.PayHolder> {

    Context context ;
    List<PayMethod>methods;
    public PayAdapter(Context context,List<PayMethod>methods ){
        this.context = context ;
        this.methods = methods ;

    }
    @NonNull
    @Override
    public PayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pay_method_item,parent ,false);
        return new PayAdapter.PayHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PayHolder holder, int position) {
    PayMethod payMethod = methods.get(position);
    holder.secritnum.setText(payMethod.getCvvCode());
    holder.cardnumber.setText(payMethod.getCardNumber());
    Picasso.get().load(payMethod.getImg()).into(holder.paytype);

    }

    @Override
    public int getItemCount() {
        return methods.size();
    }

    public class PayHolder extends RecyclerView.ViewHolder {
        ImageView paytype ;
        TextView cardnumber ;
        TextView secritnum;

        public PayHolder(@NonNull View itemView) {
            super(itemView);

            paytype = itemView.findViewById(R.id.pay_img);
            cardnumber = itemView.findViewById(R.id.cardnumber);
            secritnum = itemView.findViewById(R.id.secritnumber);

        }
    }
}
