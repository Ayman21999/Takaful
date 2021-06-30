package com.graduatio.project.takaful.Actvities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.graduatio.project.takaful.Fragments.CategoryFragment;
import com.graduatio.project.takaful.R;

public class AppBegging extends AppCompatActivity {

    Button skip;
    Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_begging);
        skip  = findViewById(R.id.skip);
        next  = findViewById(R.id.next);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppBegging.this , CategoryFragment.class);
                startActivity(intent);

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppBegging.this , SecondAppBegging.class);
                startActivity(intent);
            }
        });
    }
}