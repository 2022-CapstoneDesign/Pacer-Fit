package com.example.project.Pedo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;

public class PopupActivity extends AppCompatActivity {

    private ImageView backBtn1;
    private Button startBtn;
    private Button backBtn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //requestWindowFeature( Window.FEATURE_NO_TITLE );  // 타이틀 미사용
        setContentView(R.layout.pedo_popup_activity);

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
                Intent intent = new Intent(getApplication(), CountDownActivity.class); // 카운트다운
                startActivity(intent);
            }
        });
    }
}