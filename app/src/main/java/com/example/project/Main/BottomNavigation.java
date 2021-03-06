package com.example.project.Main;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.example.project.Ranking.UserInfo;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class BottomNavigation extends AppCompatActivity {
    private static final String TAG = "Main_Activity";
    private long backKeyPressedTime = 0;
    private final int ACCESS_FINE_LOCATION = 1000;
    private BottomNavigationView mBottomNavigationView;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_bottom_navigation);
        mBottomNavigationView = findViewById(R.id.bottom_navigation);
        menu = mBottomNavigationView.getMenu();
        //참고 : https://itstudy-mary.tistory.com/190
        //첫 화면 띄우기
        getSupportFragmentManager().beginTransaction().add(R.id.frame_container, new HomeFragment()).commit();
        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID"); //UserID 가져옴
        String userPass = intent.getStringExtra("userPass"); //UserPass 가져옴
        String userName = intent.getStringExtra("userName");
        String userGender = intent.getStringExtra("userGender"); // 성별
        String userHeight = intent.getStringExtra("userHeight"); // 키
        String userWeight = intent.getStringExtra("userWeight");
        String userAge = intent.getStringExtra("userAge"); // 나이
        String userStepsRecord = intent.getStringExtra("today_stepsRecord");
        String userHashtag = intent.getStringExtra("userHashtag"); // #
        int userLevelLike = Integer.parseInt(intent.getStringExtra("userLevelLike")); // 선호 난이도
        int userProfileNum = intent.getIntExtra("userProfileNum", 0);

        // 쉼표 단위로 끊어서 배열에 저장
        String[] hashTags = userHashtag.split(",");
        if (hashTags[0].equals(" ")) { // 공원
            UserInfo.getInstance().setTag_park(0);
        } else {
            UserInfo.getInstance().setTag_park(1);
        }
        if (hashTags[1].equals(" ")) { // 산
            UserInfo.getInstance().setTag_mountain(0);
        } else {
            UserInfo.getInstance().setTag_mountain(1);
        }
        if (hashTags[2].equals(" ")) { // 숲
            UserInfo.getInstance().setTag_forest(0);
        } else {
            UserInfo.getInstance().setTag_forest(1);
        }
        if (hashTags[3].equals(" ")) { // 바다
            UserInfo.getInstance().setTag_sea(0);
        } else {
            UserInfo.getInstance().setTag_sea(1);
        }
        if (hashTags[4].equals(" ")) { // 해변
            UserInfo.getInstance().setTag_beach(0);
        } else {
            UserInfo.getInstance().setTag_beach(1);
        }
        if (hashTags[5].equals(" ")) { // 트레킹
            UserInfo.getInstance().setTag_trekking(0);
        } else {
            UserInfo.getInstance().setTag_trekking(1);
        }
        if (hashTags[6].equals(" ")) { // 자연
            UserInfo.getInstance().setTag_nature(0);
        } else {
            UserInfo.getInstance().setTag_nature(1);
        }
        if (hashTags[7].equals(" ")) { // 명소
            UserInfo.getInstance().setTag_sights(0);
        } else {
            UserInfo.getInstance().setTag_sights(1);
        }
        if (hashTags[8].equals(" ")) { // 동네
            UserInfo.getInstance().setTag_town(0);
        } else {
            UserInfo.getInstance().setTag_town(1);
        }
        if (hashTags[9].equals(" ")) { // 풍경
            UserInfo.getInstance().setTag_scenery(0);
        } else {
            UserInfo.getInstance().setTag_scenery(1);
        }
        if (hashTags[10].equals(" ")) { // 역사
            UserInfo.getInstance().setTag_history(0);
        } else {
            UserInfo.getInstance().setTag_history(1);
        }

        System.out.println("userKg??????????????" + userWeight);
        mBottomNavigationView.setItemIconTintList(null);
        UserInfo.getInstance().setUserID(userID);
        UserInfo.getInstance().setUserPass(userPass);
        UserInfo.getInstance().setUserName(userName);
        UserInfo.getInstance().setUserProfileNum(userProfileNum);
        UserInfo.getInstance().setUserGender(userGender);
        UserInfo.getInstance().setUserHeight(userHeight);
        UserInfo.getInstance().setUserWeight(userWeight);
        UserInfo.getInstance().setUserAge(userAge);
        UserInfo.getInstance().setUserHashTags(hashTags);
        UserInfo.getInstance().setUserLevelLike(userLevelLike);
        System.out.println("================================" + userName);
        System.out.println("userStepsRecord======================:" + userStepsRecord);
        //case 함수를 통해 클릭 받을 때마다 화면 변경하기
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_1_home: //bottom_menu의 id를 가져옴
                        item.setIcon(R.drawable.bottom_home);
                        menu.findItem(R.id.bottom_2_rank).setIcon(R.drawable.unselected_rank);
                        menu.findItem(R.id.bottom_3_feed).setIcon(R.drawable.unselected_feed);
                        menu.findItem(R.id.bottom_4_account).setIcon(R.drawable.unselected_account);
                        getSupportFragmentManager().popBackStack();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new HomeFragment()).commit();
                        break;

                    case R.id.bottom_2_rank:
                        item.setIcon(R.drawable.bottom_rank);
                        menu.findItem(R.id.bottom_1_home).setIcon(R.drawable.unselected_home);
                        menu.findItem(R.id.bottom_3_feed).setIcon(R.drawable.unselected_feed);
                        menu.findItem(R.id.bottom_4_account).setIcon(R.drawable.unselected_account);
                        getSupportFragmentManager().popBackStack();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new RankingFragment()).commit();
                        break;

                    case R.id.bottom_3_feed:
                        item.setIcon(R.drawable.bottom_feed);
                        menu.findItem(R.id.bottom_1_home).setIcon(R.drawable.unselected_home);
                        menu.findItem(R.id.bottom_2_rank).setIcon(R.drawable.unselected_rank);
                        menu.findItem(R.id.bottom_4_account).setIcon(R.drawable.unselected_account);
                        getSupportFragmentManager().popBackStack();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new FeedFragment()).commit();
                        break;

                    case R.id.bottom_4_account:
                        item.setIcon(R.drawable.bottom_account);
                        menu.findItem(R.id.bottom_1_home).setIcon(R.drawable.unselected_home);
                        menu.findItem(R.id.bottom_2_rank).setIcon(R.drawable.unselected_rank);
                        menu.findItem(R.id.bottom_3_feed).setIcon(R.drawable.unselected_feed);
                        getSupportFragmentManager().popBackStack();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, MyPageFragment.class, null, "MyPageFragment_TAG").commit();
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

        // 중첩된 프래그먼트가 없을 때(메인 화면일 때)에만 메세지가 뜨도록 함
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 2초가 지났으면 Toast Show
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
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
        // 중첩된 프래그먼트가 있을 경우엔 그냥 뒤로 가기(이전 페이지 보여줌)
        else {
            super.onBackPressed();
        }
    }

    private void getHashKey() {
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