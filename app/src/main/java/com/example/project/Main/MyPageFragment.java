package com.example.project.Main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.R;

public class MyPageFragment extends Fragment {

    private ImageView walkBtn;
    private ImageView kmBtn;
    private ImageView kcalBtn;
    private ImageView timeBtn;

    private TextView maxStepTxt;
    private TextView maxStep;
    private TextView stepTxt;
    private TextView myID;
    private TextView maxKmTxt;
    private TextView maxKm;
    private TextView kmTxt;
    private TextView maxKcalTxt;
    private TextView maxKcal;
    private TextView kcalTxt;
    private TextView maxTimeTxt;
    private TextView maxTime;
    private TextView timeTxt;

    private boolean onWalkBtn = false;
    private boolean onKmBtn = false;
    private boolean onKcalBtn = false;
    private boolean onTimeBtn = false;

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

        myID = v.findViewById(R.id.mypageId);
        myID.setText(userName); // user이름 설정해주기

        walkBtn = v.findViewById(R.id.mypage_walkBtn);
        kmBtn = v.findViewById(R.id.mypage_kmBtn);
        kcalBtn = v.findViewById(R.id.mypage_kcalBtn);
        timeBtn = v.findViewById(R.id.mypage_timeBtn);
        maxStepTxt = v.findViewById(R.id.maxStepTxt);
        maxStep = v.findViewById(R.id.maxStep);
        stepTxt = v.findViewById(R.id.stepTxt);
        maxKmTxt = v.findViewById(R.id.maxKmTxt);
        maxKm = v.findViewById(R.id.maxKm);
        kmTxt = v.findViewById(R.id.kmTxt);
        maxKcalTxt = v.findViewById(R.id.maxKcalTxt);
        maxKcal = v.findViewById(R.id.maxKcal);
        kcalTxt = v.findViewById(R.id.kcalTxt);
        maxTimeTxt = v.findViewById(R.id.maxTimeTxt);
        maxTime = v.findViewById(R.id.maxTime);
        timeTxt = v.findViewById(R.id.timeTxt);

        turnOnWalk();

        walkBtn.setOnClickListener(v1 -> {
            if (!onWalkBtn) {
                turnOnWalk();
                turnOffKm();
                turnOffKcal();
                turnOffTime();
            }
        });

        kmBtn.setOnClickListener(v12 -> {
            if (!onKmBtn) {
                turnOnKm();
                turnOffWalk();
                turnOffKcal();
                turnOffTime();
            }
        });

        kcalBtn.setOnClickListener(v13 -> {
            if (!onKcalBtn) {
                turnOnKcal();
                turnOffWalk();
                turnOffKm();
                turnOffTime();
            }
        });

        timeBtn.setOnClickListener(v14 -> {
            if (!onTimeBtn) {
                turnOnTime();
                turnOffWalk();
                turnOffKm();
                turnOffKcal();
            }
        });

        return v;
    }

    private void turnOnWalk() {
        walkBtn.setImageResource(R.drawable.walk_over);
        maxStep.setText(bestSteps); // DB에서 불러온 값으로 바꾸기
        maxStep.setTextSize(23);
        stepTxt.setTextColor(Color.parseColor("#FFFFFFFF"));
        maxStepTxt.setText("최대 걸음");
        onWalkBtn = true;
    }

    private void turnOffWalk() {
        walkBtn.setImageResource(R.drawable.walk);
        maxStep.setText("");
        maxStep.setTextSize(3);
        stepTxt.setTextColor(Color.parseColor("#909090"));
        maxStepTxt.setText("");
        onWalkBtn = false;
    }

    private void turnOnKm() {
        kmBtn.setImageResource(R.drawable.km_over);
        maxKm.setText(bestKm + "km"); // DB에서 불러온 값으로 바꾸기
        maxKm.setTextSize(23);
        kmTxt.setTextColor(Color.parseColor("#FFFFFFFF"));
        maxKmTxt.setText("최대 거리");
        onKmBtn = true;
    }

    private void turnOffKm() {
        kmBtn.setImageResource(R.drawable.km);
        maxKm.setText("");
        maxKm.setTextSize(3);
        kmTxt.setTextColor(Color.parseColor("#909090"));
        maxKmTxt.setText("");
        onKmBtn = false;
    }

    private void turnOnKcal() {
        if (Integer.parseInt(bestCalorie_Km) > Integer.parseInt(bestCalorie_Steps))
            bestCalorie = bestCalorie_Km;
        else
            bestCalorie = bestCalorie_Steps;

        kcalBtn.setImageResource(R.drawable.kcal_over);
        maxKcal.setText(bestCalorie + "kcal"); // DB에서 불러온 값으로 바꾸기
        maxKcal.setTextSize(23);
        kcalTxt.setTextColor(Color.parseColor("#FFFFFFFF"));
        maxKcalTxt.setText("최대 칼로리");
        onKcalBtn = true;
    }

    private void turnOffKcal() {
        kcalBtn.setImageResource(R.drawable.kcal);
        maxKcal.setText("");
        maxKcal.setTextSize(3);
        kcalTxt.setTextColor(Color.parseColor("#909090"));
        maxKcalTxt.setText("");
        onKcalBtn = false;
    }

    private void turnOnTime() {
        if (Integer.parseInt(bestTime_Km) > Integer.parseInt(bestTime_Steps))
            bestTime = bestTime_Km;
        else
            bestTime = bestTime_Steps;
        minutes = Integer.parseInt(bestTime) / 60;
        hour = minutes / 60;
        minutes %= 60;
        timeBtn.setImageResource(R.drawable.time_over);
        maxTime.setText(hour + "시간 " + minutes + "분"); // DB에서 불러온 값으로 바꾸기
        maxTime.setTextSize(23);
        timeTxt.setTextColor(Color.parseColor("#FFFFFFFF"));
        maxTimeTxt.setText("최대 시간");
        onTimeBtn = true;
    }

    private void turnOffTime() {
        timeBtn.setImageResource(R.drawable.time);
        maxTime.setText("");
        maxTime.setTextSize(3);
        timeTxt.setTextColor(Color.parseColor("#909090"));
        maxTimeTxt.setText("");
        onTimeBtn = false;
    }
}