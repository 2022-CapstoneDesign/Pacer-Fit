package com.example.project.Main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.R;

public class DetailFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_detail_fragment, container, false);
//        WebView webView = (WebView)v.findViewById(R.id.webview2);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
//        webView.getSettings().setPluginsEnabled(true);
//        webView.getSettings().setSupportMultipleWindows(true);
//
//        String url = "url";
//
//        webView.loadUrl(url);
//
//        webView.setWebChromeClient(new ChromeClient());
//        webView.setWebViewClient(new WebClient());

        WebView myWebView = (WebView) v.findViewById(R.id.webview2);
        myWebView.loadUrl("https://blesslifestore.co/youtube/?q=YToyOntzOjEyOiJrZXl3b3JkX3R5cGUiO3M6MzoiYWxsIjtzOjQ6InBhZ2UiO2k6MTt9&bmode=view&idx=6498397&t=board");
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

        myWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        myWebView.setWebViewClient(new WebViewClient());


        return v;
    }
}
