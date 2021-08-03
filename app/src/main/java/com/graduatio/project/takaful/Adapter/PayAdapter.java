//package com.graduatio.project.takaful.Adapter;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.EventListener;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestoreException;
//import com.google.firebase.firestore.QuerySnapshot;
//import com.graduatio.project.takaful.Actvities.AddPaymantMethodActivity;
//import com.graduatio.project.takaful.Model.Donations;
//import com.graduatio.project.takaful.Model.PayMethod;
//import com.graduatio.project.takaful.R;
//import com.squareup.picasso.Picasso;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.graduatio.project.takaful.Model.PayMethod.LayoutOne;
//import static com.graduatio.project.takaful.Model.PayMethod.LayoutTwo;
//
//public class PayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    List<PayMethod> methods;
//    Context context ;
//    private final static int POLL_LIMIT = 4;
//    CollectionReference   reference  = FirebaseFirestore.getInstance().collection("Users");
//    public PayAdapter(List<PayMethod> methods , Context context ) {
//        this.methods = methods;
//        this.context = context;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        switch (methods.get(position).getViewType()) {
//            case 0:
//                return LayoutOne;
//            case 1:
//                return LayoutTwo;
//            default:
//                return -1;
//        }
//    }
//
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        switch (viewType) {
//            case LayoutOne:
//                return new AddPayMethodAdapter(LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.pay_method_item, parent, false));
//            case LayoutTwo:
//                return new ShowMethodAdded(LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.pay_method_info_item, parent, false));
//            default:
//                return null;
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//
//        switch (holder.getItemViewType()) {
//            case LayoutOne:
//                ((ShowMethodAdded) holder).bindItem(methods.get(position));
//             break;
//            case  LayoutTwo:
//                ((AddPayMethodAdapter) holder).bindItem(methods.get(position));
//
//        }
////        switch (methods.get(position).getViewType()) {
////            case LayoutOne:
////                String text
////                        = itemClassList.get(position).getText();
////                ((LayoutOneViewHolder) holder).setView(text);
////
////                // The following code pops a toast message
////                // when the item layout is clicked.
////                // This message indicates the corresponding
////                // layout.
////                ((LayoutOneViewHolder) holder)
////                        .linearLayout.setOnClickListener(
////                        new View.OnClickListener() {
////                            @Override
////                            public void onClick(View view) {
////
////                                Toast
////                                        .makeText(
////                                                view.getContext(),
////                                                "Hello from Layout One!",
////                                                Toast.LENGTH_SHORT)
////                                        .show();
////                            }
////                        });
////
////                break;
////
////            case LayoutTwo:
////                int image
////                        = methods.get(position).getImg();
////                String text_one
////                        = methods.get(position).getCardNumber();
////                String text_two
////                        = methods.get(position).getPass();
////                ((LayoutTwoViewHolder) holder)
////                        .setViews(image, text_one, text_two);
////                ((LayoutTwoViewHolder) holder)
////                        .linearLayout.setOnClickListener(
////                        new View.OnClickListener() {
////                            @Override
////                            public void onClick(View view) {
////                                Toast.makeText(view.getContext(),
////                                        "Hello from Layout Two!",
////                                        Toast.LENGTH_SHORT)
////                                        .show();
////                            }
////                        });
////                break;
////            default:
////                return;
////        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return methods.size();
//    }
//
//
//    static class AddPayMethodAdapter extends RecyclerView.ViewHolder {
//
//        Button add;
//    private ScrollToBottomListener scrollToBottomListener;
//        Context context;
//        private final static int POLL_LIMIT = 4;
//        private ArrayList<String> methods;
//        private String method;
//         ScrollToBottomListener scrollToBottomListener;
//
//        public AddPayMethodAdapter(@NonNull View itemView) {
//            super(itemView);
//
//            // Find the Views
//            add = itemView.findViewById(R.id.add);
//        }
//
//        public interface ScrollToBottomListener {
//                    void scrollToBottom();
//    }
//        public void bindItem(PayMethod payMethod) {
//            add.setHint(pollOptions.get(position));
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
//            add.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context , AddPaymantMethodActivity.class);
//                    context.startActivity(intent);
//                }
//            });
//
//        }
//    }
//
//    class ShowMethodAdded extends RecyclerView.ViewHolder {
//
//        private ImageView icon;
//        private TextView email, password;
//        private ConstraintLayout linearLayout;
//
//        public ShowMethodAdded(@NonNull View itemView) {
//            super(itemView);
//            icon = itemView.findViewById(R.id.image);
//            email = itemView.findViewById(R.id.email);
//            password = itemView.findViewById(R.id.password);
//        }
//        public  void bindItem(PayMethod payMethod){
//            String type = "";
//            if (type.equals("")) {
//
//                email.setText(payMethod.getCardNumber());
//                password.setText(payMethod.getCvvCode());
//                icon.setImageResource(R.drawable.ic_visa);
//
//            }else if (type.equals("")){
//                email.setText(payMethod.getEmail());
//                password.setText(payMethod.getPass());
//                icon.setImageResource(R.drawable.ic_paypal);
//
//            }else if (type.equals("")){
//                email.setText(payMethod.getEmail());
//                password.setText(payMethod.getPass());
//                icon.setImageResource(R.drawable.ic_googlewallet);
//
//            }
//        }
//    }
//
//
//}
