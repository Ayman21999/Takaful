package com.graduatio.project.takaful.Actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.graduatio.project.takaful.Fragments.DonationInforamtiomFragment;
import com.graduatio.project.takaful.Fragments.CategoryFragment;
import com.graduatio.project.takaful.Fragments.ProfileFragment;
import com.graduatio.project.takaful.Fragments.SearchFragment;
import com.graduatio.project.takaful.R;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity  {

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
                case R.id.category:
                    fragment= new CategoryFragment();
                    break;
                case R.id.search :
                    fragment = new SearchFragment();
                    break;
                case R.id.donationData :
                    fragment = new DonationInforamtiomFragment();
                    DonationInforamtiomFragment donationFragment = new DonationInforamtiomFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.container,donationFragment).commit();
                    break;
                case R.id.profile:
                    fragment = new ProfileFragment();
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();

            return true;
        }
    };

    public void replaceFragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

    }
    public void onBackPressed() {
            Log.d("ttt","last frag: "+getSupportFragmentManager().getFragments()
                    .get(getSupportFragmentManager().getFragments().size()-1));
            if (bottomNavigationViewl.getSelectedItemId() != R.id.category) {
                bottomNavigationViewl.setSelectedItemId(R.id.category);
                replaceFragment(new CategoryFragment());
            } else {
                super.onBackPressed();
            }
        }




}