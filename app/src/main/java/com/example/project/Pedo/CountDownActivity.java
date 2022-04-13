package com.example.project.Pedo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;

public class CountDownActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedo_countdown_activity);

        Handler hd = new Handler();
        hd.postDelayed(new SplashHandler(), 4000); // 4초 대기 후 전환
    }

    private class SplashHandler implements Runnable {
        public void run() {
            startActivity(new Intent(getApplication(), StepCounterActivity.class)); // 만보기
            overridePendingTransition(0, 0); // 액티비티 전환 애니메이션 제거
            finish();
        }
    }
}