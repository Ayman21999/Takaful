package com.graduatio.project.takaful.Adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHoleder> {
    @NonNull
    @Override
    public SearchHoleder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHoleder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class SearchHoleder extends RecyclerView.ViewHolder {
        public SearchHoleder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
