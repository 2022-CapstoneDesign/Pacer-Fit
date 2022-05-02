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
    String[] PedoRecord7_step = new String[7];
    String[] PedoRecord7_time = new String[7];
    String[] PedoRecord30_step = new String[31];
    String[] PedoRecord30_time = new String[31];
    String[] PedoRecord30 = new String[31];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pedo_detail_record_fragment, container, false);

        tabLayout = v.findViewById(R.id.detail_pedo_tablayout);
        viewPager = v.findViewById(R.id.barchart_viewpager);
        tabLayout.setupWithViewPager(viewPager);

        //나중에 코드 정리하기
        if (getArguments() != null)
        {
            for(int i=0; i<7; i++) {
                PedoRecord7_step[i] = getArguments().getString(i+".day_step"); // HomeFragment에서 받아온 값 넣기
                PedoRecord7_time[i] = getArguments().getString(i+".day_time"); // HomeFragment에서 받아온 값 넣기
//                System.out.println("getArguments Test =====================Step:" + PedoRecord7[i]);
                //7일 데이터
                data.setPedoRecord7(i, PedoRecord7_step[i], PedoRecord7_time[i]);
               //                System.out.println("getArguments Test =====================Time:" + PedoRecord7[i]);
            }
            for(int i=0; i<31; i++){
                //30일 데이터
                data.setPedoRecord30(i,getArguments().getString(i+".month_step"),getArguments().getString(i+".month_time"));
            }
            data.pedo_max_day = getArguments().getString("pedo_max_day");
            data.pedo_max_month = getArguments().getString("pedo_max_month");
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