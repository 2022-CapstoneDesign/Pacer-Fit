package com.example.project.Main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

import java.util.ArrayList;
import java.util.List;


public class FeedFragment extends Fragment{
//    Button detailfrag;

    RecyclerView newsRecyclerView;
    RecyclerView fitnessRecyclerView;
    LinearLayoutManager newsLayoutManager;
    LinearLayoutManager fitnessLayoutManager;
    List<FeedRecyclerModel> newsList;
    List<FeedRecyclerModel> fitnessList;
    FeedRecyclerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.main_feed_fragment,container,false);
//        detailfrag = v.findViewById(R.id.DetailFrag);
//        detailfrag.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                FragmentManager fm = getActivity().getSupportFragmentManager();
//                fm.beginTransaction()
//                        .replace(R.id.frame_container, new DetailFragment())
//                        .addToBackStack(null).commit();
//            }
//        });

//        List<YouTubeContent> contents = new ArrayList<>();
//        // 로꼬(Loco) - Party Band + OPPA
//        contents.add(new YouTubeContent("ieZ_qkyhLwU"));

        newsRecyclerView = v.findViewById(R.id.news_recycler_view);
        fitnessRecyclerView = v.findViewById(R.id.fitness_recycler_view);

//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(new YouTubeAdapter(contents));

        initNewsData();
        initNewsRecyclerView();

        initFitnessData();
        initFitnessRecyclerView();

//        YouTubePlayerView youTubePlayerView = new YouTubePlayerView(getContext());
//        layout.addView(youTubePlayerView);
//        WebView myWebView = (WebView) v.findViewById(R.id.webview2);
//        myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6MTt9&bmode=view&idx=6498397&t=board");
//        WebSettings webSettings = myWebView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        // 웹뷰의 설정 수정하기
//
//        myWebView.setWebViewClient(new WebViewClient());
        return v;
    }

    private void initNewsData() { // 뉴스 데이터 설정
        newsList = new ArrayList<>();
        newsList.add(new FeedRecyclerModel(R.drawable.feed_news1, "시간을 쪼개 쓰는 하루 3분 나노 운동"));
        newsList.add(new FeedRecyclerModel(R.drawable.feed_news2, "나도 혹시 대사증후군? 이 음식으로 극복!!"));
        newsList.add(new FeedRecyclerModel(R.drawable.feed_news3, "구두를 신지 않는데 무지외반 이라고?"));
        newsList.add(new FeedRecyclerModel(R.drawable.feed_news4, "다이어트 빵 단백질 빵의 오해와 진실"));
        newsList.add(new FeedRecyclerModel(R.drawable.feed_news6, "단백질 보충제 먹었는데 설사에 피부 트러블이 난다면?"));
        newsList.add(new FeedRecyclerModel(R.drawable.feed_news7, "단백질 부족 비상, 노년층 단백질 보충제 먹어도 될까?"));
        newsList.add(new FeedRecyclerModel(R.drawable.feed_news8, "운동 전문가 3인이 추천하는 홈트레이닝 도구"));
        newsList.add(new FeedRecyclerModel(R.drawable.feed_news9, "불면증, 자기 전 따뜻한 우유 한잔 살 안 찔까?"));
        newsList.add(new FeedRecyclerModel(R.drawable.feed_news10, "숨쉬기운동! 발가락운동! 우습게 보지 마세요!"));
        newsList.add(new FeedRecyclerModel(R.drawable.feed_news11, "기구 필라테스, 매트 필라테스, 소도구 필라테스에 대한 차이점"));
        newsList.add(new FeedRecyclerModel(R.drawable.feed_news12, "남자가 기구 필라테스 해야하는 이유!"));
    }

    private void initNewsRecyclerView() {
        newsLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        newsLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        newsRecyclerView.setLayoutManager(newsLayoutManager);
        adapter = new FeedRecyclerAdapter(getContext(), newsList, R.layout.main_feed_news_item);
        newsRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void initFitnessData() { // 피트니스 데이터 설정
        fitnessList = new ArrayList<>();
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness0, "침대에서 스트레칭으로 하루를 시작 하세요~"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness1, "뭉친 어깨, 목, 스트레칭 마사지"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness2, "피로가 녹아내리는 발바닥 종아리 허벅지 마사지 Lower Body Massage"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness3, "수고했어 오늘도! 10분 전신 마사지 Total Body Massage"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness4, "누구나 쉽게 따라 할 수 있는 7분 스텝 운동"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness5, "필라테스 / 스트레칭 으로 코어 운동 입문하기~"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness6, "매일 \'5분 스트레칭 체조\'"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness7, "직장인 스트레스를 풀어주는 오피스 스트레칭"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness8, "업무피로 사라지는 3분 스트레칭"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness9, "소파에서 하는 필라테스 스트레칭"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness11, "집에서 하는 매트 필라테스 스트레칭"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness13, "슬기로운 공원운동 하기"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness14, "덤벨 다이어트 듀엣 홈트운동 LV1"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness15, "덤벨 다이어트 듀엣 홈트운동 LV2"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness16, "덤벨 다이어트 듀엣 홈트운동 LV3"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness17, "런지 100회 챌린지! every day 100 Lunge Challenge!"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness18, "놀면 뭐하니? 친구야~ 방구석 걷기 다이어트 하자~! Walk at Home~!"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness19, "운동 초보 여자 & 시니어 기초 체력 운동"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness20, "무조껀 옆구리 살 빠지는 10분 운동 꼭 따라 하세요!!"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness21, "초보자도 쉽게 할 수 있는 어깨가 예뻐지는 푸쉬업! 100회 챌린지!"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness22, "단기간에 살빠지는 전신 다이어트 운동! 칼로리 불태우기!"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness23, "딱! 10분! 하루의 피로를 풀어주는 폼롤러 하체 마사지 & 스트레칭"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness24, "매일 10분! 굽은등! 굽은어깨! 자세교정 스트레칭 운동! 온가족이 함께해요."));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness25, "매일 10분! 근막 전신 스트레칭으로 다이어트 효과까지!"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness26, "상체 근력 순환 운동! 이것만 매일 해도 어깨와 등 라인이 달라져요!"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness27, "다리가 매일 붓는다? 힙과 허벅지에 탄력이 없다? 매일 10분만!"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness28, "필수 매트 필라테스의 핵심만 꼭 집어 보여드려요! #필라테스 #코어강화 #골반교정 #전신스트레칭"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness29, "팔뚝살 신나고 쉽게 빼기! No Equipment & Quick!"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness30, "7MIN !! FULL BODY FAT BURN !! 7분이면 충분해요 ~ 전신 타바타!"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness31, "6MIN EVERY DAY HIP SQUAT! 매일 6분! 예쁜 힙 만들기!"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness32, "Side Booty & Hip Dips! 볼륨감 있는 힙을 위해!"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.feed_fitness10, "스쿼트 100회 챌린지"));
    }

    private void initFitnessRecyclerView() {
        fitnessLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        //fitnessLayoutManager.setOrientation(RecyclerView.VERTICAL);
        fitnessRecyclerView.setLayoutManager(fitnessLayoutManager);
        adapter = new FeedRecyclerAdapter(getContext(), fitnessList, R.layout.main_feed_fragment_item);
        fitnessRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

}