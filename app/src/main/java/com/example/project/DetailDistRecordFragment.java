package com.example.project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.project.Dist.DistVPAdapter;
import com.example.project.Dist.OneMonthDist;
import com.example.project.Dist.OneWeekDist;
import com.example.project.Dist.OneYearDist;
import com.example.project.Dist.SixMonthDist;
import com.example.project.Dist.ThreeMonthDist;
import com.google.android.material.tabs.TabLayout;

public class DetailDistRecordFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_detail_dist_record, container, false);

        tabLayout = v.findViewById(R.id.detail_dist_tablayout);
        viewPager = v.findViewById(R.id.linechart_viewpager);

        tabLayout.setupWithViewPager(viewPager);

        DistVPAdapter distVPAdapter = new DistVPAdapter(getActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        distVPAdapter.addFragment(new OneWeekDist(), "7일");
        distVPAdapter.addFragment(new OneMonthDist(), "30일");
        distVPAdapter.addFragment(new ThreeMonthDist(), "3개월");
        distVPAdapter.addFragment(new SixMonthDist(), "6개월");
        distVPAdapter.addFragment(new OneYearDist(), "1년");
        viewPager.setAdapter(distVPAdapter);

        return v;
    }
}