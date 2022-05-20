package com.example.project.Map;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;

public class RecordActivity extends AppCompatActivity {

    TextView time_tv;
    TextView dist_tv;
    TextView cal_tv;
    double cal;
    double dist;
    int time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_record_result);
        Intent intent = getIntent();
        cal = intent.getDoubleExtra("cal", 0.0);
        dist = intent.getDoubleExtra("dist", 0.0);
        time = intent.getIntExtra("time", 0);

        time_tv = findViewById(R.id.result_time);
        dist_tv = findViewById(R.id.result_dist);
        cal_tv = findViewById(R.id.result_cal);

        int hour = time / 3600;
        int min = time / 60 % 60;
        int sec = time % 60;
        String timeFormat = String.format("%02d:%02d:%02d", hour, min, sec);
        time_tv.setText(timeFormat);
        dist_tv.setText(Double.toString(dist));
        cal_tv.setText(Double.toString(cal));

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBackPressed() {
    }
}

