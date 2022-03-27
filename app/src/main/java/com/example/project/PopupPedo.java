package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class PopupPedo extends AppCompatActivity {

    private ImageView backBtn1;
    private Button startBtn;
    private Button backBtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //requestWindowFeature( Window.FEATURE_NO_TITLE );  // 타이틀 미사용
        setContentView(R.layout.activity_popup_pedo);

        backBtn1 = findViewById(R.id.popupPedoBackBtn);
        backBtn2 = findViewById(R.id.popupPedoBackBtn2);
        startBtn = findViewById(R.id.popupPedoStartBtn);

        backBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        backBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplication(), ThreeTwoOneGo.class); // 카운트다운
                startActivity(intent);
            }
        });
    }
}