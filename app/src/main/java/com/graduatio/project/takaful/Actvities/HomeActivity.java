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

public class HomeActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView nav_btom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


    }

    public void SetUPElement() {
        nav_btom = findViewById(R.id.bottomnav);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        if (item.getItemId() == R.id.home) {
            replaceFragment(new CategoryFragment());

        } else if (item.getItemId() == R.id.profile) {
            replaceFragment(new ProfileFragment());

        } else if (item.getItemId() == R.id.search) {
            replaceFragment(new SearchFragment());
        } else if (item.getItemId() == R.id.donationData) {
            replaceFragment(new DonationInforamtiomFragment());
        }
        return true;
    }

    public void replaceFragment(Fragment fragment) {

        getSupportFragmentManager().beginTransaction().replace(R.id.homeFrameLayout, fragment).commit();

    }
}