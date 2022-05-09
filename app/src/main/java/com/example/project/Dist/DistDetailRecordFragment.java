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

public class DistDetailRecordFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    DistRecordData data = new DistRecordData();
    String[] KmRecord7_km = new String[7];
    String[] KmRecord7_time = new String[7];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dist_detail_record_fragment, container, false);

        tabLayout = v.findViewById(R.id.detail_dist_tablayout);
        viewPager = v.findViewById(R.id.linechart_viewpager);
        tabLayout.setupWithViewPager(viewPager);

        //나중에 코드 정리하기
        if (getArguments() != null)
        {
            for(int i=0; i<7; i++) {
                KmRecord7_km[i] = getArguments().getString(i+".day_km"); // HomeFragment에서 받아온 값 넣기
                KmRecord7_time[i] = getArguments().getString(i+".day_time"); // HomeFragment에서 받아온 값 넣기
//                System.out.println("getArguments Test =====================Step:" + PedoRecord7[i]);
                //7일 데이터
                data.setKmRecord7(i, KmRecord7_km[i], KmRecord7_time[i]);
                //                System.out.println("getArguments Test =====================Time:" + PedoRecord7[i]);
            }
            for(int i=0; i<31; i++){
                //30일 데이터
                data.setKmRecord30(i,getArguments().getString(i+".month_km"),getArguments().getString(i+".month_time"));
            }
            for(int i=0; i<24; i++){
                data.setKmRecord180(i,getArguments().getString(i+".week_km"),getArguments().getString(i+".week_time"));
            }
            for(int i=0; i<12; i++){
                data.setKmRecordYear(i,getArguments().getString(i+".year_km"),getArguments().getString(i+".year_time"));
            }
            data.km_max_day = getArguments().getString("km_max_day");
            data.km_max_month = getArguments().getString("km_max_month");
            data.km_max_180 = getArguments().getString("km_max_180");
            data.km_max_year = getArguments().getString("km_max_year");
        }

        DistVPAdapter distVPAdapter = new DistVPAdapter(getActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        distVPAdapter.addFragment(new OneWeekFragment(), "7일");
        distVPAdapter.addFragment(new OneMonthFragment(), "30일");
        distVPAdapter.addFragment(new SixMonthFragment(), "6개월");
        distVPAdapter.addFragment(new OneYearFragment(), "1년");
        viewPager.setAdapter(distVPAdapter);

        return v;
    }
}