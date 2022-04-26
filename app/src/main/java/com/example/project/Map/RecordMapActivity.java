package com.example.project.Map;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

@RequiresApi(api = Build.VERSION_CODES.P)
public class RecordMapActivity extends AppCompatActivity implements View.OnClickListener,
        MapViewFragment.OnConnectListener {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;


    private final String recordTag = "RecordTAG";
    private final String TAG = "RecordMapActivity";

    // 맵 변수
    public MapView mMap = null;
    private MapPolyline polyline = null;
    private MapPolyline polyline2 = null;
    private int markerCount = 0;

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

    private LocationService mService;


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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_record_activity);

        mapViewFrag = new MapViewFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.mainFrame, mapViewFrag).commit();

        // polyline
        polyline = new MapPolyline();
        polyline.setTag(0);
        polyline.setLineColor(Color.argb(128, 255, 51, 0)); // Polyline 컬러 지정.

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

        MyBroadcast br = new MyBroadcast();
        IntentFilter filter = new IntentFilter();

        filter.addAction("Thread");
        registerReceiver(br, filter);

        GpsTrackerService gps = new GpsTrackerService(getApplicationContext());


    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 트래킹 중지
        stopTracking();

        // 서비스 중지
        Toast.makeText(this.getApplicationContext(), "서비스 종료", Toast.LENGTH_SHORT).show();

        if (isLocationServiceRunning())
            stopLocationService();

        if (mService != null)
            unbindService(sconn);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    // MapFragment와 RecordMapActivity를 연결
    @Override
    public void onConnect(MapView mapView) {
        if (mapView != null)
            mMap = mapView;
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

        bindService(new Intent(RecordMapActivity.this, LocationService.class), sconn, BIND_AUTO_CREATE);

        // 로그
        Log.d(TAG, "StartFab");
    }

    // 정지 기능
    public void PauseFab() {
        // 트래킹 중지
        stopTracking();

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
        startTracking();

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
        makeMarker("도착 지점");

        mService.destroyNotification();
        stopLocationService();

        // 서비스 종료
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
            Toast.makeText(this, "Location service started", Toast.LENGTH_SHORT).show();
        }
        startTracking();
    }

    // 서비스 종료
    private void stopLocationService() {
        if (isLocationServiceRunning()) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
            stopService(intent);
            Toast.makeText(this, "Location service stopped", Toast.LENGTH_SHORT).show();
        }
    }

    // 서비스로부터 값을 받아와서 UI에 적용
    public class MyBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("Thread")) {
                if (mService != null)
                    polyline = mService.getPolyline();
                mMap.addPolyline(polyline);
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


    // Tracking 시작
    private void startTracking() {
        if (mMap != null)
            mMap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        Log.d(TAG, "startTracking");
    }

    // Tracking 중지
    private void stopTracking() {
        if (mMap != null)
            mMap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        Log.d(TAG, "stopTracking");
    }

    // 현재 위치에 마커를 생성하는 함수
    public void makeMarker(String tag) {
        if (mService != null) {
            MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(mService.getCurLat(), mService.getCurLng());
            MapPOIItem marker = new MapPOIItem();
            marker.setItemName(tag);
            marker.setTag(markerCount++);
            marker.setMapPoint(MARKER_POINT);
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
            mMap.addPOIItem(marker);
            Log.i(TAG, "Marker " + String.format("위치 (%f, %f)에 %s 마커를 생성 ", mService.getCurLat(), mService.getCurLng(), tag));
        }
    }


}