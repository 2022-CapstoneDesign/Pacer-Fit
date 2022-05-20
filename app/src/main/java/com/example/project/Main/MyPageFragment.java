package com.example.project.Main;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.project.R;
import com.ramotion.foldingcell.FoldingCell;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPageFragment extends Fragment {

    String bestCalorie_Steps;
    String bestCalorie_Km;
    String bestTime_Steps;
    String bestTime_Km;
    String bestTime;
    String userID;
    String userName;
    String bestCalorie;
    String bestSteps;
    String bestKm;
    int userProfileNum;
    int hour, minutes;

    FoldingCell foldingCell;
    Float userHeight;
    Float userWeight;

    Dialog dialog;

    int[] ProfileDrawable = {
            R.drawable.profile_default, R.drawable.profile_man, R.drawable.profile_man_beard, R.drawable.profile_man_cap,
            R.drawable.profile_man_hat, R.drawable.profile_man_hood, R.drawable.profile_man_horn, R.drawable.profile_man_round,
            R.drawable.profile_man_suit, R.drawable.profile_man_sunglass, R.drawable.profile_woman_glasses, R.drawable.profile_woman_neck,
            R.drawable.profile_woman_old, R.drawable.profile_woman_scarf
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_mypage_fragment, container, false);

        TextView myID = v.findViewById(R.id.mypageId);

        TextView maxStep = v.findViewById(R.id.maxStep);
        TextView maxKm = v.findViewById(R.id.maxKm);
        TextView maxKcal = v.findViewById(R.id.maxKcal);
        TextView maxTime = v.findViewById(R.id.maxTime);
        CircleImageView profileImg = v.findViewById(R.id.profileImg);

        TextView heightTxt = v.findViewById(R.id.heightTxt);
        TextView weightTxt = v.findViewById(R.id.weightTxt);
        TextView bmiTxt = v.findViewById(R.id.bmiTxt);
        TextView bmiExplain = v.findViewById(R.id.bmi_explain);

        Intent intent = getActivity().getIntent();
        userID = intent.getStringExtra("userID");
        userName = intent.getStringExtra("userName");
        userProfileNum = intent.getIntExtra("userProfileNum",0);
        userHeight = Float.valueOf(intent.getStringExtra("userHeight"));
        userWeight = Float.valueOf(intent.getStringExtra("userWeight"));

        profileImg.setImageResource(ProfileDrawable[userProfileNum]);

        // <------ 팝업 다이얼로그 ------>
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_my_page_popup_img);

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), MyPagePopupImgActivity.class); //Fragment -> Activity로 이동 (만보기팝업)
//                startActivity(intent);
                showProfileDialog();
            }
        });
        // <--------------------------->

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("========================" + response);
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) { // 만보기 클릭시
                        bestSteps = jsonObject.getString("bestSteps");
                        bestKm = jsonObject.getString("bestKm");
                        bestTime_Km = jsonObject.getString("bestTime(km)");
                        bestCalorie_Km = jsonObject.getString("bestCalorie(km)");
                        bestTime_Steps = jsonObject.getString("bestTime(steps)");
                        bestCalorie_Steps = jsonObject.getString("bestCalorie(steps)");

                        myID.setText(userName); // user이름 설정해주기
                        
                        //칼로리
                        if (Float.parseFloat(bestCalorie_Km) > Float.parseFloat(bestCalorie_Steps))
                            bestCalorie = bestCalorie_Km;
                        else
                            bestCalorie = bestCalorie_Steps;

                        //시간
                        if (Integer.parseInt(bestTime_Km) > Integer.parseInt(bestTime_Steps))
                            bestTime = bestTime_Km;
                        else
                            bestTime = bestTime_Steps;
                        minutes = Integer.parseInt(bestTime) / 60;
                        hour = minutes / 60;
                        minutes %= 60;

                        //만보기 기록
                        if(bestSteps.equals("0")){
                            maxStep.setText("");
                            TextView maxtxt = v.findViewById(R.id.maxStepTxt);
                            maxtxt.setText("");
                            TextView steptxt = v.findViewById(R.id.stepTxt);
                            steptxt.setTextColor(Color.GRAY);
                            ImageView stepBtn = v.findViewById(R.id.mypage_walkBtn);
                            stepBtn.setImageResource(R.drawable.walk);
                        }
                        else
                            maxStep.setText(bestSteps); // DB에서 불러온 값으로 바꾸기
                        //거리 기록
                        if(bestKm.equals("0")){
                            //숫자 데이터
                            maxKm.setText("");
                            //"최대거리" 텍스트
                            TextView maxtxt = v.findViewById(R.id.maxKmTxt);
                            maxtxt.setText("");
                            //"거리" 텍스트
                            TextView kmtxt = v.findViewById(R.id.kmTxt);
                            kmtxt.setTextColor(Color.GRAY);
                            //Image
                            ImageView kmBtn = v.findViewById(R.id.mypage_kmBtn);
                            kmBtn.setImageResource(R.drawable.km);
                        }
                        else
                            maxKm.setText(bestKm + "km"); // DB에서 불러온 값으로 바꾸기
                        //칼로리 기록
                        if(bestCalorie.equals("0")){
                            maxKcal.setText("");
                            TextView maxtxt = v.findViewById(R.id.maxKcalTxt);
                            maxtxt.setText("");
                            TextView caltxt = v.findViewById(R.id.kcalTxt);
                            caltxt.setTextColor(Color.GRAY);
                            ImageView calBtn = v.findViewById(R.id.mypage_kcalBtn);
                            calBtn.setImageResource(R.drawable.kcal);
                        }
                        else
                            maxKcal.setText(bestCalorie + "kcal"); // DB에서 불러온 값으로 바꾸기
                        //시간 기록
                        if(Integer.parseInt(bestTime)<60){
                            maxTime.setText("");
                            TextView maxtxt = v.findViewById(R.id.maxTimeTxt);
                            maxtxt.setText("");
                            TextView timetxt = v.findViewById(R.id.timeTxt);
                            timetxt.setTextColor(Color.GRAY);
                            ImageView timeBtn = v.findViewById(R.id.mypage_timeBtn);
                            timeBtn.setImageResource(R.drawable.time);
                        }
                        else {
                            if(hour!=0)
                                maxTime.setText(hour + "시간 " + minutes + "분"); // DB에서 불러온 값으로 바꾸기
                            else
                                maxTime.setText(minutes + "분");
                        }
                    } else { // 로그인에 실패한 경우
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        MyPageBestRecordRequest myPageBestRecordRequest = new MyPageBestRecordRequest(userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(myPageBestRecordRequest);

        // <-------- 폴딩셀 -------->
        foldingCell = (FoldingCell) v.findViewById(R.id.folding_cell);
        foldingCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foldingCell.toggle(false);
            }
        });

        heightTxt.setText(userHeight + "cm");
        weightTxt.setText(userWeight + "kg");
        double bmi = userWeight / ((userHeight*0.01)*(userHeight*0.01));
        bmi = Math.round(bmi*100)/100.0; // 소수점 아래 둘째자리까지 반올림
        bmiTxt.setText("BMI : " + bmi);

        float markerVal;
        Guideline guideline_bmi = v.findViewById(R.id.guideline_bmi);
        if (bmi < 20) { // 0 ~ 20
            markerVal = (float) (0.0125 * bmi);
            bmiExplain.setText("저체중");
            bmiExplain.setTextColor(ContextCompat.getColor(getContext(), R.color.bmi_blue));
        }
        else if (bmi >= 20 && bmi < 24) {
            markerVal = (float) (0.0625 * bmi - 1);
            bmiExplain.setText("정상 체중");
            bmiExplain.setTextColor(ContextCompat.getColor(getContext(), R.color.bmi_green));
        }
        else if (bmi >= 24 && bmi < 30) {
            markerVal = (float) (0.0417 * bmi - 0.5);
            bmiExplain.setText("과체중");
            bmiExplain.setTextColor(ContextCompat.getColor(getContext(), R.color.bmi_yellow));
        }
        else { // 30 ~ 100
            markerVal = (float) (0.0036 * bmi + 0.64);
            bmiExplain.setText("비만");
            bmiExplain.setTextColor(ContextCompat.getColor(getContext(), R.color.bmi_red));
        }
        markerVal = (float) (Math.round(markerVal*100)/100.0); // 소수점 아래 둘째자리까지 반올림
        guideline_bmi.setGuidelinePercent(markerVal);

        ImageView bmi_marker = v.findViewById(R.id.bmi_marker);
        bmi_marker.bringToFront();
        // <----------------------->


        return v;
    }

    private void showProfileDialog() {
        dialog.show();

        Button okBtn = dialog.findViewById(R.id.popupProfileOK);
        Button cancelBtn = dialog.findViewById(R.id.popupProfileCancel);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

}