package com.example.project.Main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.project.R;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.LineData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FeedFragment extends Fragment{
//    Button detailfrag;

    RecyclerView newsRecyclerView;
    RecyclerView realnewsRecyclerView;
    RecyclerView fitnessRecyclerView;
    LinearLayoutManager newsLayoutManager;
    LinearLayoutManager fitnessLayoutManager;
    LinearLayoutManager realnewsLayoutManager;
    List<FeedRecyclerModel> newsList;
    List<FeedRecyclerModel> fitnessList;
    List<RealNewsFeedRecyclerModel> realnewsList;
    FeedRecyclerAdapter adapter;
    RealNewsFeedRecyclerAdapter adapter2;
    StringBuffer response;
    Handler handler = new Handler(Looper.getMainLooper());
    String[] title = new String[11];
    String[] link = new String[11];
    RealNewsData newsData = new RealNewsData();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.main_feed_fragment,container,false);
        newsRecyclerView = v.findViewById(R.id.news_recycler_view);
        fitnessRecyclerView = v.findViewById(R.id.fitness_recycler_view);
        realnewsRecyclerView = v.findViewById(R.id.real_time_news_recycler_view);
        SearchNews("news", "면역");

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

    private void SearchNews(final String _category, final String searchWord) {
        new Thread() {
            @Override
            public void run() {
                String clientId = "pQoRGrYcpVcsgzupVeuV";//애플리케이션 클라이언트 아이디값";
                String clientSecret = "E_60TMvvs0";//애플리케이션 클라이언트 시크릿값";
                try {
                    String text = URLEncoder.encode(searchWord, "UTF-8");
//                        String apiURL = "https://openapi.naver.com/v1/search/" + _category + "?query=" + text +"&display=20"; // json 결과
                    String apiURL = "https://openapi.naver.com/v1/search/"+ _category +".json?query="+text+"&display=10&start=1&sort=date";    // json 결과
                    //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과
                    URL url = new URL(apiURL);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("X-Naver-Client-Id", clientId);
                    con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                    int responseCode = con.getResponseCode();
                    BufferedReader br;
                    if (responseCode == 200) { // 정상 호출
                        br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                    } else {  // 에러 발생
                        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                    }

                    int i=0;int ii=0;
                    String inputLine;
                    response = new StringBuffer();
                    StringBuffer response2 = new StringBuffer();
                    while ((inputLine = br.readLine()) != null) {
                        response.append(inputLine);
                        response.append("\n");
                        if(inputLine.contains("title")){
//                            Pattern pattern = Pattern.compile("[\"](.*?)[\"]");
//                            Matcher matcher = pattern.matcher(inputLine);
//                            while (matcher.find()) {  // 일치하는 게 있다면
//                                title[i++] = matcher.group(1);
//                                if (matcher.group(1) == null)
//                                    break;
//                            }
                            title[i++] = inputLine.substring(12,inputLine.length()-2).replace("<b>", "").replace("<\\/b>", "").replace("&quot;","\"");

                        }
                        if(inputLine.contains("\"link\"")){
                            link[ii++] = inputLine.substring(11,inputLine.length()-2);
                        }
                    }
                    br.close();
                    String naverHtml = response.toString();

                    Bundle bun = new Bundle();
                    bun.putString("NAVER_HTML", naverHtml);
                    Message msg = handler.obtainMessage();
                    msg.setData(bun);
                    handler.sendMessage(msg);

//                        testText.setText(response.toString());
                    System.out.println(response.toString());
                    for(int j=0; j<10; j++){
//                        System.out.println("title and link================="+title[j]+link[j]);
                        newsData.setRealTimeNews(j,title[j],link[j]);
                    }

                    ArrayList<String> data = new ArrayList<>();
                    for(int d=0; d<10; d++){
                        data.add(title[d]);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initRealNewsData();
                            initRealNewsRecyclerView();
                        }
                    });
//                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,data);
//                    ListView listview = (ListView) v.findViewById(R.id.listview);
//                    listview.setAdapter(adapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }.start();

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

    private void initRealNewsRecyclerView() {
        realnewsLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        realnewsLayoutManager.setOrientation(RecyclerView.VERTICAL);
        realnewsRecyclerView.setLayoutManager(realnewsLayoutManager);
        adapter2 = new RealNewsFeedRecyclerAdapter(getContext(), realnewsList, R.layout.main_feed_real_news_item);
        realnewsRecyclerView.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();

    }

    private void initRealNewsData() { // 뉴스 데이터 설정
        realnewsList = new ArrayList<>();
        for(int i=0; i<10; i++)
            realnewsList.add(new RealNewsFeedRecyclerModel(newsData.newstitle[i]+""));
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