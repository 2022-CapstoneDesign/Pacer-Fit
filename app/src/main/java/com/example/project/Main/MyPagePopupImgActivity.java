package com.example.project.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;

import com.example.project.R;

public class MyPagePopupImgActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //requestWindowFeature( Window.FEATURE_NO_TITLE );  // 타이틀 미사용
        setContentView(R.layout.activity_my_page_popup_img);


    }
}