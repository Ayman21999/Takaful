package com.graduatio.project.takaful.Actvities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.graduatio.project.takaful.Fragments.CategoryFragment;
import com.graduatio.project.takaful.R;

public class SecondAppBegging extends AppCompatActivity {
    Button skip;
    Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_app_begging);
        skip  = findViewById(R.id.skip);
        next  = findViewById(R.id.signup);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondAppBegging.this , HomeActivity.class);
                startActivity(intent);

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondAppBegging.this , LastAppBegging.class);
                startActivity(intent);
            }
        });

    }
}