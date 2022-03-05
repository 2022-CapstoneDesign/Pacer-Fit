package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main_Activity";
    private long backKeyPressedTime = 0;

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

    } // onCreate

    // 뒤로가기 버튼을 두 번 누를 시 종료되도록 설정
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 2초가 지났으면 Toast Show
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2초가 지나지 않았으면 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
        }
    }
    private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }
}