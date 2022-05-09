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
        newsList.add(new FeedRecyclerModel(R.drawable.weather_snow, "테스트1"));
        newsList.add(new FeedRecyclerModel(R.drawable.weather_rainy, "짧은제목"));
        newsList.add(new FeedRecyclerModel(R.drawable.weather_sunny, "짧은제목짧은제목짧은제목"));
        newsList.add(new FeedRecyclerModel(R.drawable.weather_cloudy, "짧은제목짧은제목짧은제목짧은제목"));
        newsList.add(new FeedRecyclerModel(R.drawable.weather_heavy_cloudy, "긴제목긴제목긴제목긴제목긴제목긴제목긴제목긴제목긴제목긴제목긴제목긴제목긴제목"));
    }

    private void initNewsRecyclerView() {
        newsLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        newsLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        newsRecyclerView.setLayoutManager(newsLayoutManager);
        adapter = new FeedRecyclerAdapter(newsList, R.layout.main_feed_news_item);
        newsRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void initFitnessData() { // 피트니스 데이터 설정
        fitnessList = new ArrayList<>();
        fitnessList.add(new FeedRecyclerModel(R.drawable.weather_snow, "테스트2"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.weather_rainy, "테스트2"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.weather_sunny, "테스트2"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.weather_cloudy, "테스트2"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.weather_heavy_cloudy, "테스트2"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.weather_heavy_cloudy, "테스트2"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.weather_heavy_cloudy, "테스트2"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.weather_heavy_cloudy, "테스트2"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.weather_heavy_cloudy, "테스트2"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.weather_heavy_cloudy, "테스트2"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.weather_heavy_cloudy, "테스트2"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.weather_heavy_cloudy, "테스트2"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.weather_heavy_cloudy, "테스트2"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.weather_heavy_cloudy, "테스트2"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.weather_heavy_cloudy, "테스트2"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.weather_heavy_cloudy, "테스트2"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.weather_heavy_cloudy, "테스트2"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.weather_heavy_cloudy, "테스트2"));
        fitnessList.add(new FeedRecyclerModel(R.drawable.weather_heavy_cloudy, "테스트2"));
    }

    private void initFitnessRecyclerView() {
        fitnessLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        //fitnessLayoutManager.setOrientation(RecyclerView.VERTICAL);
        fitnessRecyclerView.setLayoutManager(fitnessLayoutManager);
        adapter = new FeedRecyclerAdapter(fitnessList, R.layout.main_feed_fragment_item);
        fitnessRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

}