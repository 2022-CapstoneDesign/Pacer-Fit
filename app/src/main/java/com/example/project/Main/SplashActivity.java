package com.example.project.Main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.project.Login.IntroActivity;
import com.example.project.Login.LoginRequest;
import com.example.project.R;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // 상태바 없이 전체화면
        setContentView(R.layout.main_splash_activity);

        Handler hd = new Handler();
        hd.postDelayed(new SplashHandler(), 2000); // 2초 대기 후 메인 액티비티로 전환

    }

    private class SplashHandler implements Runnable {
        public void run() {
            SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", Activity.MODE_PRIVATE);
            String loginid = sharedPreferences.getString("userid",null);
            String loginpwd = sharedPreferences.getString("userpwd",null);
            if(loginid != null && loginpwd != null){
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println("========================" + response);
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) { // 로그인에 성공한 경우
                                String userID = jsonObject.getString("userID");
                                String userPass = jsonObject.getString("userPassword");
                                String userName = jsonObject.getString("userName");
                                String userHeight = jsonObject.getString("userHeight"); // 키
                                String userWeight = jsonObject.getString("userWeight");
                                String pedo_max = jsonObject.getString("pedo_max");
                                String km_max = jsonObject.getString("km_max");
                                String userGender = jsonObject.getString("userGender");
                                String userAge = jsonObject.getString("userAge");
                                int userProfileNum = jsonObject.getInt("userProfileNum");
                                Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SplashActivity.this, BottomNavigation.class);// 메인 액티비티로 전환
                                intent.putExtra("userID", userID);
                                intent.putExtra("userPass", userPass);
                                intent.putExtra("userName",userName);
                                intent.putExtra("userHeight", userHeight); // 키
                                intent.putExtra("userWeight", userWeight);
                                intent.putExtra("pedo_max",pedo_max);
                                intent.putExtra("km_max",km_max);
                                intent.putExtra("userProfileNum",userProfileNum);
                                intent.putExtra("userGender", userGender);
                                intent.putExtra("userAge", userAge);
                                startActivity(intent);
                                finish();
                            } else { // 로그인에 실패한 경우
                                Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(loginid, loginpwd, responseListener);
                RequestQueue queue = Volley.newRequestQueue(SplashActivity.this);
                queue.add(loginRequest);
            }else {
                startActivity(new Intent(getApplication(), IntroActivity.class)); // 전환될 액티비티 지정
                SplashActivity.this.finish();
            }
        }
    }
}