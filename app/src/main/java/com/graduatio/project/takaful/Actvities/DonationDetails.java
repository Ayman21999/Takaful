package com.graduatio.project.takaful.Actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.graduatio.project.takaful.Model.Advertising;
import com.graduatio.project.takaful.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class DonationDetails extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    CollectionReference reference;
    ImageView ad_image;
    TextView targetnumber,desc,userPublisher,daynum,rimeing,title;
    Button donate;
    Advertising advertising;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_details);
        SetUpElement();
        getAdData();

    }

    public void SetUpElement(){
        firebaseFirestore =FirebaseFirestore.getInstance();
        reference = firebaseFirestore.collection("Advertising");
        ad_image = findViewById(R.id.add_image);
        targetnumber= findViewById(R.id.target_number);
        desc = findViewById(R.id.description);
        userPublisher = findViewById(R.id.user_published);
        daynum = findViewById(R.id.dayleft);
        rimeing = findViewById(R.id.remining_number);
        donate = findViewById(R.id.donate_bttn);
    }


    private void getAdData() {


        if(getIntent()== null || !getIntent().hasExtra("id"))
            return;

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference adver_Ref;

//        if(getIntent().hasExtra("isForUser") && getIntent().getBooleanExtra("isForUser",false)){
//
//            postRef = FirebaseFirestore.getInstance().collection("Users")
//                    .document(getIntent().getStringExtra("publisherId"))
//                    .collection("UserPosts")
//                    .document(getIntent().getStringExtra("postId"));

        adver_Ref = FirebaseFirestore.getInstance().collection("Advertising")
                    .document(getIntent().getStringExtra("userId"));



        adver_Ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    advertising = documentSnapshot.toObject(Advertising.class);
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful() && advertising!=null){

                    getUserInfo();


                }

            }
        });




    }
    private void getUserInfo() {
        FirebaseFirestore.getInstance().collection("Users")
                .document(advertising.getUserId()).get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                userPublisher.setText(snapshot.getString("firstName"));
            }
        });
    }

}