package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main_Activity";

    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationView=findViewById(R.id.bottom_navigation);
        //참고 : https://itstudy-mary.tistory.com/190
        //첫 화면 띄우기
        getSupportFragmentManager().beginTransaction().add(R.id.frame_container,new Frag1()).commit();

        //case 함수를 통해 클릭 받을 때마다 화면 변경하기
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_1_home: //bottom_menu의 id를 가져옴
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new Frag1()).commit();
                        break;
                    case R.id.bottom_2_rank:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new Frag2()).commit();
                        break;
                    case R.id.bottom_3_chat:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new Frag3()).commit();
                        break;
                    case R.id.bottom_4_account:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new Frag4()).commit();
                        break;
                }
                return true;
            }
        });
    }
}