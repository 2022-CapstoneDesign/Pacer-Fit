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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.project.Map.DetailBottomFragment;
import com.example.project.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class StepCounterActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager sensorManager;
    Sensor stepCountSensor;
    TextView pedoStep;
    TextView pedoTime;
    TextView pedoCal;
    TextView pedoStepText;
    TextView pedoTimeText;
    TextView pedoCalText;

    Timer timer;
    //사용자 DB 값
    String userID;
    String userKg;
    String date_concat;
    String today_userPedoRecord;
    String today_userPedoTimeRecord;
    String today_userPedoCalorieRecord;

    // 타이머 변수
    private int time = -1;
    TimerTask timerTask;
    int total_sec = 0;
    double calories = 0;

    Button recordStartPedo;
    ImageButton helpBtn;
    CircleImageView detailBack;

    // 현재 걸음 수
    int currentSteps;

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
        userID = intent.getStringExtra("userID");
//        today_userPedoRecord = intent.getStringExtra("userStepsRecord");
//        today_userPedoTimeRecord = intent.getStringExtra("userStepsTimeRecord");
//        today_userPedoCalorieRecord = intent.getStringExtra("userStepsCalorieRecord");
        today_userPedoRecord = intent.getStringExtra("today_stepsRecord");
        today_userPedoTimeRecord = intent.getStringExtra("today_stepsTimeRecord");
        today_userPedoCalorieRecord = intent.getStringExtra("today_stepsCalorieRecord");
        System.out.println("userID============="+userID);
        System.out.println("userWeight============="+userKg);
        System.out.println("today_userPedoRecord============="+today_userPedoRecord);
        System.out.println("today_userPedoTimeRecord============="+today_userPedoTimeRecord);
        System.out.println("today_userPedoCalorieRecord============="+today_userPedoCalorieRecord);

        pedoStep = findViewById(R.id.pedo_step);
        pedoStepText = findViewById(R.id.pedo_step_text);
        pedoTime = findViewById(R.id.pedo_time);
        pedoTimeText = findViewById(R.id.pedo_time_text);
        pedoCal = findViewById(R.id.pedo_cal);
        pedoCalText = findViewById(R.id.pedo_cal_text);

        recordStartPedo = findViewById(R.id.stopPedoBtn);
        detailBack = findViewById(R.id.pedo_detail_img);


        if(Integer.parseInt(today_userPedoRecord)!=0) {
            //db값들 testview에 보여주기
            currentSteps = Integer.parseInt(today_userPedoRecord);
            pedoStep.setText(String.valueOf(currentSteps));
            //calories = Double.parseDouble(today_userPedoCalorieRecord);
            calories = (currentSteps*((0.00007*Integer.parseInt(userKg))+0.04));
            pedoCal.setText(String.format("%.2f",calories) + "kcal");
            //DB값 시간설정
            time = Integer.parseInt(today_userPedoTimeRecord);
            int sec = time % 60;
            int min = time / 60 % 60;
            int hour = time / 3600;
            pedoTime.setText(hour + "H " + min + "M " + sec + "S");
        }
        else {
            currentSteps = 0;
            pedoStep.setText(String.valueOf(currentSteps));
            //DB값 시간설정
            time = Integer.parseInt(today_userPedoTimeRecord);
            int sec = time % 60;
            int min = time / 60 % 60;
            int hour = time / 3600;
            pedoTime.setText(hour + "H " + min + "M " + sec + "S");
        }

        // 초기 투명도 설정
        setTextAlpha(0.3f);
        // 어두운 배경
        detailBack.setImageResource(R.drawable.exer_pedo_background01);
        
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
        recordStartPedo.setOnClickListener(view -> {
            if(recordStartPedo.getText().equals("운동 시작")) {
                recordStartPedo.setText("그만하기");
                recordStartPedo.setBackgroundResource(R.drawable.btn_style4);
                detailBack.setImageResource(R.drawable.exer_pedo_background02);
                setTextAlpha(1f);
                startPedo();
            }
            else{
                recordStartPedo.setText("운동 시작");
                detailBack.setImageResource(R.drawable.exer_pedo_background01);
                recordStartPedo.setBackgroundResource(R.drawable.btn_style4_pedo_ready);
                setTextAlpha(0.3f);
                stopPedo();
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
    
    // 원 내부에 textview들 투명도 설정
    public void setTextAlpha(float values){
        pedoStep.setAlpha(values);
        pedoStepText.setAlpha(values);
        pedoTime.setAlpha(values);
        pedoTimeText.setAlpha(values);
        pedoCal.setAlpha(values);
        pedoCalText.setAlpha(values);
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
        //DB에 사용자 운동데이터 값 저장
        // DB추가
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate now = null;
            now = LocalDate.now();

            int monthValue = now.getMonthValue();
            int dayOfMonth = now.getDayOfMonth();
            date_concat = monthValue + "m" + dayOfMonth + "d";
        }
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("========================" + response);
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) { // 만보기 클릭시
                        System.out.println("성공");
                    } else { // 로그인에 실패한 경우
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PedoRecordSaveRequest pedoRecordSaveRequest = new PedoRecordSaveRequest(userID, currentSteps+"",time+"", String.format("%.2f",calories), responseListener);
        RequestQueue queue = Volley.newRequestQueue(StepCounterActivity.this);
        queue.add(pedoRecordSaveRequest);
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
                    pedoStep.setText(String.valueOf(currentSteps));
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
                        pedoStep = findViewById(R.id.pedo_step);
                        pedoTime = findViewById(R.id.pedo_time);
                        pedoCal = findViewById(R.id.pedo_cal);
                        if (pedoTime != null)
                            pedoTime.setText(hour + "H " + min + "M " + sec + "S");
                        if (pedoStep != null)
                            pedoStep.setText(String.valueOf(currentSteps));
                        if (pedoCal != null) {
                            //calories = (currentSteps*((0.0007*Integer.parseInt(userKg))+0.04));
                            pedoCal.setText(String.format("%.2f",calories) + "kcal");
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