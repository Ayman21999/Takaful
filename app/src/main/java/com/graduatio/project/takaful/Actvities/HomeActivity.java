package com.graduatio.project.takaful.Actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.graduatio.project.takaful.Fragments.DonationInforamtiomFragment;
import com.graduatio.project.takaful.Fragments.CategoryFragment;
import com.graduatio.project.takaful.Fragments.ProfileFragment;
import com.graduatio.project.takaful.Fragments.SearchFragment;
import com.graduatio.project.takaful.R;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationViewl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationViewl = findViewById(R.id.bottomnav);
        bottomNavigationViewl.setOnNavigationItemSelectedListener(bottomNavMethoed);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new CategoryFragment()).commit();



    }
    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethoed = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null ;
            switch (item.getItemId()){
                case R.id.home:
                    fragment= new CategoryFragment();
                    break;
                case R.id.search :
                    fragment = new SearchFragment();
                    break;
                case R.id.donationData :
                    fragment = new DonationInforamtiomFragment();
                    break;
                case R.id.profile:
                    fragment = new ProfileFragment();
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();

            return true;
        }
    };

}