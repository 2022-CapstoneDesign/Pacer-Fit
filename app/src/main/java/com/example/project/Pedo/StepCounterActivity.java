package com.example.project.Pedo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.project.Map.DetailBottomFragment;
import com.example.project.Map.RecordFragment;
import com.example.project.R;

import java.util.Timer;
import java.util.TimerTask;

public class StepCounterActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager sensorManager;
    Sensor stepCountSensor;
    TextView stepCountView;
    TextView timeText;
    Timer timer;
    //사용자 DB 값
    String userKg;

    // 타이머 변수
    private int time = -1;
    TimerTask timerTask;
    int total_sec = 0;
    double calories;
    TextView step_tv;
    TextView time_tv;
    TextView cal_tv;
    Button recordStartPedo;
    ImageButton helpBtn;
    // 현재 걸음 수
    int currentSteps = 0;

    // n초동안 클릭 방지
    Handler h = new Handler();

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedo_stepcounter_activity);
        Intent intent = getIntent();

        //currentSteps를 DB값으로 뽑아온후에 시작할때 당일 운동 데이터값 보여주기
        //DB 사용자 몸무게 가져와서 cal계산. 거리는 8.0km/h로 계산.
        userKg = intent.getStringExtra("userWeight");
        System.out.println("userWeight============="+userKg);

        stepCountView = findViewById(R.id.StepCountView);
        timeText = findViewById(R.id.timeText);
        recordStartPedo = findViewById(R.id.stopPedoBtn);
        // 활동 퍼미션 체크
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }
        // 걸음 센서 연결
        // * 옵션
        // - TYPE_STEP_DETECTOR:  리턴 값이 무조건 1, 앱이 종료되면 다시 0부터 시작
        // - TYPE_STEP_COUNTER : 앱 종료와 관계없이 계속 기존의 값을 가지고 있다가 1씩 증가한 값을 리턴
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        // 디바이스에 걸음 센서의 존재 여부 체크
        if (stepCountSensor == null) {
            Toast.makeText(this, "No Step Sensor", Toast.LENGTH_SHORT).show();
        }
        recordStartPedo.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recordStartPedo.getText().equals("운동 시작")) {
                    recordStartPedo.setText("그만하기");
                    recordStartPedo.setBackgroundResource(R.drawable.btn_style4);
                    startPedo();
                }
                else{
                    recordStartPedo.setText("운동 시작");
                    recordStartPedo.setBackgroundResource(R.drawable.btn_style4_pedo_ready);
                    stopPedo();
                }
            }
        });

        //Pedo 도움말 버튼
        helpBtn = findViewById(R.id.pedoHelpBtn);
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View map) {
                DetailBottomFragment helpFragment = new DetailBottomFragment(getApplicationContext());
                helpFragment.show(getSupportFragmentManager(), helpFragment.getTag());
            }
        });
    }

    public void startPedo(){
        startTimer();
        recordStartPedo.setEnabled(false); // 클릭 무효화
        h.postDelayed(new splashhandler(), 1500);//1.5초 지연
    }

    public void stopPedo(){
        pauseTimer();
        recordStartPedo.setEnabled(false); // 클릭 무효화
        h.postDelayed(new splashhandler(), 1500);//1.5초 지연
        //DB에 사용자 운동데이터 값 넣기

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
        if(recordStartPedo.getText().equals("그만하기")) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
                if (sensorEvent.values[0] == 1.0f) {
                    // 센서 이벤트가 발생할때 마다 걸음수 증가
                    currentSteps += sensorEvent.values[0];
                    Log.d("만보기", String.valueOf(currentSteps));
                    stepCountView.setText(String.valueOf(currentSteps));
                    //1kg당 1보 계산식 (5.0km/h기준)
                    calories = (currentSteps*((0.00007*Integer.parseInt(userKg))+0.04));
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    //그만하기누르지 않고 뒤로가기키 눌렀을때
    @Override
    public void onBackPressed(){
        //운동 시작 하고 Back키 눌렀을때 예외처리
        if(recordStartPedo.getText().equals("그만하기")){
            Toast.makeText(getApplicationContext(), "그만하기를 눌러주세요.", Toast.LENGTH_SHORT).show();
        }
        else
            super.onBackPressed();
    }



    // 타이머 기능
    private void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                time++;
                total_sec = time;
                int sec = time % 60;
                int min = time / 60 % 60;
                int hour = time / 3600;

                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        // textview의 값들이 계속 null로 나와서 여기에 뒀습니다.
                        step_tv = findViewById(R.id.StepCountView);
                        time_tv = findViewById(R.id.timeText);
                        cal_tv = findViewById(R.id.pedo_calText);
                        if (time_tv != null)
                            time_tv.setText(hour + "H " + min + "M " + sec + "S");
                        if (step_tv != null)
                            step_tv.setText(String.valueOf(currentSteps));
                        if (cal_tv != null) {
                            //calories = (currentSteps*((0.0007*Integer.parseInt(userKg))+0.04));
                            cal_tv.setText(String.format("%.2f",calories) + "kcal");
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    // 타이머 정지
    private void pauseTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    private class splashhandler implements Runnable {
        @Override
        public void run() {
            recordStartPedo.setEnabled(true); // 클릭 유효화
        }
    }
}