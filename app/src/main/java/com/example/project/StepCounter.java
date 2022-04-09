package com.example.project;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class StepCounter extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    Sensor stepCountSensor;
    TextView stepCountView;


    // 현재 걸음 수
    int currentSteps = 0;
    @RequiresApi(api = Build.VERSION_CODES.Q)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepcounter);

        stepCountView = findViewById(R.id.StepCountView);

        // 활동 퍼미션 체크
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){

            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }
        // 걸음 센서 연결
        // * 옵션
        // - TYPE_STEP_DETECTOR:  리턴 값이 무조건 1, 앱이 종료되면 다시 0부터 시작
        // - TYPE_STEP_COUNTER : 앱 종료와 관계없이 계속 기존의 값을 가지고 있다가 1씩 증가한 값을 리턴
        //
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        // 디바이스에 걸음 센서의 존재 여부 체크
        if (stepCountSensor == null) {
            Toast.makeText(this, "No Step Sensor", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this, stepCountSensor,SensorManager.SENSOR_DELAY_FASTEST);
        // 센서 속도 설정
        // * 옵션
        // - SENSOR_DELAY_NORMAL: 20,000 초 딜레이
        // - SENSOR_DELAY_UI: 6,000 초 딜레이
        // - SENSOR_DELAY_GAME: 20,000 초 딜레이
        // - SENSOR_DELAY_FASTEST: 딜레이 없음
    }



    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // 걸음 센서 이벤트 발생시
        if(sensorEvent.sensor.getType() == Sensor.TYPE_STEP_DETECTOR){


            if(sensorEvent.values[0]==1.0f){
                // 센서 이벤트가 발생할때 마다 걸음수 증가
                currentSteps += sensorEvent.values[0];
                Log.d("만보기", String.valueOf(currentSteps));
                stepCountView.setText(String.valueOf(currentSteps));
            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}