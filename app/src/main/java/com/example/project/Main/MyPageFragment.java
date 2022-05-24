package com.example.project.Main;

import static com.example.project.Map.LocationService.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Guideline;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.project.Dist.DistDetailRecordFragment;
import com.example.project.Login.IntroActivity;
import com.example.project.Map.RecordMapActivity;
import com.example.project.R;
import com.example.project.Ranking.UserInfo;
import com.ramotion.foldingcell.FoldingCell;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPageFragment extends Fragment{

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
    private String pathJsonString;
    private static final String TAG_JSON = "pacerfit";
    private static final String TAG_PROFILENUM = "userProfileNum";
    private static final String TAG_USERID = "userID";

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

    CircleImageView profileImg;

    MyCircleImageView selecting_profile;
    CircleImageView selected_back;
    CircleImageView selecting_back;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_mypage_fragment, container, false);

        TextView myID = v.findViewById(R.id.mypageId);

        TextView maxStep = v.findViewById(R.id.maxStep);
        TextView maxKm = v.findViewById(R.id.maxKm);
        TextView maxKcal = v.findViewById(R.id.maxKcal);
        TextView maxTime = v.findViewById(R.id.maxTime);
        profileImg = v.findViewById(R.id.profileImg);

        TextView heightTxt = v.findViewById(R.id.heightTxt);
        TextView weightTxt = v.findViewById(R.id.weightTxt);
        TextView bmiTxt = v.findViewById(R.id.bmiTxt);
        TextView bmiExplain = v.findViewById(R.id.bmi_explain);

        Intent intent = getActivity().getIntent();
        userID = intent.getStringExtra("userID");
        userName = intent.getStringExtra("userName");
        //userProfileNum = intent.getIntExtra("userProfileNum",0);
        userProfileNum = UserInfo.getInstance().getUserProfileNum();
        userHeight = Float.valueOf(intent.getStringExtra("userHeight"));
        userWeight = Float.valueOf(intent.getStringExtra("userWeight"));

        profileImg.setImageResource(ProfileDrawable[userProfileNum]);

        Button mypageEditBtn = v.findViewById(R.id.mypageEditBtn);
        mypageEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 프래그먼트 전환 시에 애니메이션을 적용하고 싶다면 다음과 같이 설정
                // 1. FragmentTransaction 선언 바로 다음에 setCustomAnimations 설정
                // 2. 각 메소드를 분리하여 작성(연달아서 X)
                FragmentManager fm = getActivity().getSupportFragmentManager();
                MyPageIdentifyFragment fragment = new MyPageIdentifyFragment();
                FragmentTransaction transaction = fm.beginTransaction();
                //transaction.setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out);
                transaction.replace(R.id.frame_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        // <------- 로그 아웃 처리 ------->
        Button logout = v.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), IntroActivity.class);
                startActivity(intent);
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedPreferences", Activity.MODE_PRIVATE);
                //Editor를 preferences에 쓰겠다고 연결
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                getActivity().finish();
            }
        });

        // <------ 팝업 다이얼로그 ------>
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_my_page_select_img);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); // 뒤에 하얀 배경 안 나오게

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


        return v;
    }

    private void showProfileDialog() {
        dialog.show();

        Button okBtn = dialog.findViewById(R.id.popupProfileOK);
        Button cancelBtn = dialog.findViewById(R.id.popupProfileCancel);

        // ****** 여기서 사용자가 선택한 프로필 사진으로 변경 처리 ******
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selecting_profile == null) {
                    Toast.makeText(getContext(), "선택된 이미지가 없습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    dialog.dismiss(); // 팝업창 닫힘
                    profileImg.setImageResource(ProfileDrawable[selecting_profile.getIndex()]);
                    UserInfo.getInstance().setUserProfileNum(selecting_profile.getIndex());
                    UpdateUserProfileNum task = new UpdateUserProfileNum();
                    task.execute("http://pacerfit.dothome.co.kr/updateUserProfile.php");
                    deleteSelectingProfileData();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // 팝업창 닫힘
                if (selecting_profile != null) {
                    deleteSelectingProfileData();
                }
            }
        });

        // <---------- 팝업 상의 프로필 이미지 아이콘들 ------------>
        MyCircleImageView profile0 = dialog.findViewById(R.id.profile0); profile0.setIndex(0);
        MyCircleImageView profile1 = dialog.findViewById(R.id.profile1); profile1.setIndex(1);
        MyCircleImageView profile2 = dialog.findViewById(R.id.profile2); profile2.setIndex(2);
        MyCircleImageView profile3 = dialog.findViewById(R.id.profile3); profile3.setIndex(3);
        MyCircleImageView profile4 = dialog.findViewById(R.id.profile4); profile4.setIndex(4);
        MyCircleImageView profile5 = dialog.findViewById(R.id.profile5); profile5.setIndex(5);
        MyCircleImageView profile6 = dialog.findViewById(R.id.profile6); profile6.setIndex(6);
        MyCircleImageView profile7 = dialog.findViewById(R.id.profile7); profile7.setIndex(7);
        MyCircleImageView profile8 = dialog.findViewById(R.id.profile8); profile8.setIndex(8);
        MyCircleImageView profile9 = dialog.findViewById(R.id.profile9); profile9.setIndex(9);
        MyCircleImageView profile10 = dialog.findViewById(R.id.profile10); profile10.setIndex(10);
        MyCircleImageView profile11 = dialog.findViewById(R.id.profile11); profile11.setIndex(11);
        MyCircleImageView profile12 = dialog.findViewById(R.id.profile12); profile12.setIndex(12);
        MyCircleImageView profile13 = dialog.findViewById(R.id.profile13); profile13.setIndex(13);

        // <---------- 팝업 상의 프로필 이미지 아이콘들 선택 시 노란색 테두리를 그려주기 위해 불러옴 ------------>
        CircleImageView profile0_back = dialog.findViewById(R.id.profile0_back);
        CircleImageView profile1_back = dialog.findViewById(R.id.profile1_back);
        CircleImageView profile2_back = dialog.findViewById(R.id.profile2_back);
        CircleImageView profile3_back = dialog.findViewById(R.id.profile3_back);
        CircleImageView profile4_back = dialog.findViewById(R.id.profile4_back);
        CircleImageView profile5_back = dialog.findViewById(R.id.profile5_back);
        CircleImageView profile6_back = dialog.findViewById(R.id.profile6_back);
        CircleImageView profile7_back = dialog.findViewById(R.id.profile7_back);
        CircleImageView profile8_back = dialog.findViewById(R.id.profile8_back);
        CircleImageView profile9_back = dialog.findViewById(R.id.profile9_back);
        CircleImageView profile10_back = dialog.findViewById(R.id.profile10_back);
        CircleImageView profile11_back = dialog.findViewById(R.id.profile11_back);
        CircleImageView profile12_back = dialog.findViewById(R.id.profile12_back);
        CircleImageView profile13_back = dialog.findViewById(R.id.profile13_back);


        profile0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecting_profile = profile0;
                setHighlight(profile0_back);
            }
        });
        profile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecting_profile = profile1;
                setHighlight(profile1_back);

            }
        });
        profile2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecting_profile = profile2;
                setHighlight(profile2_back);
            }
        });
        profile3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecting_profile = profile3;
                setHighlight(profile3_back);
            }
        });
        profile4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecting_profile = profile4;
                setHighlight(profile4_back);
            }
        });
        profile5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecting_profile = profile5;
                setHighlight(profile5_back);
            }
        });
        profile6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecting_profile = profile6;
                setHighlight(profile6_back);
            }
        });
        profile7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecting_profile = profile7;
                setHighlight(profile7_back);
            }
        });
        profile8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecting_profile = profile8;
                setHighlight(profile8_back);
            }
        });
        profile9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecting_profile = profile9;
                setHighlight(profile9_back);
            }
        });
        profile10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecting_profile = profile10;
                setHighlight(profile10_back);
            }
        });
        profile11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecting_profile = profile11;
                setHighlight(profile11_back);
            }
        });
        profile12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecting_profile = profile12;
                setHighlight(profile12_back);
            }
        });
        profile13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecting_profile = profile13;
                setHighlight(profile13_back);
            }
        });
    }

    private void setHighlight(CircleImageView profile_back) {
        if (selected_back != null) {
            selected_back.setBorderWidth(0);
        }
        selecting_back = profile_back;
        selecting_back.setBorderWidth(3);
        selected_back = selecting_back;

    }

    private void deleteSelectingProfileData() {
        selecting_back.setBorderWidth(0);
        selecting_profile = null;
        selected_back = null;
        selecting_back = null;
    }

    private class UpdateUserProfileNum extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPostExecute(String result) { //doInBackground에서 return한 값을 받음
            super.onPostExecute(result);
            //progressDialog.dismiss();
            Log.d(TAG, "response  - " + result);
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];

            try {
                String userID = "userID=" + UserInfo.getInstance().getUserID();
                String profileNum = "profileNum=" + UserInfo.getInstance().getUserProfileNum();
                StringBuffer stParams = new StringBuffer();
                stParams.append(userID);
                stParams.append("&");
                stParams.append(profileNum);

                String strParams = stParams.toString();
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(strParams.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
                //어플에서 데이터 전송

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }//연결상태 확인

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString().trim();
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();
                return null;
            }
        }
    }

}