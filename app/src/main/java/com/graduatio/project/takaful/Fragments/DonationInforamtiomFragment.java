package com.graduatio.project.takaful.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.graduatio.project.takaful.Adapter.AdapterPager;
import com.graduatio.project.takaful.R;

import java.util.Arrays;
import java.util.List;

public class DonationInforamtiomFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View   view = inflater.inflate(R.layout.fragment_donation_inforamtiom, container, false);
        TabLayout tabLayout =view.findViewById(R.id.tabs);
        ViewPager viewPager = view.findViewById(R.id.viewpager);
        List<Fragment> fragments = Arrays.asList(new ActivityFragment() , new DonationFragment());
        List<String> fragmentTitles = Arrays.asList("Activity","Donation");
        AdapterPager tabAdapter = new AdapterPager
                (getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
                        ,getContext(),fragments,fragmentTitles);
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        return view ;

    }
}