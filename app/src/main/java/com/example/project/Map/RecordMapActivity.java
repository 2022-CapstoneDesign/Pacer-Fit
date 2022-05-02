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
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
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


    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    // TAG
    private final String recordTag = "RecordTAG";
    private final String TAG = "RecordMapActivity";
    private static final String TAG_JSON = "pacerfit";
    private static final String TAG_PATH = "gpxPath";
    private static final String TAG_SIGUN = "sigun";

    // 네이버 맵 변수
    private MapView mapView;
    private NaverMap naverMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private PolylineOverlay curUserPolyline;
    private List<LatLng> latLngList;

    private boolean startPoint = false;
    private boolean isBackground = false;
    // Fragment
    private MapViewFragment mapViewFrag;
    private RecordFragment recordFrag;

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
    private double calories;
    // 시간 변수
    private int time = -1;

    // Service bind
    private LocationService mService;

    MyBroadcast br;
    IntentFilter filter;


    private GpsTrackerService gpsTracker;
    private String address = ""; // x,y는 격자x,y좌표
    String location = null;
    String pathJsonString;
    ArrayList<HashMap<String, String>> pathArrayList;


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

        if (mapViewFrag == null) {
            mapViewFrag = new MapViewFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.mainFrame, mapViewFrag).commit();
        }

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

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

        latLngList = new ArrayList<>();
        curUserPolyline = new PolylineOverlay();
        curUserPolyline.setWidth(10);
        curUserPolyline.setColor(Color.RED);
        latLngList.add(new LatLng(33.124124,126.12215));
        latLngList.add(new LatLng(33.1254124,35.122515));
        curUserPolyline.setCoords(latLngList);

        Weather weatherMethod = new Weather();
        gpsTracker = new GpsTrackerService(this);
        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();


        address = weatherMethod.getCurrentAddress(this, latitude, longitude);
        String[] local = address.split(" ");  //주소를 대한민국, 서울특별시, xx구 ... 로 나눔
        local[1] = AreaChange(local[1]);  //서울특별시, 경기도등의 이름을 db에 맞게 수정
        location = (local[1] + " " + local[2]);
        Log.d(TAG, "location - " + location);
        pathArrayList = new ArrayList<>();

        GetGpxPathData task = new GetGpxPathData();
        task.execute("http://pacerfit.dothome.co.kr/getPathWithArea.php");


    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;

        naverMap.setLocationSource(locationSource);
        // 트래킹 모드
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        naverMap.addOnLocationChangeListener(location ->{

        });
        Log.d(TAG, "onMapReady");
    }

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
                        if (mService != null) {
                            latLngList = mService.getUserLocationList();
                            if (!startPoint && !latLngList.isEmpty()) {
                                startPoint = true;
                                makeMarker("출발지점", latLngList.get(0).latitude, latLngList.get(0).longitude);
                            }
                            if (latLngList.size() > 2) {
                                curUserPolyline.setCoords(latLngList);
                                curUserPolyline.setMap(naverMap);
                            }
                        }

                        dist_tv = findViewById(R.id.distanceText);
                        time_tv = findViewById(R.id.pedo_time);
                        cal_tv = findViewById(R.id.calText);

                        time = intent.getIntExtra("timer", 0);
                        distance = intent.getDoubleExtra("distance", 0.0);
                        calories = intent.getDoubleExtra("calories", 0.0);

                        int sec = time % 60;
                        int min = time / 60 % 60;
                        int hour = time / 3600;
                        if (time_tv != null)
                            time_tv.setText(hour + "H " + min + "M " + sec + "S");
                        if (dist_tv != null)
                            dist_tv.setText(Double.toString(Math.round(distance * 100) / 100.0));
                        if (cal_tv != null)
                            cal_tv.setText(calories + "kcal");
                        Log.d("BroadCast", "Received BroadCast " + time + " " + distance);
                    }
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isBackground = false;
        if(mService !=null)
            mService.isBackground = false;
        Log.d(TAG, "onResume " + isBackground);
    }

    @Override
    public void onPause() {
        super.onPause();
        isBackground = true;
        if(mService !=null)
            mService.isBackground = true;
        Log.d(TAG, "onPause " + isBackground);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 트래킹 중지
        //stopTracking(naverMap);

        // 서비스 중지
        if (isLocationServiceRunning()) {
            stopLocationService();
            Toast.makeText(this.getApplicationContext(), "서비스 종료", Toast.LENGTH_SHORT).show();
        }
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
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(RecordMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            startLocationService();
        }
        //startTracking(naverMap);

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
        MapToRecord();


        // 로그
        Log.d(TAG, "StartFab");
    }

    // 정지 기능
    public void PauseFab() {
        // 트래킹 중지
        //stopTracking(naverMap);

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
        // 트래킹 시작
        //startTracking(naverMap);

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
        if (isLocationServiceRunning())
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

    /*
        // 권한
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationService();
                } else {
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    */
    // 서비스 실행 여부 체크
    private boolean isLocationServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationService.class.getName().equals(service.service.getClassName())) {
                    if (service.foreground) {
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    // 서비스 시작
    private void startLocationService() {
        if (!isLocationServiceRunning()) {
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
        if (isLocationServiceRunning()) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
            stopService(intent);
            unbindService(sconn);
            Toast.makeText(this, "Location service stopped", Toast.LENGTH_SHORT).show();
            unregisterReceiver(br);
        }
    }

    /*
        // Tracking 시작
        private void startTracking(@NonNull NaverMap naverMap) {
            naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            Log.d(TAG, "startTracking");
        }

        // Tracking 중지
        private void stopTracking(@NonNull NaverMap naverMap) {
            naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            Log.d(TAG, "stopTracking");
        }

     */
    // 현재 위치에 마커를 생성하는 함수
    public void makeMarker(String tag, double lat, double lng) {
        Marker marker = new Marker();
        marker.setPosition(new LatLng(lat, lng));
        marker.setCaptionText(tag);
        marker.setIconTintColor(Color.BLUE);
        marker.setWidth(50);
        marker.setHeight(50);
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
                        String gpxpt = getData();
                        Bundle bun = new Bundle();
                        bun.putString("gpxpt", gpxpt);
                        Message msg = handler.obtainMessage();
                        msg.setData(bun);
                        handler.sendMessage(msg);
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

                String gpxPath = item.getString(TAG_PATH);
                String sigun = item.getString(TAG_SIGUN);


                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put(TAG_PATH, gpxPath);
                hashMap.put(TAG_SIGUN, sigun);

                pathArrayList.add(hashMap);

            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }


    private String getData() {

        String gpxpt = "";
        URL url = null;
        HttpURLConnection http = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        int size = pathArrayList.size();

        if (size >= 2) {
            HashMap<String, String> hashMap1 = pathArrayList.get(0);
            HashMap<String, String> hashMap2 = pathArrayList.get(1);
            HashMap<String, String> hashMap3 = pathArrayList.get(2);

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
        }
        return gpxpt;
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bun = msg.getData();
            String gpxpt = bun.getString("gpxpt");
            String[] latLon = gpxpt.split(" ");
            List<LatLng> COORDS = new ArrayList<>();
            for (int i = 0; i < latLon.length; i += 2) {
                COORDS.add(new LatLng(Double.valueOf(latLon[i]), Double.valueOf(latLon[i + 1])));
            }
            PolylineOverlay polylineOverlay1 = new PolylineOverlay();
            polylineOverlay1.setWidth(10);
            polylineOverlay1.setCoords(COORDS);
            polylineOverlay1.setColor(Color.GREEN);
            polylineOverlay1.setMap(naverMap);

        }


    };


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