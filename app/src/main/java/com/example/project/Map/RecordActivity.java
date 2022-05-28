package com.example.project.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.example.project.Weather.GpsTrackerService;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.FusedLocationSource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private NaverMap naverMap;

    TextView time_tv;
    TextView dist_tv;
    TextView cal_tv;
    TextView avgSpeed_tv;
    double cal;
    double dist;
    double speed;
    int time;

    // 맵 위치
    private FusedLocationSource locationSource;

    List<LatLng> recordPath;
    ArrayList<String> crsNameList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_record_result);
        Intent intent = getIntent();
        cal = intent.getDoubleExtra("cal", 0.0);
        dist = intent.getDoubleExtra("distance", 0.0);
        time = intent.getIntExtra("time", 0);
        Serializable locationList = intent.getSerializableExtra("userLocationList");
        Serializable nameList = intent.getSerializableExtra("ratingCrsList");

        recordPath = (ArrayList<LatLng>) locationList;
        crsNameList = (ArrayList<String>) nameList;


        time_tv = findViewById(R.id.result_time);
        dist_tv = findViewById(R.id.result_dist);
        cal_tv = findViewById(R.id.result_cal);
        avgSpeed_tv = findViewById(R.id.result_avg_speed);


        time_tv.setText(Constants.formattingTime(time));
        dist_tv.setText(Constants.formattingDist(dist));
        cal_tv.setText(Constants.formattingCal(cal));
        avgSpeed_tv.setText(Constants.formattingSpeed(dist / (double) time));


        // 위치 변수 생성
        locationSource = new FusedLocationSource(this, Constants.LOCATION_PERMISSION_REQUEST_CODE);

        mapView = findViewById(R.id.record_mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        for (int i = crsNameList.size() - 1; i >= 0; i--) {
            RatingDialog dlg = new RatingDialog(this, crsNameList.get(i));
            dlg.show();
        }

    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setZoomControlEnabled(false);






        if (recordPath.size() > 2) {
            PathOverlay pathOverlay = new PathOverlay(recordPath);
            pathOverlay.setColor(Color.RED);
            pathOverlay.setMap(naverMap);

            createMarker("출발 지점", pathOverlay.getCoords().get(0).latitude, pathOverlay.getCoords().get(0).longitude);
            createMarker("도착 지점", pathOverlay.getCoords().get(pathOverlay.getCoords().size() - 1).latitude, pathOverlay.getCoords().get(pathOverlay.getCoords().size() - 1).longitude);

            CameraUpdate cameraUpdate = CameraUpdate.fitBounds(pathOverlay.getBounds(), 100, 100, 100, 100);
            naverMap.moveCamera(cameraUpdate);
        }else{
            // 현재 좌표로 변경
            double curLat;
            double curLng;
            GpsTrackerService gps = new GpsTrackerService(getApplicationContext());
            curLat = gps.getLatitude();
            curLng = gps.getLongitude();


            //위치 및 각도 조정
            CameraPosition cameraPosition = new CameraPosition(
                    new LatLng(curLat, curLng),         // 위치 지정
                    15,                           // 줌 레벨
                    20,                             // 기울임 각도
                    0                            // 방향
            );
            naverMap.setCameraPosition(cameraPosition);
            // 위치
            naverMap.setLocationSource(locationSource);
            // 초기 위치 표시
            naverMap.setLocationTrackingMode(LocationTrackingMode.NoFollow);
        }

    }

    // 현재 위치에 마커를 생성하는 함수
    public void createMarker(String tag, double lat, double lng) {
        Marker marker = new Marker();
        marker.setPosition(new LatLng(lat, lng));
        marker.setIcon(OverlayImage.fromResource(R.drawable.marker_icon));
        marker.setCaptionText(tag);
        marker.setWidth(100);
        marker.setHeight(100);
        marker.setMap(naverMap);
    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}

