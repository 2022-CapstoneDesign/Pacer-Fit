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
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.project.R;
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
import com.naver.maps.map.overlay.PolylineOverlay;
import com.naver.maps.map.util.FusedLocationSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RequiresApi(api = Build.VERSION_CODES.P)
public class RecordMapActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, MapViewFragment.OnConnectListener {

    // color
    // gpx 구분하기 위해 여러 색으로 뒀는데, 기준을 주면 좋을 것 같음. ex 코스 레벨
    int[] ColorEnum = {Color.RED, Color.BLUE, Color.BLACK, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.WHITE};
    int colorIdx = 0;

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

    // 네이버 맵, 맵 관련 변수
    private MapView mapView;
    private NaverMap naverMap;

    // 맵 위치
    private FusedLocationSource locationSource;

    // 폴리라인
    private PolylineOverlay curUserPolyline;
    // 유저 위치 리스트
    public static List<LatLng> latLngList;

    // 시작지점 체크
    private boolean startPoint = false;
    // 백그라운드 체크
    private boolean isBackground = false;
    // 거리 측정 확인 변수
    private boolean isRecord = false;

    private boolean selectCrs = false;

    // Fragment
    private MapViewFragment mapViewFrag;
    private RecordFragment recordFrag;
    private CrsInfoBottomFragment crsInfoBottomFragment;

    // FloatingActionButton
    private FloatingActionButton recordStartFab;
    private FloatingActionButton recordPauseFab;
    private FloatingActionButton recordResumeFab;
    private FloatingActionButton recordSaveFab;
    private FloatingActionButton toRecordFab;
    private FloatingActionButton toMapFab;

    // TextView
    private TextView time_tv;
    private TextView dist_tv;
    private TextView cal_tv;

    // 거리 변수
    private double distance = 0.0;
    // 칼로리 변수
    private double calories = 0.0;
    // 속도 변수
    private double avgSpeed = 0.0;
    // 시간 변수
    private int time = -1;

    // Service bind
    private LocationService mService;

    // 몸무게
    private double userKg;
    // 칼로리 계산 변수 (산소)
    private double MET = 0.0;

    // 서비스 <-> 액티비티 통신
    private MyBroadcast br;
    private IntentFilter filter;

    // weather
    private GpsTrackerService gpsTracker;
    private String address = ""; // x,y는 격자x,y좌표
    private String location = "";
    private String pathJsonString;
    private ArrayList<HashMap<String, String>> pathArrayList;

    // 코스 정보 리스트
    private ArrayList<String> crsNameList;
    private ArrayList<String> crsSummaryList;
    private ArrayList<String> crsTimeList;
    private ArrayList<String> crsLevelList;
    private ArrayList<String> crsDistList;
    private ArrayList<PolylineOverlay> crsPolylineOverlays;
    private ArrayList<Marker> crsMarkers;

    // 마커 listener
    private InfoWindow infoWindow;

    // 서비스 <- 액티비티 위치 리스트 반환
    public static List<LatLng> getList() {
        return latLngList;
    }

    // 서비스 -> 액티비티 위치 리스트 추가
    public static void addList(LatLng latLng) {
        latLngList.add(new LatLng(latLng.latitude, latLng.longitude));
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
        recordStartFab = findViewById(R.id.recordStartFab);
        recordPauseFab = findViewById(R.id.recordPauseFab);
        recordResumeFab = findViewById(R.id.recordResumeFab);
        recordSaveFab = findViewById(R.id.recordSaveFab);
        toRecordFab = findViewById(R.id.toRecordFab);
        toMapFab = findViewById(R.id.toMapFab);

        // onClick
        recordStartFab.setOnClickListener(this);
        recordPauseFab.setOnClickListener(this);
        recordResumeFab.setOnClickListener(this);
        recordSaveFab.setOnClickListener(this);
        toRecordFab.setOnClickListener(this);
        toMapFab.setOnClickListener(this);

        // 서비스 <-> 액티비티간의 통신 등록
        br = new MyBroadcast();
        filter = new IntentFilter();
        filter.addAction("etc");
        registerReceiver(br, filter);


        // 유저의 위치, 폴리라인 리스트 생성 및 설정
        latLngList = new ArrayList<>();
        curUserPolyline = new PolylineOverlay();
        curUserPolyline.setWidth(10);
        curUserPolyline.setColor(Color.RED);

        // 코스들 폴리라인, 마커 리스트 생성
        crsPolylineOverlays = new ArrayList<>();
        crsMarkers = new ArrayList<>();
        infoWindow = new InfoWindow();


        // 유저 몸무게 받아오기 (db에서 받아오기)
        userKg = 50.0;

        Weather weatherMethod = new Weather();
        gpsTracker = new GpsTrackerService(this);
        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();

        address = weatherMethod.getCurrentAddress(this, latitude, longitude);
        String[] local = address.split(" ");  //주소를 대한민국, 서울특별시, xx구 ... 로 나눔
        local[1] = AreaChange(local[1]);  //서울특별시, 경기도등의 이름을 db에 맞게 수정
        location = (local[1] + " " + local[2]);
        Log.d(TAG, "location - " + location);

        // 코스 리스트들 생성
        pathArrayList = new ArrayList<>();
        crsNameList = new ArrayList<>();
        crsSummaryList = new ArrayList<>();
        crsTimeList = new ArrayList<>();
        crsLevelList = new ArrayList<>();
        crsDistList = new ArrayList<>();

        GetGpxPathData task = new GetGpxPathData();
        task.execute("http://pacerfit.dothome.co.kr/getPathWithArea.php");
    }

    // 맵이 생성되면 불려지는 callback 함수
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        // 네이버 맵 설정
        this.naverMap = naverMap;

        // 위치
        naverMap.setLocationSource(locationSource);

        // 지도를 클릭하면 정보 창을 닫기
        naverMap.setOnMapClickListener((coord, point) -> {
            colorInit();
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

        // 트래킹 모드 (추적 안함)
        naverMap.setLocationTrackingMode(LocationTrackingMode.NoFollow);

        // 위치 변경 리스너
        naverMap.addOnLocationChangeListener(location -> {
            // 액티비티가 포그라운드 상태이고 운동이 시작된 상태이면
            if (isRecord && !isBackground && location.getAccuracy() < 8.5) {
                // 속도가 있으면 평균 속도 구하기
                if (location.hasSpeed())
                    avgSpeed = location.getSpeed() * 3600 / 1000;
                Toast.makeText(getApplicationContext(), "정확도: " + location.getAccuracy() + "속도: " + avgSpeed, Toast.LENGTH_SHORT).show();

                // 리스트 추가
                addList(new LatLng(location.getLatitude(), location.getLongitude()));

                // 칼로리 계산 변수
                if (avgSpeed >= 3.5) {
                    MET = 3.8;
                } else if (avgSpeed <= 3.5 && avgSpeed >= 1.0) {
                    MET = 3.0;
                } else {
                    MET = 0.0;
                }

                // 폴리라인 맵에 갱신
                if (latLngList.size() > 2) {
                    curUserPolyline.setCoords(latLngList);
                    curUserPolyline.setMap(naverMap);
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
                        // 출발 지점 표시
                        if (!startPoint && !latLngList.isEmpty()) {
                            startPoint = true;
                            makeMarker("출발지점", latLngList.get(0).latitude, latLngList.get(0).longitude);
                        }

                        dist_tv = findViewById(R.id.distanceText);
                        time_tv = findViewById(R.id.pedo_time);
                        cal_tv = findViewById(R.id.calText);

                        time = intent.getIntExtra("timer", 0);
                        distance = intent.getDoubleExtra("distance", 0.0);
                        calories += getCalories(MET);
                        int sec = time % 60;
                        int min = time / 60 % 60;
                        int hour = time / 3600;
                        if (time_tv != null)
                            time_tv.setText(hour + "H " + min + "M " + sec + "S");
                        if (dist_tv != null)
                            dist_tv.setText(Double.toString(Math.round(distance * 100) / 100.0));
                        if (cal_tv != null)
                            cal_tv.setText(Math.round(calories * 100) / 100.0 + "kcal");
                        Log.d("BroadCast", "Received BroadCast " + time + " " + distance);
                    }
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mService != null)
            mService.isBackground = false;
        isBackground = false;

        Log.d(TAG, "onResume " + isBackground);
    }

    @Override
    public void onPause() {
        super.onPause();
        isBackground = true;

        if (mService != null)
            mService.isBackground = true;
        Log.d(TAG, "onPause " + isBackground);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 트래킹 중지
        //stopTracking(naverMap);

        // 서비스 중지
        stopLocationService();
        Toast.makeText(this.getApplicationContext(), "서비스 종료", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.recordStartFab:
                StartFab();
                break;
            case R.id.recordPauseFab:
                PauseFab();
                break;
            case R.id.recordResumeFab:
                ResumeFab();
                break;
            case R.id.toRecordFab:
                MapToRecord();
                break;
            case R.id.toMapFab:
                recordToMap();
                break;
            case R.id.recordSaveFab:
                RecordSave();
                break;
            default:
                break;
        }
    }

    // 시작 기능
    public void StartFab() {
        isRecord = true;

        // 권한 체크 후 서비스 시작
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RecordMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            startLocationService();
        }

        // 버튼 숨김
        recordStartFab.hide();

        // 버튼 보임
        recordPauseFab.show();
        toMapFab.show();

        // 기록 화면 전환
        recordFrag = new RecordFragment();
        getSupportFragmentManager().beginTransaction().
                add(R.id.mainFrame, recordFrag, recordTag).
                hide(recordFrag).commit();

        // 맵 <-> 기록
        MapToRecord();


        // 트래킹 모드 follow
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        // 로그
        Log.d(TAG, "StartFab");
    }

    // 정지 기능
    public void PauseFab() {
        isRecord = false;
        // 트래킹 중지
        naverMap.setLocationTrackingMode(LocationTrackingMode.NoFollow);

        // 서비스 타이머 정지
        mService.pauseTimer();

        // 버튼 숨김
        recordPauseFab.hide();

        // 버튼 보임
        recordResumeFab.show();
        recordSaveFab.show();

        // 로그
        Log.d(TAG, "PauseFab");
    }

    // 재개 기능
    public void ResumeFab() {
        isRecord = true;
        // 트래킹 시작
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        // 서비스 타이머 시작
        mService.startTimer();

        // 버튼 숨김
        recordResumeFab.hide();
        recordSaveFab.hide();

        //버튼 보임
        recordPauseFab.show();

        // 로그
        Log.d(TAG, "ResumeFab");
    }

    // 저장 기능
    private void RecordSave() {
        // 버튼 숨김
        recordResumeFab.hide();

        // 도착 마크 생성
        if (latLngList.size() > 1 && startPoint)
            makeMarker("도착 지점", latLngList.get(latLngList.size() - 1).latitude, latLngList.get(latLngList.size() - 1).longitude);
        else if (startPoint && latLngList.size() == 1)
            makeMarker("도착 지점", latLngList.get(0).latitude, latLngList.get(0).longitude);


        // 서비스 종료
        if (mService != null)
            mService.destroyNotification();
            stopLocationService();
        Toast.makeText(this.getApplicationContext(), "서비스 종료", Toast.LENGTH_SHORT).show();
    }

    // 기록 -> 맵
    private void recordToMap() {
        // mapFragment를 보이게 하고 recordFragmet를 숨김
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
    public boolean isLocationServiceRunning(String serviceName){
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo runServiceInfo : manager.getRunningServices(Integer.MAX_VALUE)){
            if(serviceName.equals(runServiceInfo.service.getClassName())){
                return true;
            }
        }
        return false;
    }


    // 서비스 시작
    private void startLocationService() {
        if (!isLocationServiceRunning("com.example.project.Map.LocationService")) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
            startService(intent);
            bindService(new Intent(RecordMapActivity.this, LocationService.class), sconn, BIND_AUTO_CREATE);
            Toast.makeText(this, "Location service started", Toast.LENGTH_SHORT).show();
        }
        //startTracking();
    }

    // 서비스 종료
    private void stopLocationService() {
        if (isLocationServiceRunning("com.example.project.Map.LocationService")) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
            stopService(intent);
            unbindService(sconn);
            unregisterReceiver(br);
            Toast.makeText(this, "Location service stopped", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Location service not exec", Toast.LENGTH_SHORT).show();
        }
    }

    // 현재 위치에 마커를 생성하는 함수
    public void makeMarker(String tag, double lat, double lng) {
        Marker marker = new Marker();
        marker.setPosition(new LatLng(lat, lng));
        marker.setIcon(OverlayImage.fromResource(R.drawable.marker_icon));
        marker.setCaptionText(tag);
        marker.setWidth(100);
        marker.setHeight(100);
        marker.setMap(naverMap);
        Log.d(TAG, "makeMarker " + tag + " " + lat + ", " + lng);
    }

    // 칼로리 계산
    private double getCalories(double met) {
        double cal;
        cal = met * 3.5 * userKg / 60 * 5 / 1000;
        return cal;
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
                            if (!selectCrs) {
                                String gpxpt = getData(i);
                                Bundle bun = new Bundle();
                                bun.putString("gpxpt", gpxpt);
                                bun.putInt("color", i);
                                bun.putString("name", crsNameList.get(i));
                                bun.putString("summary", crsSummaryList.get(i));
                                bun.putString("time", crsTimeList.get(i));
                                bun.putString("level", crsLevelList.get(i));
                                bun.putString("dist", crsDistList.get(i));
                                Message msg = handler.obtainMessage();
                                msg.setData(bun);
                                handler.sendMessage(msg);
                            }
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

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
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

                // 코스 정보 리스트에 저장
                String gpxPath = item.getString(TAG_PATH);
                String sigun = item.getString(TAG_SIGUN);
                String name = item.getString(TAG_NAME);
                String summary = item.getString(TAG_SUMMRAY);
                String time = item.getString(TAG_TIME);
                String level = item.getString(TAG_LEVEL);
                String dist = item.getString(TAG_DIST);

                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put(TAG_PATH, gpxPath);
                hashMap.put(TAG_SIGUN, sigun);

                pathArrayList.add(hashMap);
                crsNameList.add(name);
                crsSummaryList.add(summary);
                crsTimeList.add(time);
                crsLevelList.add(level);
                crsDistList.add(dist);

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
        int size = pathArrayList.size();


        HashMap<String, String> hashMap3 = pathArrayList.get(index);

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
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (Exception e) {
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                }
            }
        }

        System.out.println("gpxpt: " + gpxpt);
        return gpxpt;
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (!selectCrs) {
                Bundle bun = msg.getData();
                String gpxpt = bun.getString("gpxpt");
                String[] latLon = gpxpt.split(" ");
                String name = bun.getString("name");
                String summary = bun.getString("summary");
                String time = bun.getString("time");
                String level = bun.getString("level");
                String dist = bun.getString("dist");
                int color = bun.getInt("color");
                List<LatLng> COORDS = new ArrayList<>();
                for (int i = 0; i < latLon.length; i += 2) {
                    try {
                        if (latLon[i].substring(latLon[i].indexOf(".")).length() == 7
                                &&latLon[i+1].substring(latLon[i].indexOf(".")).length() == 7) continue;
                        COORDS.add(new LatLng(Double.valueOf(latLon[i]), Double.valueOf(latLon[i + 1])));
                    } catch (NumberFormatException e) {
                        // 문자열을 숫자로 인식할때 예외처리
                        // java.lang.NumberFormatException: For input string: "http://www.topografix.com/GPX/1/1" 에러
                    } catch (Exception e) {
                        //다른 에러 예외처리
                    }

                }
                if (COORDS.size() > 2) {
                    PolylineOverlay polylineOverlay1 = new PolylineOverlay();
                    polylineOverlay1.setWidth(10);
                    polylineOverlay1.setCoords(COORDS);
                    polylineOverlay1.setTag(name);
                    makeGPXMarker(name, summary, time, level, dist, COORDS.get(0).latitude, COORDS.get(0).longitude, color % ColorEnum.length, polylineOverlay1);
                    polylineOverlay1.setMap(naverMap);
                    crsPolylineOverlays.add(polylineOverlay1);
                }
            }

        }


    };

    public void colorInit(){

        if(!crsPolylineOverlays.isEmpty()){
            for(PolylineOverlay polylineOverlay: crsPolylineOverlays){
                polylineOverlay.setColor(Color.BLACK);
            }
        }

    }
    public void makeGPXMarker(String GPXName, String summary, String time, String level, String dist, double lat, double lng, int color, PolylineOverlay polylineOverlay1) {
        Marker marker = new Marker();
        marker.setPosition(new LatLng(lat, lng));
        marker.setCaptionText(GPXName);
        marker.setMap(naverMap);
        marker.setTag(GPXName);
        marker.setIconTintColor(ColorEnum[color]);
        PolylineOverlay polylineOverlay = polylineOverlay1;

        Overlay.OnClickListener listener = overlay -> {
            Toast.makeText(getApplicationContext(), "마커 " + overlay.getTag() + " 클릭됨",
                    Toast.LENGTH_SHORT).show();

            if (marker.getInfoWindow() == null) {
                // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                infoWindow.open(marker);
                polylineOverlay.setColor(Color.RED);

            } else {
                // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                infoWindow.close();
            }
            CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(lat, lng))
                    .animate(CameraAnimation.Easing);
            naverMap.moveCamera(cameraUpdate);

            crsInfoBottomFragment = new CrsInfoBottomFragment(getApplicationContext(), GPXName, summary, time, level, dist);
            crsInfoBottomFragment.show(getSupportFragmentManager(), crsInfoBottomFragment.getTag());

            return true;
        };
        marker.setOnClickListener(listener);
        crsMarkers.add(marker);
    }

    public void selectCrs(String crsName) {
        selectCrs = true;
        int curIndex = 0;
        for (PolylineOverlay polylineOverlay : crsPolylineOverlays) {
            if (polylineOverlay.getTag().equals(crsName)) {
                PathOverlay pathOverlay = new PathOverlay(polylineOverlay.getCoords());
                pathOverlay.setTag(crsName);
                pathOverlay.setWidth(20);
                pathOverlay.setColor(Color.RED);
                pathOverlay.setMap(naverMap);
                CameraUpdate cameraUpdate = CameraUpdate.fitBounds(polylineOverlay.getBounds(), 100, 100, 100, 100).animate(CameraAnimation.Easing);
                naverMap.moveCamera(cameraUpdate);
                Log.d(TAG, curIndex + " crsName: " + polylineOverlay.getTag());
            }

            polylineOverlay.setMap(null);
            crsMarkers.get(curIndex).setMap(null);
            Log.d(TAG, curIndex + " setNull " + polylineOverlay.getTag());
            curIndex++;
        }
        crsPolylineOverlays.clear();
        crsMarkers.clear();
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


}