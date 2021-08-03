package com.graduatio.project.takaful.Actvities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.graduatio.project.takaful.Fragments.ProfileFragment;
import com.graduatio.project.takaful.R;


import java.util.ArrayList;
import java.util.List;

public class EditPaymentMethod extends AppCompatActivity {


    RecyclerView list;
    Button save;
    ArrayList<String> methods;
//    PayAdapter adapter;

    Button visa,google,pall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        save = findViewById(R.id.add);
        visa = findViewById(R.id.vias);
        google = findViewById(R.id.google);
        pall = findViewById(R.id.paypal);
//        createPayItemsList();
//        list.setAdapter(adapter);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(EditPaymentMethod.this , ProfileFragment.class);
                startActivity(intent);
            }
        });
        visa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(EditPaymentMethod.this , PayInfoVisa.class);
                startActivity(intent);

            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(EditPaymentMethod.this , AddGoogleWActivity2.class);
                startActivity(intent);

            }
        });
        pall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(EditPaymentMethod.this ,AddPAyPalActivity.class);
                startActivity(intent);

            }
        });
    }

//    private void createPayItemsList() {
//
//        methods = new ArrayList<>();
//        final String option = "Add new method";
//        methods.add(option + "Add new Method");
//        adapter = new PayAdapter(methods, this, this);
//        list.setAdapter(adapter);
//
//    }

//    @Override
//    public void scrollToBottom() {
//        list.smoothScrollToPosition(methods.size() - 1);
//
//    }


}