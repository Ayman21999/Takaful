package com.graduatio.project.takaful.Actvities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.graduatio.project.takaful.R;

public class LastAppBegging extends AppCompatActivity {
    Button singe_txt;
    Button login_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_app_begging);
        setUpElements();
         singe_txt.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent  = new Intent(LastAppBegging.this , SignUp.class);
                 startActivity(intent);

             }
         });
        login_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(LastAppBegging.this , Login.class);
                startActivity(intent);

            }
        });

    }

 void setUpElements(){
        singe_txt = findViewById(R.id.signup);
        login_txt = findViewById(R.id.login);

 }
}