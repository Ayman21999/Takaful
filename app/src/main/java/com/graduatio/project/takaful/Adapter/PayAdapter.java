package com.graduatio.project.takaful.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.graduatio.project.takaful.Actvities.AddPaymantMethodActivity;
import com.graduatio.project.takaful.Model.PayMethod;
import com.graduatio.project.takaful.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PayAdapter extends RecyclerView.Adapter<PayAdapter.PayHolder> {

    Context context;
    private final static int POLL_LIMIT = 4;
    private ArrayList<String> methods;
    private String method;
    private ScrollToBottomListener scrollToBottomListener;

    public interface ScrollToBottomListener {
        void scrollToBottom();
    }

    public PayAdapter(ArrayList<String> methods, Context context,
                      ScrollToBottomListener scrollToBottomListener) {
        this.methods = methods;
        this.context = context;
        method= "Add Method";

    }

    @NonNull
    @Override
    public PayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PayHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pay_method_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PayHolder holder, int position) {
        holder.bind(position);
    }
    private void addPollItem(int position) {
        methods.add(methods+" "+ (position + 1));
        notifyItemInserted(methods.size());
        scrollToBottomListener.scrollToBottom();
    }

    @Override
    public int getItemCount() {
        return methods.size();
    }

    public class PayHolder extends RecyclerView.ViewHolder {
        private final   Button add;
        public PayHolder(@NonNull View itemView) {
            super(itemView);
            add = itemView.findViewById(R.id.add);

        }
        private void bind(int position){
            add.setText(method);

            if (position == getItemCount() - 1 && position != POLL_LIMIT) {
                add.setVisibility(View.VISIBLE);
                add.setOnClickListener(v -> {
                    Intent intent =new Intent(context , AddPaymantMethodActivity.class);
                    context.startActivity(intent);
                    addPollItem(position + 1);
                });
            } else {
                add.setVisibility(View.INVISIBLE);
                add.setOnClickListener(null);
            }

        }

    }

}
