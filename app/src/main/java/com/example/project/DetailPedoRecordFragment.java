package com.example.project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project.Pedo.OneMonthPedo;
import com.example.project.Pedo.OneWeekPedo;
import com.example.project.Pedo.OneYearPedo;
import com.example.project.Pedo.PedoVPAdapter;
import com.example.project.Pedo.SixMonthsPedo;
import com.example.project.Pedo.ThreeMonthsPedo;
import com.google.android.material.tabs.TabLayout;

public class DetailPedoRecordFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_detail_pedo_record, container, false);

        tabLayout = v.findViewById(R.id.detail_pedo_tablayout);
        viewPager = v.findViewById(R.id.barchart_viewpager);

        tabLayout.setupWithViewPager(viewPager);

        PedoVPAdapter pedoVPAdapter = new PedoVPAdapter(getActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pedoVPAdapter.addFragment(new OneWeekPedo(), "7일");
        pedoVPAdapter.addFragment(new OneMonthPedo(), "30일");
        pedoVPAdapter.addFragment(new ThreeMonthsPedo(), "3개월");
        pedoVPAdapter.addFragment(new SixMonthsPedo(), "6개월");
        pedoVPAdapter.addFragment(new OneYearPedo(), "1년");
        viewPager.setAdapter(pedoVPAdapter);

        return v;
    }
}