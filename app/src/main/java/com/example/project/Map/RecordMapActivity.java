package com.example.project.Map;

import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.project.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.daum.android.map.coord.MapCoordLatLng;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

@RequiresApi(api = Build.VERSION_CODES.P)
public class RecordMapActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener,
        MapView.CurrentLocationEventListener, MapFragment.OnConnectListener {

    private final String recordTag = "RecordTAG";
    private final String TAG = "MapTAG";
    private final int ACCESS_FINE_LOCATION = 1000;

    // 맵 변수
    private MapView mMap = null;
    private MapPolyline polyline = null;
    private int markerCount = 0;
    private boolean startMarker = false;

    // 타이머 변수
    private int time = -1;
    private Timer timer;
    TimerTask timerTask;
    int total_sec = 0;

    // Fragment
    MapFragment mapFrag;
    RecordFragment recordFrag;

    // 기록 시작 버튼 체크 여부
    boolean recordPressed = false;
    // 기록 시작 여부
    boolean recordStart = false;

    // 위도 경도 저장
    ArrayList<Pair<Double, Double>> latlngArray = new ArrayList<>();
    int latlngIndex = 0;

    // 만보기
    int pedometer = 0;
    SensorManager sensorManager;

    // 위도 경도 거리 속도 변수 값
    double curLat = 0.00;
    double curLng = 0.00;
    double befLat = 0.00;
    double befLng = 0.00;
    double distance = 0.00;
    double avgSpeed = 0.00;

    // FloatingActionButton
    FloatingActionButton recordStartFab;
    FloatingActionButton recordPauseFab;
    FloatingActionButton recordResumeFab;
    FloatingActionButton recordSaveFab;
    FloatingActionButton toRecordFab;
    FloatingActionButton toMapFab;

    // TextView
    TextView secTextView;
    TextView minTextView;
    TextView hourTextView;
    TextView recordPedometer;
    TextView distanceKm;
    TextView averageSpeed;

    MapPolyline mapPolyline;

    String test1 = "";
    String test2 = "";
    TextView test1v;
    TextView test2v;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_map);

        mapFrag = new MapFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.mainFrame, mapFrag).commit();

        // polyline
        polyline = new MapPolyline();
        polyline.setLineColor(Color.argb(128, 255, 51, 0)); // Polyline 컬러 지정.

        // 만보기
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

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

        // 현재 위치
        /*
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location userNowLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(userNowLocation!=null){
            curLat = userNowLocation.getLatitude();
            curLng = userNowLocation.getLongitude();

            Log.d("Location", userNowLocation.toString());
        }
         */


    }

    @Override
    public void onStart() {
        super.onStart();
        mapPolyline = new MapPolyline();
        mapPolyline.setLineColor(Color.argb(128, 255, 51, 0));
        mapPolyline.addPoint(MapPoint.mapPointWithGeoCoord(37.6645928, 126.8962906));
        mapPolyline.addPoint(MapPoint.mapPointWithGeoCoord(37.65382463422385, 126.89663234494247));
        mapPolyline.addPoint(MapPoint.mapPointWithGeoCoord(37.65382463422405, 126.89663234494267));
        mapPolyline.addPoint(MapPoint.mapPointWithGeoCoord(37.65382463422425, 126.89663234494297));
        mapPolyline.addPoint(MapPoint.mapPointWithGeoCoord(38.65382463422455, 126.89663234494327));
        mMap.addPolyline(mapPolyline);
        Log.d("onStart", "onStart is active");
    }

    // MapFragment와 RecordMapActivity를 연결
    @Override
    public void onConnect(MapView mapView) {
        mMap = mapView;
        mMap.setCurrentLocationEventListener(this);
        mMap.setZoomLevel(1, true);

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

    public void StartFab() {
        sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR),
                SensorManager.SENSOR_DELAY_NORMAL
        );

        if (checkLocationService()) {
            permissionCheck();
        } else {
            Toast.makeText(this, "GPS를 켜주세요", Toast.LENGTH_SHORT).show();
        }

        startTimer();
        recordStartFab.hide();
        recordPauseFab.show();
        recordPressed = true;
        recordFrag = new RecordFragment();
        getSupportFragmentManager().beginTransaction().
                add(R.id.mainFrame, recordFrag, recordTag).
                hide(recordFrag).commit();

        toMapFab.show();
        makeMarker("출발 지점");

        Log.d("StartFab", "exec");
    }

    public void PauseFab() {
        stopTracking();
        sensorManager.unregisterListener(this);
        pauseTimer();
        recordPressed = false;
        recordStart = false;
        recordPauseFab.hide();
        recordResumeFab.show();
        recordSaveFab.show();
        Log.d("PauseFab", "exec");
    }

    public void ResumeFab() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER), SensorManager.SENSOR_DELAY_NORMAL);
        startTimer();
        startTracking();
        recordPressed = true;
        recordResumeFab.hide();
        recordSaveFab.hide();
        recordPauseFab.show();

    }

    private void recordToMap() {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.from_left, R.anim.from_right).show(mapFrag).commit();
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.from_right, R.anim.from_left).hide(recordFrag).commit();

        toRecordFab.show();
        toMapFab.hide();
    }

    private void MapToRecord() {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.from_left, R.anim.to_right).hide(mapFrag).commit();
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.from_left, R.anim.to_right).show(recordFrag).commit();
        toRecordFab.hide();
        toMapFab.show();
    }

    private void RecordSave() {
        recordResumeFab.hide();
        makeMarker("도착 지점");
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy");
        pauseTimer();
        stopTracking();
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            Log.d("Sensor ", "Success");
            if (event.values[0] == 1.0f) {
                pedometer += 1;
            }
        }
    }


    // 타이머 기능
    private void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                time++;
                total_sec = time;

                int sec = time % 60;
                int min = time / 60 % 60;
                int hour = time / 3600;
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        // textview의 값들이 계속 null로 나와서 여기에 뒀습니다.
                        recordPedometer = findViewById(R.id.recordPedometer);
                        secTextView = findViewById(R.id.secTextView);
                        minTextView = findViewById(R.id.minTextView);
                        hourTextView = findViewById(R.id.hourTextView);
                        distanceKm = findViewById(R.id.distanceKm);
                        averageSpeed = findViewById(R.id.averageSpeed);
                        test1v = findViewById(R.id.locTest);
                        test2v = findViewById(R.id.accTest);
                        if (secTextView != null)
                            secTextView.setText(Integer.toString(sec));
                        if (minTextView != null)
                            minTextView.setText(Integer.toString(min));
                        if (hourTextView != null)
                            hourTextView.setText(Integer.toString(hour));
                        if (distanceKm != null)
                            distanceKm.setText(Double.toString(Math.round(distance * 100) / 100.0));
                        if (averageSpeed != null)
                            averageSpeed.setText(Double.toString(Math.round(avgSpeed * 100) / 100.0));
                        if (recordPedometer != null)
                            recordPedometer.setText(Integer.toString(pedometer));
                        if(test1v != null)
                            test1v.setText(test1);
                        if(test2v!=null)
                            test2v.setText(test2);
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 1000);

    }

    // 타이머 정지
    private void pauseTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }


    private boolean checkLocationService() {
        LocationManager locationManger = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManger.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    // GPS 허용 여부 권환 확인
    private void permissionCheck() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        SharedPreferences preference = getPreferences(MODE_PRIVATE);
        boolean isFirstCheck = preference.getBoolean("isFirstPermissionCheck", true);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없는 상태
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // 권한 거절시 다시 한번 물어봄
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("현재 위치를 확인하시려면 위치 권한을 허용해주세요");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(RecordMapActivity.this, permissions, ACCESS_FINE_LOCATION);
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
            } else {
                if (isFirstCheck) {
                    // 최초 권한 요청
                    preference.edit().putBoolean("isFirstPermissionCheck", false).apply();
                    ActivityCompat.requestPermissions(RecordMapActivity.this, permissions, ACCESS_FINE_LOCATION);
                } else {
                    // 다시 묻지 않음 클릭 (앱 정보 화면으로 이동)
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("현재 위치를 확인하시려면 설정에서 위치 권한을 허용해주세요");
                    builder.setPositiveButton("설정으로 이동", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName"));
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.show();
                }
            }
        } else {
            // 권한이 있는 상태
            startTracking();
        }
    }

    // Tracking 시작
    private void startTracking() {
        mMap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);

        Log.d("startTracking", "tracking is start");
    }

    // Tracking 중지
    private void stopTracking() {
        mMap.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        Log.d("stopTracking", "tracking is stop");
    }

    // 현재 위치 업데이트 함수
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
        if(v > 10.0) {
            Log.i(TAG, "v is over 10");
            return;
        }
        Log.i(TAG, "MapFragment:onCurrentLocationUpdate()");
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude));
        curLat = mapPointGeo.latitude;
        curLng = mapPointGeo.longitude;
        if (!startMarker) {
            startMarker = true;
            makeMarker("시작 지점");
        }
        insertLatLngList(curLat,curLng);
        mMap.addPolyline(polyline);
        test1 = test1.concat(String.format("(%.6f, %.6f) (%.6f, %.6f) = %.2f\n", curLat, curLng, befLat, befLng, distance));
        test2 = test2.concat(String.format("accuracy (%.2f)\n", v));
        System.out.println(test1 + '\n' + test2 + '\n');
    }

    public void insertLatLngList(double lat, double lng) {
        if (latlngArray.size() == 0) {
            latlngArray.add(new Pair<>(lat, lng));
        } else { // 비어있지 않으면
            // 그리고 lat값이나 lng값 중에 하나가 다르면
            if (latlngArray.get(latlngIndex).first != lat || latlngArray.get(latlngIndex).second != lng) {
                latlngArray.add(new Pair<>(lat,lng));
                befLat = latlngArray.get(latlngIndex).first;
                befLng = latlngArray.get(latlngIndex).second;
                distance += getDistance(curLat, curLng, befLat, befLng);
                avgSpeed = distance / total_sec;
                latlngIndex++;
            }
        }

    }

    // 두 위치의 거리 계산 함수
    public double getDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        double R = 6372.8 * 1000;
        double dLat = toRadians(lat2 - lat1);
        double dLon = toRadians(lon2 - lon1);
        double a = pow(sin(dLat / 2), 2.0) + pow(sin(dLon / 2), 2.0) * cos(toRadians(lat1)) * cos(toRadians(lat2));
        double c = 2 * asin(sqrt(a));
        return R * c / 1000;
    }

    // 현재 위치에 마커를 생성하는 함수
    public void makeMarker(String tag) {
        MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(curLat, curLng);
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName(tag);
        marker.setTag(markerCount++);
        marker.setMapPoint(MARKER_POINT);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        mMap.addPOIItem(marker);
        Log.i("Marker ", String.format("위치 (%f, %f)에 %s 마커를 생성 ", curLat, curLng, tag));

    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {
        Log.i(TAG, "onCurrentLocationUpdateFailed");
    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {
        Log.i(TAG, "onCurrentLocationUpdateCancelled");
    }

}
