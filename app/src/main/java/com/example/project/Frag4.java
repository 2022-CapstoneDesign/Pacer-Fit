package com.example.project;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Frag4 extends Fragment {

    private ImageView walkBtn;
    private ImageView kmBtn;
    private ImageView kcalBtn;
    private ImageView timeBtn;

    private TextView maxStep;
    private TextView stepTxt;
    private TextView maxStepTxt;

    private TextView maxKm;
    private TextView kmTxt;
    private TextView maxKmTxt;

    private TextView maxKcal;
    private TextView kcalTxt;
    private TextView maxKcalTxt;

    private TextView maxTime;
    private TextView timeTxt;
    private TextView maxTimeTxt;

    private boolean onWalkBtn = false;
    private boolean onKmBtn = false;
    private boolean onKcalBtn = false;
    private boolean onTimeBtn = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.frag4,container,false);

        walkBtn = v.findViewById(R.id.mypage_walkBtn);
        kmBtn = v.findViewById(R.id.mypage_kmBtn);
        kcalBtn = v.findViewById(R.id.mypage_kcalBtn);
        timeBtn = v.findViewById(R.id.mypage_timeBtn);

        maxStep = v.findViewById(R.id.maxStep);
        stepTxt = v.findViewById(R.id.stepTxt);
        maxStepTxt = v.findViewById(R.id.maxStepTxt);

        maxKm = v.findViewById(R.id.maxKm);
        kmTxt = v.findViewById(R.id.kmTxt);
        maxKmTxt = v.findViewById(R.id.maxKmTxt);

        maxKcal = v.findViewById(R.id.maxKcal);
        kcalTxt = v.findViewById(R.id.kcalTxt);
        maxKcalTxt = v.findViewById(R.id.maxKcalTxt);

        maxTime = v.findViewById(R.id.maxTime);
        timeTxt = v.findViewById(R.id.timeTxt);
        maxTimeTxt = v.findViewById(R.id.maxTimeTxt);


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
        maxStep.setText("7,832"); // DB에서 불러온 값으로 바꾸기
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
        maxKm.setText("2.37" + "km"); // DB에서 불러온 값으로 바꾸기
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
        kcalBtn.setImageResource(R.drawable.kcal_over);
        maxKcal.setText("328" + "kcal"); // DB에서 불러온 값으로 바꾸기
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
        timeBtn.setImageResource(R.drawable.time_over);
        maxTime.setText("1" + "시간 " + "13" + "분"); // DB에서 불러온 값으로 바꾸기
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