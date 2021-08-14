package com.graduatio.project.takaful.Actvities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.graduatio.project.takaful.R;

public class MainActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences preferences  = getSharedPreferences("Takafull",MODE_PRIVATE);
        if (preferences.contains("is not first time")&& preferences.getBoolean("is not first time",false)) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null){
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
            }else {
                preferences.edit().putBoolean("is not first time",true).apply();
                Intent intent = new Intent(this, AppBegging.class);
                startActivity(intent);
                finish();

            }
        }
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent mainIntent = new Intent(MainActivity.this, AppBegging.class);
//                MainActivity.this.startActivity(mainIntent);
//                MainActivity.this.finish();
//            }
//        }, SPLASH_DISPLAY_LENGTH);
    }
}