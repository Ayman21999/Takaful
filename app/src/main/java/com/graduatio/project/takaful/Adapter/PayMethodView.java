//package com.graduatio.project.takaful.Adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.graduatio.project.takaful.R;
//
//import java.util.ArrayList;
//
//public class PayMethodView RecyclerView.Adapter{
//
//        Context context;
//    private final static int POLL_LIMIT = 4;
//    private ArrayList<String> methods;
//    private String method;
//    private ScrollToBottomListener scrollToBottomListener;
//
//    public interface ScrollToBottomListener {
//        void scrollToBottom();
//    }
//
//    public PayMethodView(ArrayList<String> methods, Context context,
//                         ScrollToBottomListener scrollToBottomListener) {
//        this.methods = methods;
//       this.context = context;
//        this.scrollToBottomListener = scrollToBottomListener;
//
//        method = "add new method";
//
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
//    }
//
//    @NonNull
//    @Override
//    public PayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new PayHolder(LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.pay_method_item,parent,false));
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull PayHolder holder, int position) {
//        holder.bind(position);
//    }
//    private void addPollItem(int position) {
//        methods.add(methods+" "+ (position + 1));
//        notifyItemInserted(methods.size());
//        scrollToBottomListener.scrollToBottom();
//    }
//
//    @Override
//    public int getItemCount() {
//        return methods.size();
//    }
//
//    public class PayHolder extends RecyclerView.ViewHolder {
//        private final   Button add;
//        public PayHolder(@NonNull View itemView) {
//            super(itemView);
//            add = itemView.findViewById(R.id.add);
//
//        }
//        private void bind(int position){
//            add.setHint(methods.get(position));
//            if (position == getItemCount() - 1 && position != POLL_LIMIT) {
//                add.setVisibility(View.VISIBLE);
//                add.setOnClickListener(v -> {
//                    Intent intent =new Intent(context , AddPaymantMethodActivity.class);
//                    context.startActivity(intent);
//                    addPollItem(position + 1);
//                });
//            } else {
//                add.setVisibility(View.INVISIBLE);
//                add.setOnClickListener(null);
//            }
//
//        }
//
//
//}
