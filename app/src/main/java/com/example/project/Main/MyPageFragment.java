package com.example.project.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.R;

public class MyPageFragment extends Fragment {

    String bestCalorie_Steps;
    String bestCalorie_Km;
    String bestTime_Steps;
    String bestTime_Km;
    String bestTime;
    String userName;
    String bestCalorie;
    String bestSteps;
    String bestKm;
    int hour, minutes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_mypage_fragment, container, false);

        TextView myID = v.findViewById(R.id.mypageId);

        TextView maxStep = v.findViewById(R.id.maxStep);
        TextView maxKm = v.findViewById(R.id.maxKm);
        TextView maxKcal = v.findViewById(R.id.maxKcal);
        TextView maxTime = v.findViewById(R.id.maxTime);

        Intent intent = getActivity().getIntent();
        userName = intent.getStringExtra("userName");
        bestSteps = intent.getStringExtra("bestSteps");
        bestKm = intent.getStringExtra("bestKm");
        bestTime_Km = intent.getStringExtra("bestTime_Km");
        bestCalorie_Km = intent.getStringExtra("bestCalorie_Km");
        bestTime_Steps = intent.getStringExtra("bestTime_Steps");
        bestCalorie_Steps = intent.getStringExtra("bestCalorie_Steps");

        System.out.println("1===========" + bestSteps);
        System.out.println("1===========" + bestKm);
        System.out.println("2===========" + bestTime_Km);
        System.out.println("3===========" + bestCalorie_Km);
        System.out.println("4===========" + bestTime_Steps);
        System.out.println("5===========" + bestCalorie_Steps);


        myID.setText(userName); // user이름 설정해주기

        if (Integer.parseInt(bestCalorie_Km) > Integer.parseInt(bestCalorie_Steps))
            bestCalorie = bestCalorie_Km;
        else
            bestCalorie = bestCalorie_Steps;

        if (Integer.parseInt(bestTime_Km) > Integer.parseInt(bestTime_Steps))
            bestTime = bestTime_Km;
        else
            bestTime = bestTime_Steps;
        minutes = Integer.parseInt(bestTime) / 60;
        hour = minutes / 60;
        minutes %= 60;

        maxStep.setText(bestSteps); // DB에서 불러온 값으로 바꾸기
        maxKm.setText(bestKm + "km"); // DB에서 불러온 값으로 바꾸기
        maxKcal.setText(bestCalorie + "kcal"); // DB에서 불러온 값으로 바꾸기
        maxTime.setText(hour + "시간 " + minutes + "분"); // DB에서 불러온 값으로 바꾸기

        return v;
    }

}