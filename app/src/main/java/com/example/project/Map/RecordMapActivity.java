package com.example.project.Map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.project.R;
import com.example.project.Ranking.UserInfo;
import com.example.project.Weather.GpsTrackerService;
import com.example.project.Weather.Weather;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RequiresApi(api = Build.VERSION_CODES.P)
public class RecordMapActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, MapViewFragment.OnConnectListener {


    // TAG
    private static final String TAG = "RecordMapActivity";
    private static final String recordTag = "RecordTAG";
    private static final String TAG_JSON = "pacerfit";
    private static final String TAG_PATH = "gpxPath";
    private static final String TAG_SIGUN = "sigun";
    private static final String TAG_NAME = "crsKorNm";
    private static final String TAG_SUMMRAY = "crsSummary";
    private static final String TAG_TIME = "crsTotlRqrmHour";
    private static final String TAG_LEVEL = "crsLevel";
    private static final String TAG_DIST = "crsDstnc";
    private static final String TAG_HASH = "tag";
    private static final String TAG_CRSIDX = "crsIdx";
    private static final String TAG_SLOPE = "slope_crs";
    private static final String TAG_LEVELCRS = "level_crs";
    private static final String TAG_HASHCRS = "hash_crs";

    // 네이버 맵, 맵 관련 변수
    private MapView mapView;
    private NaverMap naverMap;

    // 맵 위치
    private FusedLocationSource locationSource;

    // 유저 위치 리스트
    public static List<LatLng> userLocationList;
    // 폴리라인
    public PathOverlay userPolyline;
    public static ArrayList<Double> userSpeedList;

    // 백그라운드 체크
    private boolean isBackground = false;
    // 시작지점 체크
    private boolean startPoint = false;
    // 거리 측정 체크
    private boolean isRecord = false;

    // 코스 선택 체크
    private boolean isSelectCrs = false;
    // 코스 맵에 표시할지 체크
    private boolean isDisplayCrs = false;
    private boolean isRecommend = false;

    String slope_crs;
    String level_crs;
    String hash_crs;
    CourseData slopeCourse;
    CourseData levelCourse;
    CourseData hashCourse;
    // 서비스 처음 시작 체크
    private boolean isFirstStart = false;

    // Fragment
    private MapViewFragment mapViewFrag;
    private RecordFragment recordFrag;

    // FloatingActionButton
    private FloatingActionButton toRecordFab;
    private FloatingActionButton toMapFab;

    // TextView
    private TextView time_tv;
    private TextView dist_tv;
    private TextView cal_tv;
    private TextView crsName;
    private TextView crsHashTag;
    private TextView crsHour;
    private TextView crsDist;
    private TextView crsSummary;
    private TextView crsLevel;

    // 거리 변수
    private double distance = 0.0;
    // 칼로리 변수
    private double calories = 0.0;
    // 속도 변수
    private double avgSpeed = 0.0;
    // 시간 변수
    private int time = -1;
    private int exeTime = -1;

    //
    private long ratingStartTime = 0L;
    private long ratingEndTime = 0L;
    private ArrayList<String> ratingCrsList;


    // Service bind
    private LocationService mService;

    // 몸무게
    private double userKg;

    // 서비스 <-> 액티비티 통신
    private IntentFilter filter;
    private MyBroadcast br;

    // weather
    private ArrayList<HashMap<String, String>> pathArrayList;
    private GpsTrackerService gpsTracker;
    private String pathJsonString;
    private String pathJsonString2;
    private String location = "";
    private String address = ""; // x,y는 격자x,y좌표

    // 코스 정보 리스트
    private ArrayList<CourseData> courseDataList;
    private ArrayList<CourseData> recommendList;
    private ArrayList<String> crsNameList;
    private ArrayList<String> crsSummaryList;
    private ArrayList<String> crsTimeList;
    private ArrayList<String> crsLevelList;
    private ArrayList<String> crsDistList;
    private ArrayList<String> crsHashTagList;
    private ArrayList<String> crsIdxList;

    // 측정 관련 버튼
    private Button startBtn;
    private Button reselectBtn;
    private Button stopBtn;
    private Button recommendBtn;
    private ImageButton helpBtn;

    // 바텀 시트 레이아웃
    private SlidingUpPanelLayout mLayout;

    // 마커 listener
    private InfoWindow infoWindow;

    // 선택 코스 이름
    private String selectedCrsName = null;
    // 선택 코스 path
    private PathOverlay selectedCrs;

    // 서비스 <- 액티비티 위치 리스트 반환
    public static List<LatLng> getList() {
        return userLocationList;
    }

    // 서비스 -> 액티비티 위치 리스트 추가
    public static void addList(LatLng latLng) {
        userLocationList.add(new LatLng(latLng.latitude, latLng.longitude));
    }

    // 서비스와 binder 연결
    ServiceConnection sconn = new ServiceConnection() {
        @Override //서비스가 실행될 때 호출
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationService.MyBinder myBinder = (LocationService.MyBinder) service;
            mService = myBinder.getService();
            Log.e(TAG, "onServiceConnected()");
        }

        @Override //서비스가 종료될 때 호출
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            Log.e(TAG, "onServiceDisconnected()");
        }
    };

    // fragment <-> activity 연결 후 mapView 변수 연결
    @Override
    public void onConnect(MapView mapView) {
        if (mapView != null) {
            this.mapView = mapView;
            this.mapView.getMapAsync(this);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_record_activity);

        // 맵 화면
        if (mapViewFrag == null) {
            mapViewFrag = new MapViewFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.mainFrame, mapViewFrag).commit();
        }

        // 위치 변수 생성
        locationSource = new FusedLocationSource(this, Constants.LOCATION_PERMISSION_REQUEST_CODE);

        // FloatingActionButton
        toRecordFab = findViewById(R.id.toRecordFab);
        toMapFab = findViewById(R.id.toMapFab);

        // Button
        startBtn = findViewById(R.id.start_dist_btn);
        stopBtn = findViewById(R.id.stop_dist_btn);
        reselectBtn = findViewById(R.id.reselect_btn);
        recommendBtn = findViewById(R.id.recommend_btn);
        helpBtn = findViewById(R.id.dist_help_btn);

        // 바텀 시트
        crsName = findViewById(R.id.crs_name);
        crsHashTag = findViewById(R.id.crs_hashtag);
        crsDist = findViewById(R.id.crs_dist);
        crsHour = findViewById(R.id.crs_hour);
        crsSummary = findViewById(R.id.crs_summary);
        crsLevel = findViewById(R.id.crs_level);

        mLayout = findViewById(R.id.sliding_layout);

        // onClick
        toRecordFab.setOnClickListener(this);
        toMapFab.setOnClickListener(this);
        startBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
        reselectBtn.setOnClickListener(this);
        recommendBtn.setOnClickListener(this);
        helpBtn.setOnClickListener(this);

        // 서비스 <-> 액티비티간의 통신 등록
        br = new MyBroadcast();
        filter = new IntentFilter();
        filter.addAction("etc");
        registerReceiver(br, filter);

        // 기록 화면 전환
        recordFrag = new RecordFragment();
        getSupportFragmentManager().beginTransaction().
                add(R.id.mainFrame, recordFrag, recordTag).
                hide(recordFrag).commit();

        // 유저의 위치, 폴리라인 리스트 생성 및 설정
        userLocationList = new ArrayList<>();
        userPolyline = new PathOverlay();
        userPolyline.setWidth(10);
        userPolyline.setColor(Color.RED);

        // 코스들 폴리라인, 마커 리스트 생성
        userSpeedList = new ArrayList<>();
        infoWindow = new InfoWindow();

        // 코스 리스트들 생성
        pathArrayList = new ArrayList<>();
        crsNameList = new ArrayList<>();
        crsSummaryList = new ArrayList<>();
        crsTimeList = new ArrayList<>();
        crsLevelList = new ArrayList<>();
        crsDistList = new ArrayList<>();
        crsHashTagList = new ArrayList<>();
        crsIdxList = new ArrayList<>();

        ratingCrsList = new ArrayList<>();
        courseDataList = new ArrayList<>();


        userKg = Double.parseDouble(getIntent().getStringExtra("userWeight"));
        Weather weatherMethod = new Weather();
        gpsTracker = new GpsTrackerService(this);
        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();

        address = weatherMethod.getCurrentAddress(this, latitude, longitude);
        String[] local = address.split(" ");  //주소를 대한민국, 서울특별시, xx구 ... 로 나눔
        local[1] = AreaChange(local[1]);  //서울특별시, 경기도등의 이름을 db에 맞게 수정
        location = (local[1] + " " + local[2]);
        Log.d(TAG, "location - " + location);

        // 초기 bottom_sheet 숨김
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
//         코스 리스트들 생성
        pathArrayList = new ArrayList<>();
        crsNameList = new ArrayList<>();
        crsSummaryList = new ArrayList<>();
        crsTimeList = new ArrayList<>();
        crsLevelList = new ArrayList<>();
        crsDistList = new ArrayList<>();
        crsHashTagList = new ArrayList<>();

        // 절전 모드 해제
        onRequestPermissionBattery();
        // 백그라운드 위치 항상 허용
        onRequestPermissionBackgroundLocation();
        GetGpxPathData task = new GetGpxPathData();
        task.execute("http://pacerfit.dothome.co.kr/getPathWithArea.php");


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        GetCFData task1 = new GetCFData();
        task1.execute("http://pacerfit.dothome.co.kr/slope_one_ex.php");



    }

    private void onRequestPermissionBattery() {
        // 백그라운드에서도 서비스가 돌아갈 수 있도록 함
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        String packageName = getPackageName();
        if (pm.isIgnoringBatteryOptimizations(packageName)) {

        } else {    // 메모리 최적화가 되어 있다면, 풀기 위해 설정 화면 띄움.
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + packageName));
            startActivity(intent);
        }
    }


    // 맵이 생성되면 불려지는 callback 함수
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        // 네이버 맵 설정
        this.naverMap = naverMap;

        if (isRecord) {
            // 트래킹 모드 follow
            naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
        } else {
            // 트래킹 중지
            naverMap.setLocationTrackingMode(LocationTrackingMode.NoFollow);
        }


        // 위치
        naverMap.setLocationSource(locationSource);

        // 초기 위치 표시
        naverMap.setLocationTrackingMode(LocationTrackingMode.NoFollow);
        // 지도를 클릭하면 정보 창을 닫기
        naverMap.setOnMapClickListener((coord, point) -> {
            selectedCrsName = null;
            if (!isSelectCrs) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                dismissCrs();
            }
            infoWindow.close();
        });

        // 정보 창의 어댑터 지정
        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(getApplicationContext()) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                // 정보 창이 열린 마커의 tag를 텍스트로 노출하도록 반환
                return (CharSequence) infoWindow.getMarker().getTag();
            }
        });

        // 위치 변경 리스너
        naverMap.addOnLocationChangeListener(location -> {
            // 액티비티가 포그라운드 상태이고 운동이 시작된 상태이면
            if (isRecord && !isBackground && location.getAccuracy() < 11.0) {
                // 속도가 있으면 평균 속도 구하기
                if (location.hasSpeed())
                    avgSpeed = location.getSpeed() * 3600 / 1000;

                if (avgSpeed < 0.5) {
                    exeTime--;
                }

                // 리스트 추가
                addList(new LatLng(location.getLatitude(), location.getLongitude()));

                // 폴리라인 맵에 갱신
                if (userLocationList.size() > 2) {
                    userPolyline.setCoords(userLocationList);
                    userPolyline.setMap(naverMap);
                }
            }
        });
        Log.d(TAG, "onMapReady");
    }


    // 권한 요청
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

    // 서비스로부터 값을 받아와서 UI에 적용
    public class MyBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (intent.getAction() != null) {
                    if (intent.getAction().equals("etc")) {
                        exeTime++;
                        // 출발 지점 표시
                        if (!startPoint && !userLocationList.isEmpty()) {
                            startPoint = true;
                            makeMarker("출발지점", userLocationList.get(0).latitude, userLocationList.get(0).longitude);
                        }

                        dist_tv = findViewById(R.id.dist_km);
                        time_tv = findViewById(R.id.dist_time);
                        cal_tv = findViewById(R.id.dist_cal);

                        time = intent.getIntExtra("timer", 0);
                        distance = intent.getDoubleExtra("distance", 0.0);
                        calories = getCalories();
                        if (time_tv != null)
                            time_tv.setText(RecordUtility.formattedRecordTime(time));
                        if (dist_tv != null)
                            dist_tv.setText(RecordUtility.formattedRecordDist(distance));
                        if (cal_tv != null)
                            cal_tv.setText(RecordUtility.formattedRecordCal(calories));
                        Log.d("BroadCast", "Received BroadCast " + time + " " + distance);
                    }
                }
            }
        }
    }

    public void SaveMyRecordDB() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("========================" + response);
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) { // 만보기 값 저장완료
                        System.out.println("성공");
                    } else { // 실패한경우 강제종료
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        KmRecordSaveRequest kmRecordSaveRequest = new KmRecordSaveRequest(UserInfo.getInstance().getUserID() + "", dist_tv.getText().toString() + "", time + "", String.format("%.2f", calories) + "", responseListener);
        RequestQueue queue = Volley.newRequestQueue(RecordMapActivity.this);
        queue.add(kmRecordSaveRequest);
    }

    // 칼로리 계산
    private double getCalories() {
        return userKg * 2 * ((double) exeTime / 60) / 10;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mService != null)
            mService.isBackground = false;
        isBackground = false;
    }

    @Override
    public void onPause() {
        super.onPause();

        isBackground = true;
        if (mService != null)
            mService.isBackground = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 서비스 중지
        stopLocationService();
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        String test = intent.getStringExtra("OnNewIntent");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.toRecordFab:
                MapToRecord();
                break;
            case R.id.toMapFab:
                recordToMap();
                break;
            case R.id.reselect_btn:
                reselectCrs();
                break;
            case R.id.start_dist_btn:
                StartFab();
                break;
            case R.id.stop_dist_btn:
                SaveMyRecordDB(); //그만하기 누를시 db저장
                RecordSave();
                break;
            case R.id.recommend_btn:
                crsRecommend();
                break;

            case R.id.dist_help_btn:
                help();
            default:
                break;
        }
    }

    private void help() {
        DetailBottomFragment helpFragment = new DetailBottomFragment(getApplicationContext());
        helpFragment.show(getSupportFragmentManager(), helpFragment.getTag());

    }

    private void crsRecommend() {
        isRecommend = true;
        recommendList = new ArrayList<>();
        recordToMap();
        for (CourseData cd : courseDataList) {
            Log.d("course", cd.getId() + " " + slope_crs + " " + level_crs + " " + hash_crs);
            if (cd.getId().equals(slope_crs)) {
                slopeCourse = new CourseData(cd.getName(), cd.getSummary(), cd.getHour(), cd.getLevel(), cd.getDist(), cd.getHashTag(), cd.getId(), cd.getCoords());
                slopeCourse.createPathOverlay(false);
                slopeCourse.createMarker(false);
                slopeCourse.getPathOverlay().setColor(Color.YELLOW);
                slopeCourse.getPathOverlay().setPassedColor(Color.BLACK);
                slopeCourse.getMarker().setCaptionText("별점");
                slopeCourse.getMarker().setIcon(OverlayImage.fromResource(R.drawable.recommend_mark_img));
                recommendList.add(slopeCourse);
            }
            if (cd.getId().equals(level_crs)) {
                levelCourse = new CourseData(cd.getName(), cd.getSummary(), cd.getHour(), cd.getLevel(), cd.getDist(), cd.getHashTag(), cd.getId(), cd.getCoords());
                levelCourse.createPathOverlay(false);
                levelCourse.createMarker(false);
                levelCourse.getPathOverlay().setColor(Color.YELLOW);
                levelCourse.getPathOverlay().setPassedColor(Color.BLACK);
                levelCourse.getMarker().setCaptionText("난이도 추천");
                levelCourse.getMarker().setIcon(OverlayImage.fromResource(R.drawable.recommend_mark_img));
                recommendList.add(levelCourse);
            }
            if (cd.getId().equals(hash_crs)) {
                hashCourse = new CourseData(cd.getName(), cd.getSummary(), cd.getHour(), cd.getLevel(), cd.getDist(), cd.getHashTag(), cd.getId(), cd.getCoords());
                hashCourse.createPathOverlay(false);
                hashCourse.createMarker(false);
                hashCourse.getPathOverlay().setColor(Color.YELLOW);
                hashCourse.getPathOverlay().setPassedColor(Color.BLACK);
                hashCourse.getMarker().setCaptionText("해시태그 추천");
                hashCourse.getMarker().setIcon(OverlayImage.fromResource(R.drawable.recommend_mark_img));
                recommendList.add(hashCourse);
            }
            cd.dismissMarker();
            cd.dismissPath();

        }
        recommendBtn.setVisibility(View.INVISIBLE);
    }

    private void reselectCrs() {
        recordToMap();
        isSelectCrs = false;
        isRecord = false;
        isDisplayCrs = false;
        isRecommend = false;

        for (CourseData courseData : recommendList) {
            if (courseData != null) {
                courseData.dismissMarker();
            }
        }


        if (selectedCrsName != null) {
            ratingEndTime = System.currentTimeMillis();
        } else {
            ratingEndTime = 0L;
        }

        long seconds = TimeUnit.MILLISECONDS.toSeconds(ratingEndTime - ratingStartTime);

        if (seconds >= Constants.CHECK_RATING_CRS_TIME) {
            if (!ratingCrsList.contains(selectedCrsName)) {
                ratingCrsList.add(selectedCrsName);
            }
        }


        selectedCrsName = null;
        if (selectedCrs != null)
            selectedCrs.setMap(null);
        selectedCrs = null;

        startBtn.setVisibility(View.VISIBLE);
        stopBtn.setVisibility(View.INVISIBLE);
        reselectBtn.setVisibility(View.INVISIBLE);
        recommendBtn.setVisibility(View.VISIBLE);
        dismissCrs();


        Intent intent = new Intent(getApplicationContext(), LocationService.class);
        intent.setAction(Constants.ACTION_PAUSE_LOCATION_SERVICE);
        startService(intent);
    }

    // 시작 기능
    public void StartFab() {
        // 코스 선택 (코스 없으면 그대로 진행)
        selectCrs();
        isRecord = true;
        isDisplayCrs = true;
        // 권한 체크 후 서비스 시작
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RecordMapActivity.this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, Constants.REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            startLocationService();
        }
        toMapFab.show();
        recordToMap();
        startBtn.setVisibility(View.INVISIBLE);
        stopBtn.setVisibility(View.VISIBLE);
        reselectBtn.setVisibility(View.VISIBLE);
        recommendBtn.setVisibility(View.INVISIBLE);

        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        // 맵 <-> 기록
        MapToRecord();

        if (selectedCrsName != null) {
            ratingStartTime = System.currentTimeMillis();
        } else {
            ratingStartTime = 0L;
        }
        Log.d("start", String.valueOf(ratingStartTime));

        // 로그
        Log.d(TAG, "StartFab");
    }

    private void onRequestPermissionBackgroundLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) { //백그라운드 위치 권한 확인
        } else {
            //위치 권한 요청
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 0);
        }
    }

    // 저장 기능
    private void RecordSave() {
        if (selectedCrsName != null) {
            ratingEndTime = System.currentTimeMillis();
        } else {
            ratingEndTime = 0L;
        }

        long seconds = TimeUnit.MILLISECONDS.toSeconds(ratingEndTime - ratingStartTime);

        if (seconds >= Constants.CHECK_RATING_CRS_TIME) {
            if (!ratingCrsList.contains(selectedCrsName)) {
                ratingCrsList.add(selectedCrsName);
            }
        }

        // 서비스 종료
        if (mService != null)
            mService.destroyNotification();

        stopLocationService();

        Intent intent = new Intent(this, RecordActivity.class);
        intent.putExtra("cal", calories);
        intent.putExtra("distance", distance);
        intent.putExtra("time", time);
        intent.putExtra("userLocationList", (Serializable) userLocationList);
        intent.putExtra("ratingCrsList", (Serializable) ratingCrsList);
        startActivity(intent);
        finish();
    }

    // 기록 -> 맵
    private void recordToMap() {
        getSupportFragmentManager().beginTransaction().show(mapViewFrag).commit();
        getSupportFragmentManager().beginTransaction().hide(recordFrag).commit();
        toRecordFab.show();
        toMapFab.hide();
    }

    // 맵 -> 기록
    private void MapToRecord() {
        // recordFragmet를 보이게 하고 mapFragment를 숨김
        getSupportFragmentManager().beginTransaction().hide(mapViewFrag).commit();
        getSupportFragmentManager().beginTransaction().show(recordFrag).commit();
        toRecordFab.hide();
        toMapFab.show();
    }

    // 서비스 실행 여부 체크
    public boolean isLocationServiceRunning(String serviceName) {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo runServiceInfo : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(runServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    // 서비스 시작
    private void startLocationService() {
        Intent intent = new Intent(getApplicationContext(), LocationService.class);
        intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
        startService(intent);

        if (isFirstStart) {
            bindService(new Intent(RecordMapActivity.this, LocationService.class), sconn, BIND_AUTO_CREATE);
        }
    }

    // 서비스 종료
    private void stopLocationService() {
        if (isLocationServiceRunning("com.example.project.Map.LocationService")) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
            stopService(intent);
            if (mService != null)
                unbindService(sconn);
            if (br != null)
                unregisterReceiver(br);
        }
    }

    // 현재 위치에 마커를 생성하는 함수
    private void makeMarker(String tag, double lat, double lng) {
        Marker marker = new Marker();
        marker.setPosition(new LatLng(lat, lng));
        marker.setIcon(OverlayImage.fromResource(R.drawable.marker_icon));
        marker.setCaptionText(tag);
        marker.setWidth(100);
        marker.setHeight(100);
        marker.setMap(naverMap);
        Log.d(TAG, "makeMarker " + tag + " " + lat + ", " + lng);
    }


    private class GetGpxPathData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPostExecute(String result) { //doInBackground에서 return한 값을 받음
            super.onPostExecute(result);
            //progressDialog.dismiss();
            Log.d(TAG, "response  - " + result);
            if (result == null) {
                Log.d(TAG, errorString);
            } else {
                pathJsonString = result;
                showResult();
                new Thread() {
                    public void run() {
                        for (int i = 0; i < pathArrayList.size(); i++) {
                            String gpxpt = getData(i);
                            Bundle bun = new Bundle();
                            bun.putString("gpxpt", gpxpt);
                            bun.putString("name", crsNameList.get(i));
                            bun.putString("summary", crsSummaryList.get(i));
                            bun.putString("hour", crsTimeList.get(i));
                            bun.putString("level", crsLevelList.get(i));
                            bun.putString("dist", crsDistList.get(i));
                            bun.putString("tag", crsHashTagList.get(i));
                            bun.putString("crsIdx", crsIdxList.get(i));
                            Message msg = handler.obtainMessage();
                            msg.setData(bun);
                            handler.sendMessage(msg);

                        }
                    }
                }.start();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            try {
                String selectLocation = "location=" + location;
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(3000);
                httpURLConnection.setConnectTimeout(3000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(selectLocation.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
                //어플에서 데이터 전송

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }//연결상태 확인

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString().trim();
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();
                return null;
            }
        }
    }

    private void showResult() {
        try {
            JSONObject jsonObject = new JSONObject(pathJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                System.out.println(jsonArray.length() + "\n");
                System.out.println(i);


                // 코스 정보 리스트에 저장
                String gpxPath = item.getString(TAG_PATH);
                String sigun = item.getString(TAG_SIGUN);
                String name = item.getString(TAG_NAME);
                String summary = item.getString(TAG_SUMMRAY);
                String time = item.getString(TAG_TIME);
                String level = item.getString(TAG_LEVEL);
                String dist = item.getString(TAG_DIST);
                String tag = item.getString(TAG_HASH);
                String crsIdx = item.getString(TAG_CRSIDX);
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put(TAG_PATH, gpxPath);
                hashMap.put(TAG_SIGUN, sigun);

                pathArrayList.add(hashMap);
                crsNameList.add(name);
                crsSummaryList.add(summary);
                crsTimeList.add(time);
                crsLevelList.add(level);
                crsDistList.add(dist);
                crsHashTagList.add(tag);
                crsIdxList.add(crsIdx);

            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }


    private String getData(int index) {
        String gpxpt = "";
        URL url = null;
        HttpURLConnection http = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        HashMap<String, String> hashMap3 = pathArrayList.get(index);
        Log.d("getData", hashMap3.get(TAG_PATH));

        try {
            url = new URL(hashMap3.get(TAG_PATH));
            http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(3 * 1000);
            http.setReadTimeout(3 * 1000);
            isr = new InputStreamReader(http.getInputStream());
            br = new BufferedReader(isr);
            String str = null;
            while ((str = br.readLine()) != null) {
                if (str.contains("trkpt")) {
                    Pattern pattern = Pattern.compile("[\"](.*?)[\"]");
                    Matcher matcher = pattern.matcher(str);
                    while (matcher.find()) {  // 일치하는 게 있다면
                        gpxpt += matcher.group(1) + " ";
                        if (matcher.group(1) == null)
                            break;
                    }
                }
            }
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        } finally {
            if (http != null) {
                try {
                    http.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return gpxpt;
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bun = msg.getData();
            String gpxpt = bun.getString("gpxpt");
            String[] latLon = gpxpt.split(" ");
            String name = bun.getString("name");
            String summary = bun.getString("summary");
            String hour = bun.getString("hour");
            String level = bun.getString("level");
            String dist = bun.getString("dist");
            String tag = bun.getString("tag");
            String id = bun.getString("crsIdx");
            ArrayList<LatLng> coords = new ArrayList<>();
            for (int i = 12; i < latLon.length; i += 2) {
                try {

                    coords.add(new LatLng(Double.valueOf(latLon[i]), Double.valueOf(latLon[i + 1])));
                } catch (NumberFormatException e) {
                    // 문자열을 숫자로 인식할때 예외처리
                    // java.lang.NumberFormatException: For input string: "http://www.topografix.com/GPX/1/1" 에러
                } catch (Exception e) {
                    //다른 에러 예외처리
                }
            }

            if (coords.size() > 2) {
                CourseData courseData = new CourseData(name, summary, hour, level, dist, tag, id, coords);
                if (!isDisplayCrs) {
                    courseData.createPathOverlay(false);
                    courseData.createMarker(false);
                } else {
                    courseData.createPathOverlay(true);
                    courseData.createMarker(true);
                }
                courseDataList.add(courseData);
            }

        }
    };

    public void setBottomListData(String name, String summary, String level, String hour, String dist, String tag) {
        crsName.setText(name);
        switch (level) {
            case "1":
                crsLevel.setText("난이도 - 쉬움");
                crsLevel.setTextColor(ContextCompat.getColor(this, R.color.skyblue_project));
                crsHour.setTextColor(ContextCompat.getColor(this, R.color.skyblue_project));
                crsDist.setTextColor(ContextCompat.getColor(this, R.color.skyblue_project));
                break;
            case "2":
                crsLevel.setText("난이도 - 보통");
                crsLevel.setTextColor(Color.GREEN);
                crsHour.setTextColor(Color.GREEN);
                crsDist.setTextColor(Color.GREEN);
                break;
            case "3":
                crsLevel.setText("난이도 - 어려움");
                crsLevel.setTextColor(Color.RED);
                crsHour.setTextColor(Color.RED);
                crsDist.setTextColor(Color.RED);
                break;
        }
        crsSummary.setText(summary);
        crsHour.setText(RecordUtility.formattedCrsHour(hour));
        crsDist.setText(RecordUtility.formattedCrsDist(dist));
        crsHashTag.setText(tag);
    }

    public void clickCrs(String crsName) {
        isDisplayCrs = true;
        // 클릭 코스 제외 폴리라인, 마커 맵에서 제거
        if (!isRecommend) {
            for (CourseData cd : courseDataList) {
                if (cd.getName().equals(crsName)) {
                    cd.getPathOverlay().setColor(cd.getPathOverlay().getColor());
                } else {
                    cd.dismissPath();
                }
                cd.dismissMarker();
            }
        } else {
            for (CourseData courseData : recommendList) {
                if (courseData != null) {
                    if (courseData.getName().equals(crsName)) {
                    } else
                        courseData.dismissPath();
                    courseData.dismissMarker();
                }
            }
        }
    }

    // 맵을 클릭하거나, 바텀 시트 사라지면 코스 폴리라인, 마커 맵에 재등록
    public void dismissCrs() {
        isDisplayCrs = false;
        if (!isRecommend) {
            for (CourseData cd : courseDataList) {
                cd.attachPath();
                cd.setCourseColor(cd.getLevel());
                cd.attachMarker();
            }
        } else {
            for (CourseData courseData : recommendList) {
                if (courseData != null) {
                    courseData.attachPath();
                    courseData.attachMarker();
                }
            }
        }
        CameraUpdate cameraUpdate = CameraUpdate.zoomTo(13).animate(CameraAnimation.Easing);
        naverMap.moveCamera(cameraUpdate);
    }

    // 코스 선택시 폴리라인 -> 폴리패스로 변경 나머지 폴리라인, 마커 제거
    public void selectCrs() {
        isSelectCrs = true;
        isDisplayCrs = true;
        for (CourseData cd : courseDataList) {
            if (cd.getName().equals(selectedCrsName)) {
                selectedCrs = new PathOverlay(cd.getPathOverlay().getCoords());
                selectedCrs.setTag(selectedCrsName);
                selectedCrs.setWidth(20);
                selectedCrs.setColor(ContextCompat.getColor(this, R.color.purple_project));
                selectedCrs.setMap(naverMap);
                CameraUpdate cameraUpdate = CameraUpdate.fitBounds(cd.getPathOverlay().getBounds(), 100, 100, 100, 200).animate(CameraAnimation.Easing);
                naverMap.moveCamera(cameraUpdate);
            }
            cd.dismissPath();
            cd.dismissMarker();
        }
        for (CourseData courseData : recommendList) {
            if (courseData != null) {
                courseData.dismissMarker();
                courseData.dismissPath();
            }
        }

    }

    public String AreaChange(String area) {
        String areaChanged = null;
        if (area.contains("서울")) areaChanged = "서울";
        else if (area.contains("강원")) areaChanged = "강원";
        else if (area.contains("경기")) areaChanged = "경기";
        else if (area.contains("대구")) areaChanged = "대구";
        else if (area.contains("대전")) areaChanged = "대전";
        else if (area.contains("전라북도")) areaChanged = "전북";
        else if (area.contains("전라남도")) areaChanged = "전남";
        else if (area.contains("부산")) areaChanged = "부산";
        else if (area.contains("충청북도")) areaChanged = "충북";
        else if (area.contains("충청남도")) areaChanged = "충남";
        else if (area.contains("울산")) areaChanged = "울산";
        else if (area.contains("광주")) areaChanged = "광주";
        else if (area.contains("인천")) areaChanged = "인천";
        else if (area.contains("세종")) areaChanged = "세종";
        else if (area.contains("경상북도")) areaChanged = "경북";
        else if (area.contains("경상남도")) areaChanged = "경남";
        else if (area.contains("제주")) areaChanged = "제주";

        return areaChanged;
    }


    private class GetCFData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPostExecute(String result) { //doInBackground에서 return한 값을 받음
            super.onPostExecute(result);
            //progressDialog.dismiss();
            Log.d(TAG, "response  - " + result);
            if (result == null) {
                Log.d(TAG, errorString);
            } else {
                pathJsonString2 = result;
                showResult2();

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];

            try {
                String userID = "userID=" + UserInfo.getInstance().getUserID();
                String selectLocation = "location=" + location;

                StringBuffer stParams = new StringBuffer();
                stParams.append(userID);
                stParams.append("&");
                stParams.append(selectLocation);

                String strParams = stParams.toString();
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(3000);
                httpURLConnection.setConnectTimeout(3000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(strParams.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
                //어플에서 데이터 전송

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }//연결상태 확인

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString().trim();
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();
                return null;
            }
        }
    }

    private void showResult2() {
        try {
            JSONObject jsonObject = new JSONObject(pathJsonString2);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                System.out.println(jsonArray.length() + "\n");
                System.out.println(i);


                // 코스 정보 리스트에 저장
                slope_crs = item.getString(TAG_SLOPE); //슬로프원 알고리즘 추천코스(별점)
                level_crs = item.getString(TAG_LEVELCRS); //난이도를 이용한 추천코스
                hash_crs = item.getString(TAG_HASHCRS); //해시태그를 이용한 추천코스

                System.out.println(slope_crs + "========" + level_crs + "=========" + hash_crs + "==============");

            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    class CourseData {
        private String name;
        private String summary;
        private String hour;
        private String level;
        private String dist;
        private String hashTag;
        private String id;
        private ArrayList<LatLng> coords;
        private PathOverlay pathOverlay;
        private Marker marker;


        public CourseData(String name, String summary, String hour, String level, String dist, String hashTag, String id, ArrayList<LatLng> coords) {
            this.name = name;
            this.summary = summary;
            this.hour = hour;
            this.level = level;
            this.dist = dist;
            this.hashTag = hashTag;
            this.coords = coords;
            this.id = id;
        }

        public void createPathOverlay(boolean flag) {
            pathOverlay = new PathOverlay();
            pathOverlay.setWidth(10);
            pathOverlay.setCoords(coords);
            pathOverlay.setTag(name);
            setCourseColor(level);
            if (!flag)
                attachPath();
            else
                dismissPath();
        }

        public void createMarker(boolean flag) {
            marker = new Marker();
            marker.setPosition(new LatLng(coords.get(0).latitude, coords.get(0).longitude));
            marker.setCaptionText(name);
            marker.setWidth(100);
            marker.setHeight(100);
            switch (level) {
                case "1":
                    marker.setIcon(OverlayImage.fromResource(R.drawable.lv_1_mark_img));
                    break;
                case "2":
                    marker.setIcon(OverlayImage.fromResource(R.drawable.lv_2_mark_img));
                    break;
                case "3":
                    marker.setIcon(OverlayImage.fromResource(R.drawable.lv_3_mark_img));
                    break;
            }
            Overlay.OnClickListener listener = overlay -> {
                // 선택 코스 이름 변경
                selectedCrsName = name;
                if (marker.getInfoWindow() == null) {
                    // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                    infoWindow.open(marker);
                    clickCrs(name);
                } else {
                    infoWindow.close();
                }

                // 카메라를 gpx 경로로 이동
                CameraUpdate cameraUpdate = CameraUpdate.fitBounds(pathOverlay.getBounds(), 100, 100, 100, 200).animate(CameraAnimation.Easing);
                naverMap.moveCamera(cameraUpdate);

                // bottomsheet 보이게 하기
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                setBottomListData(name, summary, level, hour, dist, hashTag);

                return true;
            };
            marker.setOnClickListener(listener);
            if (!flag)
                attachMarker();
            else
                dismissMarker();
        }

        public void dismissPath() {
            pathOverlay.setMap(null);
        }

        public void attachPath() {
            pathOverlay.setMap(naverMap);
        }

        public void dismissMarker() {
            marker.setMap(null);
        }

        public void attachMarker() {
            marker.setMap(naverMap);
        }

        // 코스 난이도에 따른 색 변경
        public void setCourseColor(String level) {
            switch (level) {
                case "1":
                    pathOverlay.setColor(Color.BLUE);
                    break;
                case "2":
                    pathOverlay.setColor(Color.GREEN);
                    break;
                case "3":
                    pathOverlay.setColor(Color.RED);
                    break;
            }
        }

        public String getName() {
            return name;
        }

        public String getSummary() {
            return summary;
        }

        public String getHour() {
            return hour;
        }

        public String getLevel() {
            return level;
        }

        public String getDist() {
            return dist;
        }

        public String getHashTag() {
            return hashTag;
        }

        public ArrayList<LatLng> getCoords() {
            return coords;
        }

        public PathOverlay getPathOverlay() {
            return pathOverlay;
        }

        public Marker getMarker() {
            return marker;
        }

        public String getId() {
            return id;
        }
    }
}