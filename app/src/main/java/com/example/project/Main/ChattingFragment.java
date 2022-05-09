package com.example.project.Main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.YouTube.YouTubeAdapter;
import com.example.project.YouTube.YouTubeContent;

import java.util.ArrayList;
import java.util.List;


public class ChattingFragment extends Fragment{
    Button detailfrag;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.main_chatting_fragment,container,false);
        detailfrag = v.findViewById(R.id.DetailFrag);
        detailfrag.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.frame_container, new DetailFragment())
                        .addToBackStack(null).commit();
            }
        });

        List<YouTubeContent> contents = new ArrayList<>();
        // 로꼬(Loco) - Party Band + OPPA
        contents.add(new YouTubeContent("ieZ_qkyhLwU"));

        RecyclerView recyclerView = v.findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new YouTubeAdapter(contents));
//        YouTubePlayerView youTubePlayerView = new YouTubePlayerView(getContext());
//        layout.addView(youTubePlayerView);
        WebView myWebView = (WebView) v.findViewById(R.id.webview2);
        myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6MTt9&bmode=view&idx=6498397&t=board");
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // 웹뷰의 설정 수정하기

        myWebView.setWebViewClient(new WebViewClient());
        return v;
    }

}