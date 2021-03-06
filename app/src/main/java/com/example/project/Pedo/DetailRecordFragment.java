package com.example.project.Pedo;

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

    PedoRecordData data = new PedoRecordData();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pedo_detail_record_fragment, container, false);

        tabLayout = v.findViewById(R.id.detail_pedo_tablayout);
        viewPager = v.findViewById(R.id.barchart_viewpager);
        tabLayout.setupWithViewPager(viewPager);

        if (getArguments() != null)
        {
            for(int i=0; i<7; i++) {
                //7일 데이터
                data.setPedoRecord7(i, getArguments().getString(i+".day_step"), getArguments().getString(i+".day_time"));
            }
            for(int i=0; i<31; i++){
                //30일 데이터
                data.setPedoRecord30(i,getArguments().getString(i+".month_step"),getArguments().getString(i+".month_time"));
            }
            for(int i=0; i<24; i++){
                data.setPedoRecord180(i,getArguments().getString(i+".week_step"),getArguments().getString(i+".week_time"));
            }
            for(int i=0; i<12; i++){
                data.setPedoRecordYear(i,getArguments().getString(i+".year_step"),getArguments().getString(i+".year_time"));
            }
            data.pedo_max_day = getArguments().getString("pedo_max_day");
            data.pedo_max_month = getArguments().getString("pedo_max_month");
            data.pedo_max_180 = getArguments().getString("pedo_max_180");
            data.pedo_max_year = getArguments().getString("pedo_max_year");
        }
        PedoVPAdapter pedoVPAdapter = new PedoVPAdapter(getActivity().getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pedoVPAdapter.addFragment(new OneWeekFragment(), "7일");
        pedoVPAdapter.addFragment(new OneMonthFragment(), "30일");
        pedoVPAdapter.addFragment(new SixMonthFragment(), "6개월");
        pedoVPAdapter.addFragment(new OneYearFragment(), "1년");
        viewPager.setAdapter(pedoVPAdapter);

        return v;
    }
}