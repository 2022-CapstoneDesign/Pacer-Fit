package com.example.project.Dist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.project.R;
import com.google.android.material.tabs.TabLayout;

public class DetailRecordFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dist_detail_record_fragment, container, false);

        tabLayout = v.findViewById(R.id.detail_dist_tablayout);
        viewPager = v.findViewById(R.id.linechart_viewpager);
        tabLayout.setupWithViewPager(viewPager);

        DistVPAdapter distVPAdapter = new DistVPAdapter(getActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        distVPAdapter.addFragment(new OneWeekFragment(), "7일");
        distVPAdapter.addFragment(new OneMonthFragment(), "30일");
        distVPAdapter.addFragment(new SixMonthFragment(), "6개월");
        distVPAdapter.addFragment(new OneYearFragment(), "1년");
        viewPager.setAdapter(distVPAdapter);

        return v;
    }
}