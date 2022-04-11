package com.example.project.Main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.Login.IntroActivity;
import com.example.project.R;

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
            startActivity(new Intent(getApplication(), IntroActivity.class)); // 전환될 액티비티 지정
            SplashActivity.this.finish();
        }
    }
}