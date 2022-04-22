package com.example.project.Map;

import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
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
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.project.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

@RequiresApi(api = Build.VERSION_CODES.P)
public class RecordMapActivity extends AppCompatActivity implements View.OnClickListener,
        MapView.CurrentLocationEventListener, MapViewFragment.OnConnectListener {

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private static double currentLat = 0;
    private static double currentLon = 0;

    // notification
    public final String CHANNEL_ID = "notification_channel";
    private CharSequence name = "map channel";
    private String description = "map";
    public final int NOTIFICATION_ID = 101;
    private NotificationCompat.Builder builder;
    private NotificationManagerCompat notificationManagerCompat;

    static RecordMapActivity recordMapActivity;

    private final String recordTag = "RecordTAG";
    private final String TAG = "MapTAG";
    private final int ACCESS_FINE_LOCATION = 1000;

    // 맵 변수
    private MapView mMap = null;
    private MapPolyline polyline = null;
    private int markerCount = 0;
    private boolean startMarker = false;
    int polylineIndex = 0;

    // 타이머 변수
    private int time = -1;
    private Timer timer;
    TimerTask timerTask;
    int total_sec = 0;

    // Fragment
    MapViewFragment mapViewFrag;
    RecordFragment recordFrag;

    // 기록 시작 버튼 체크 여부
    boolean recordPressed = false;
    // 기록 시작 여부
    boolean recordStart = false;

    // 위도 경도 저장
    ArrayList<Pair<Double, Double>> latlngArray = new ArrayList<>();
    int latlngIndex = 0;

    // 칼로리
    double calories;

    // 위도 경도 거리 속도 변수 값
    double curLat = 0.0;
    double curLng = 0.0;
    double befLat = 0.0;
    double befLng = 0.0;
    double distance = 0.0;
    double avgSpeed = 0.0;

    // FloatingActionButton
    FloatingActionButton recordStartFab;
    FloatingActionButton recordPauseFab;
    FloatingActionButton recordResumeFab;
    FloatingActionButton recordSaveFab;
    FloatingActionButton toRecordFab;
    FloatingActionButton toMapFab;

    // TextView
    TextView time_tv;
    TextView dist_tv;
    TextView cal_tv;

    String test1 = "";
    String test2 = "";
    TextView test1v;
    TextView test2v;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_record_activity);

        recordMapActivity = this;
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

    }
    @Override
    public void onStart() {
        super.onStart();
    }

    // MapFragment와 RecordMapActivity를 연결
    @Override
    public void onConnect(MapView mapView) {
        mMap = mapView;
        mMap.setCurrentLocationEventListener(this);

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
        // gps 권한 체크
        if (checkLocationService()) {
            permissionCheck();
        } else {
            Toast.makeText(this, "GPS를 켜주세요", Toast.LENGTH_SHORT).show();
        }

        // 타이머 시작
        startTimer();

        recordStartFab.hide();
        recordPauseFab.show();
        toMapFab.show();
        recordPressed = true;

        recordFrag = new RecordFragment();
        getSupportFragmentManager().beginTransaction().
                add(R.id.mainFrame, recordFrag, recordTag).
                hide(recordFrag).commit();

        // 시작 지점 마커 생성
        makeMarker("출발 지점");
        MapToRecord();
        Log.d("StartFab", "exec");
        displayNotification();
        onStartService();
    }

    // 정지 기능
    public void PauseFab() {
        // 트래킹 중지
        stopTracking();
        // 타이머 중지
        pauseTimer();

        recordPressed = false;
        recordStart = false;
        recordPauseFab.hide();
        recordResumeFab.show();
        recordSaveFab.show();

        Log.d("PauseFab", "exec");
    }

    // 재개 기능
    public void ResumeFab() {
        // 타이머 시작
        startTimer();
        // 트래킹 시작
        startTracking();

        recordPressed = true;
        recordResumeFab.hide();
        recordSaveFab.hide();
        recordPauseFab.show();
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

    // 저장 기능
    private void RecordSave() {
        recordResumeFab.hide();
        makeMarker("도착 지점");
        Toast.makeText(this.getApplicationContext(), "서비스 종료", Toast.LENGTH_SHORT).show();
        notificationManagerCompat.cancel(NOTIFICATION_ID);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("onResume", "...");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("onPause", "...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pauseTimer();
        stopTracking();

        Toast.makeText(this.getApplicationContext(), "서비스 종료", Toast.LENGTH_SHORT).show();
        if(notificationManagerCompat != null) {
            notificationManagerCompat.cancel(NOTIFICATION_ID);
            mLocationManager.removeUpdates(mLocationListener);
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
                        dist_tv = findViewById(R.id.distanceText);
                        time_tv = findViewById(R.id.pedo_time);
                        cal_tv = findViewById(R.id.calText);
                        if (time_tv != null)
                            time_tv.setText(hour + "H " + min + "M " + sec + "S");
                        if (dist_tv != null)
                            dist_tv.setText(Double.toString(Math.round(distance * 100) / 100.0));
                        if (cal_tv != null)
                            cal_tv.setText(calories + "kcal");
                        // 외부에서 테스트용
                        if (test1v != null)
                            test1v.setText(test1);
                        if (test2v != null)
                            test2v.setText(test2);
                        //System.out.println(avgSpeed);
                        // notification 수정


                        builder.setContentText(String.format(Locale.KOREA, "%02d:%02d:%02d / %.2f km", hour, min, sec, distance));
                        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());

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
        if (v > 0) {
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
        insertLatLngList(curLat, curLng);
        mMap.addPolyline(polyline);

        // 외부에서 테스트용도
//        test1 = test1.concat(String.format("(%.6f, %.6f) (%.6f, %.6f) = %.2f\n", curLat, curLng, befLat, befLng, distance));
//        test2 = test2.concat(String.format("accuracy (%.2f)\n", v));
//        System.out.println(test1 + '\n' + test2 + '\n');
    }

    public void insertLatLngList(double lat, double lng) {
        if (latlngArray.size() == 0) {
            latlngArray.add(new Pair<>(lat, lng));
        } else { // 비어있지 않으면
            // 그리고 lat값이나 lng값 중에 하나가 다르면
            if (latlngArray.get(latlngIndex).first != lat || latlngArray.get(latlngIndex).second != lng) {
                latlngArray.add(new Pair<>(lat, lng));
                befLat = latlngArray.get(latlngIndex).first;
                befLng = latlngArray.get(latlngIndex).second;
                distance += getDistance(curLat, curLng, befLat, befLng);
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

    // notification 설정
    public void displayNotification() {
        createNotificationChannel();
        Intent intent = new Intent(this, NotificationBroadcast.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addCategory(Intent.CATEGORY_LAUNCHER)
                .setAction(Intent.ACTION_MAIN);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.walk_over)
                .setContentTitle("걷기")
                .setContentText("00:00:00 / 0.0 km")
                .setPriority(NotificationManagerCompat.IMPORTANCE_DEFAULT)
                .setContentIntent(pendingIntent)
                .setOngoing(true);
        notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());


    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public void onStartService() {

        Toast.makeText(this.getApplicationContext(), "서비스 시작", Toast.LENGTH_SHORT).show();
        startService(new Intent(this, LocationService.class));
        addListenerLocation();
    }

    private void addListenerLocation() {
        mLocationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLat = location.getLatitude();
                currentLon = location.getLongitude();
                // polyline의 포인트 개수가 0 이상이면
                if(polyline.getPointCount() > 0){
                    MapPoint m = polyline.getPoint(polyline.getPointCount()-1);
                    double lastLat = m.getMapPointGeoCoord().latitude;
                    double lastLng = m.getMapPointGeoCoord().longitude;
                    distance += getDistance(currentLat,currentLon,lastLat,lastLng);
                }
                polyline.addPoint(MapPoint.mapPointWithGeoCoord(currentLat, currentLon));
                mMap.addPolyline(polyline);

                Toast.makeText(getApplicationContext(), String.format("%.5f %.5f",currentLat,currentLon), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (lastKnownLocation != null) {
                    currentLat = lastKnownLocation.getLatitude();
                    currentLon = lastKnownLocation.getLongitude();
                }

            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1000, 1, mLocationListener);
    }

}