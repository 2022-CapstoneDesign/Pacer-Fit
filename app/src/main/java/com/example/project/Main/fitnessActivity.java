package com.example.project.Main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.project.R;

public class fitnessActivity extends Activity {
    private String view_title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fitness_activity);
        Intent intent = getIntent();
        view_title = intent.getStringExtra("TEXT");

        WebView myWebView = (WebView) findViewById(R.id.webview);
        if(view_title.equals("시간을 쪼개 쓰는 하루 3분 나노 운동"))
            myWebView.loadUrl("https://blesslifestore.co/hnews/?q=YToxOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjt9&bmode=view&idx=1669112&t=board");
        else if(view_title.equals("나도 혹시 대사증후군? 이 음식으로 극복!!"))
            myWebView.loadUrl("https://blesslifestore.co/hnews/?q=YToxOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjt9&bmode=view&idx=1669130&t=board");
        else if(view_title.equals("구두를 신지 않는데 무지외반 이라고?"))
            myWebView.loadUrl("https://blesslifestore.co/hnews/?q=YToxOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjt9&bmode=view&idx=1669189&t=board");
        else if(view_title.equals("다이어트 빵 단백질 빵의 오해와 진실"))
            myWebView.loadUrl("https://blesslifestore.co/hnews/?q=YToxOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjt9&bmode=view&idx=1669216&t=board");
        else if(view_title.equals("단백질 보충제 먹었는데 설사에 피부 트러블이 난다면?"))
            myWebView.loadUrl("https://blesslifestore.co/hnews/?q=YToxOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjt9&bmode=view&idx=1669258&t=board");
        else if(view_title.equals("단백질 부족 비상, 노년층 단백질 보충제 먹어도 될까?"))
            myWebView.loadUrl("https://blesslifestore.co/hnews/?q=YToxOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjt9&bmode=view&idx=1669266&t=board");
        else if(view_title.equals("운동 전문가 3인이 추천하는 홈트레이닝 도구"))
            myWebView.loadUrl("https://blesslifestore.co/hnews/?q=YToxOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjt9&bmode=view&idx=1669304&t=board");
        else if(view_title.equals("불면증, 자기 전 따뜻한 우유 한잔 살 안 찔까?"))
            myWebView.loadUrl("https://blesslifestore.co/hnews/?q=YToxOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjt9&bmode=view&idx=1669332&t=board");
        else if(view_title.equals("숨쉬기운동! 발가락운동! 우습게 보지 마세요!"))
            myWebView.loadUrl("https://blesslifestore.co/hnews/?q=YToxOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjt9&bmode=view&idx=1669364&t=board");
        else if(view_title.equals("기구 필라테스, 매트 필라테스, 소도구 필라테스에 대한 차이점"))
            myWebView.loadUrl("https://blesslifestore.co/hnews/?q=YToxOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjt9&bmode=view&idx=1972498&t=board");
        else if(view_title.equals("남자가 기구 필라테스 해야하는 이유!"))
            myWebView.loadUrl("https://blesslifestore.co/hnews/?q=YToxOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjt9&bmode=view&idx=1972614&t=board");

        else if(view_title.equals("뭉친 어깨, 목, 스트레칭 마사지"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6MTU7fQ%3D%3D&bmode=view&idx=501903&t=board");
        else if(view_title.equals("침대에서 스트레칭으로 하루를 시작 하세요~"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6MTA7fQ%3D%3D&bmode=view&idx=3289411&t=board");
        else if(view_title.equals("피로가 녹아내리는 발바닥 종아리 허벅지 마사지 Lower Body Massage"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6MTQ7fQ%3D%3D&bmode=view&idx=501911&t=board");
        else if(view_title.equals("수고했어 오늘도! 10분 전신 마사지 Total Body Massage"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6MTQ7fQ%3D%3D&bmode=view&idx=501914&t=board");
        else if(view_title.equals("누구나 쉽게 따라 할 수 있는 7분 스텝 운동"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6MTM7fQ%3D%3D&bmode=view&idx=3026299&t=board");
        else if(view_title.equals("필라테스 / 스트레칭 으로 코어 운동 입문하기~"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6MTI7fQ%3D%3D&bmode=view&idx=3041064&t=board");
        else if(view_title.equals("매일 \'5분 스트레칭 체조\'"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6MTI7fQ%3D%3D&bmode=view&idx=3041069&t=board");
        else if(view_title.equals("직장인 스트레스를 풀어주는 오피스 스트레칭"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6MTI7fQ%3D%3D&bmode=view&idx=3107074&t=board");
        else if(view_title.equals("업무피로 사라지는 3분 스트레칭"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6MTE7fQ%3D%3D&bmode=view&idx=3123286&t=board");
        else if(view_title.equals("소파에서 하는 필라테스 스트레칭"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6MTE7fQ%3D%3D&bmode=view&idx=3162814&t=board");
        else if(view_title.equals("집에서 하는 매트 필라테스 스트레칭"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6MTA7fQ%3D%3D&bmode=view&idx=3303367&t=board");
        else if(view_title.equals("슬기로운 공원운동 하기"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6OTt9&bmode=view&idx=3547755&t=board");
        else if(view_title.equals("덤벨 다이어트 듀엣 홈트운동 LV1"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6ODt9&bmode=view&idx=3628908&t=board");
        else if(view_title.equals("덤벨 다이어트 듀엣 홈트운동 LV2"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6Nzt9&bmode=view&idx=3638338&t=board");
        else if(view_title.equals("덤벨 다이어트 듀엣 홈트운동 LV3"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6Nzt9&bmode=view&idx=3670181&t=board");
        else if(view_title.equals("런지 100회 챌린지! every day 100 Lunge Challenge!"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6Nzt9&bmode=view&idx=3688942&t=board");
        else if(view_title.equals("놀면 뭐하니? 친구야~ 방구석 걷기 다이어트 하자~! Walk at Home~!"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6Nzt9&bmode=view&idx=3715649&t=board");
        else if(view_title.equals("운동 초보 여자 & 시니어 기초 체력 운동"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6Njt9&bmode=view&idx=3807675&t=board");
        else if(view_title.equals("무조껀 옆구리 살 빠지는 10분 운동 꼭 따라 하세요!!"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6Njt9&bmode=view&idx=3823562&t=board");
        else if(view_title.equals("초보자도 쉽게 할 수 있는 어깨가 예뻐지는 푸쉬업! 100회 챌린지!"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6NTt9&bmode=view&idx=3835308&t=board");
        else if(view_title.equals("단기간에 살빠지는 전신 다이어트 운동! 칼로리 불태우기!"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6NTt9&bmode=view&idx=3868308&t=board");
        else if(view_title.equals("딱! 10분! 하루의 피로를 풀어주는 폼롤러 하체 마사지 & 스트레칭"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6NDt9&bmode=view&idx=3987401&t=board");
        else if(view_title.equals("매일 10분! 굽은등! 굽은어깨! 자세교정 스트레칭 운동! 온가족이 함께해요."))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6NDt9&bmode=view&idx=4205533&t=board");
        else if(view_title.equals("매일 10분! 근막 전신 스트레칭으로 다이어트 효과까지!"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6NDt9&bmode=view&idx=4473708&t=board");
        else if(view_title.equals("상체 근력 순환 운동! 이것만 매일 해도 어깨와 등 라인이 달라져요!"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6Mzt9&bmode=view&idx=4761335&t=board");
        else if(view_title.equals("다리가 매일 붓는다? 힙과 허벅지에 탄력이 없다? 매일 10분만!"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6Mzt9&bmode=view&idx=4858328&t=board");
        else if(view_title.equals("필수 매트 필라테스의 핵심만 꼭 집어 보여드려요! #필라테스 #코어강화 #골반교정 #전신스트레칭"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6Mzt9&bmode=view&idx=4947889&t=board");
        else if(view_title.equals("팔뚝살 신나고 쉽게 빼기! No Equipment & Quick!"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6Mjt9&bmode=view&idx=6213897&t=board");
        else if(view_title.equals("7MIN !! FULL BODY FAT BURN !! 7분이면 충분해요 ~ 전신 타바타!"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6MTt9&bmode=view&idx=6498397&t=board");
        else if(view_title.equals("6MIN EVERY DAY HIP SQUAT! 매일 6분! 예쁜 힙 만들기!"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6MTt9&bmode=view&idx=6782174&t=board");
        else if(view_title.equals("Side Booty & Hip Dips! 볼륨감 있는 힙을 위해!"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6MTt9&bmode=view&idx=7083171&t=board");
        else if(view_title.equals("스쿼트 100회 챌린지"))
            myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6MTE7fQ%3D%3D&bmode=view&idx=3171989&t=board");
        else
            myWebView.loadUrl("https://www.naver.com");
        myWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        WebSettings webSettings = myWebView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        // 웹뷰의 설정 수정하기

        WebSettings mWebSettings = myWebView.getSettings();
// 웹뷰에 동영상을 바로 실행시키기 위함.
        mWebSettings.setMediaPlaybackRequiresUserGesture(false);
// 자바 스크립트 사용
        mWebSettings.setJavaScriptEnabled(true);
// 뷰 가속 - 가속하지 않으면 영상실행 안됨, 소리만 나온다

        //전체화면
        myWebView.setWebChromeClient(new FullscreenableChromeClient(fitnessActivity.this));

        myWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        myWebView.setWebViewClient(new WebViewClient());
    }
}
