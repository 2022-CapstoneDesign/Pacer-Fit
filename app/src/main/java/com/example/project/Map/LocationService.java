package com.example.project.Map;

import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.project.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class LocationService extends Service {


    public static final String TAG = "LocationService";
    public static final String BROADCAST_ACTION = "LocationService";
    public Intent intent;
    public MapPolyline polyline;

    public double curLng = 0.0;
    public double curLat = 0.0;

    // notification
    public final String CHANNEL_ID = "notification_channel";
    public final int NOTIFICATION_ID = 101;
    private CharSequence name = "map channel";
    private String description = "map";
    private NotificationCompat.Builder builder;
    private NotificationManagerCompat notificationManagerCompat;

    // 거리
    double distance = 0.0;
    // 칼로리
    double calories = 0.0;
    // 타이머 변수
    private int time = -1;
    private Timer timer;
    private TimerTask timerTask;
    int total_sec = 0;

    // 기록 시작 버튼 체크 여부
    boolean recordPressed = false;
    // 기록 시작 여부
    boolean recordStart = false;

    // bindService 구현
    private IBinder mIBinder = new MyBinder();

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult != null && locationResult.getLastLocation() != null) {
                Location location = locationResult.getLastLocation();
                float accuracy = location.getAccuracy();
                if (accuracy > 12.0)
                    return;

                curLat = location.getLatitude();
                curLng = location.getLongitude();
                polyline.addPoint(MapPoint.mapPointWithGeoCoord(curLat, curLng));
                distance = curDistance(polyline);
                Log.d(TAG, String.format("UPDATE LOCATION acc: %f lat: %f, lng: %f", accuracy, curLat, curLng));
            }
        }
    };


    class MyBinder extends Binder {
        LocationService getService() {
            return LocationService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    MapPolyline getPolyline() {
        return polyline;
    }

    Double getCurLat() {
        return curLat;
    }

    Double getCurLng() {
        return curLng;
    }

    private void startLocationService() {
        createNotificationChannel();
        displayNotification();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

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
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, mLocationCallback, Looper.getMainLooper());
        startForeground(1, builder.build());
        recordPressed = true;
        startTimer();
    }

    private void stopLocationService() {
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(mLocationCallback);
        stopForeground(true);
        stopSelf();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);

        // polyline 생성
        polyline = new MapPolyline();
        polyline.setTag(0);
        polyline.setLineColor(Color.argb(128, 255, 51, 0)); // Polyline 컬러 지정.

        Log.d("LocationService", "service onCreate");
        displayNotification();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(Constants.ACTION_START_LOCATION_SERVICE)) {
                    startLocationService();
                } else if (action.equals(Constants.ACTION_STOP_LOCATION_SERVICE)) {
                    stopLocationService();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        pauseTimer();
        notificationManagerCompat.cancel(NOTIFICATION_ID);
        stopLocationService();
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

    public double curDistance(MapPolyline mapPolyline) {
        MapPoint[] mapPoint = mapPolyline.getMapPoints();
        int index = mapPoint.length;
        double dist = 0.0;
        double latA, lngA, latB, lngB;
        for (int i = 0; i < index - 1; i++) {
            latA = mapPoint[i].getMapPointGeoCoord().latitude;
            lngA = mapPoint[i].getMapPointGeoCoord().longitude;
            latB = mapPoint[i + 1].getMapPointGeoCoord().latitude;
            lngB = mapPoint[i + 1].getMapPointGeoCoord().longitude;
            dist += getDistance(latA, lngA, latB, lngB);
            Log.d(TAG, String.format("curDistance[%d] latA: %f lngA: %f latB: %f lngB: %f distance %f", i, latA, lngA, latB, lngB, dist));

        }

        return dist;
    }

    // 타이머 기능
    public void startTimer() {
        recordPressed = true;

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                time++;
                total_sec = time;
                int sec = time % 60;
                int min = time / 60 % 60;
                int hour = time / 3600;

                // 액티비티로 데이터 전송
                intent.setAction("Thread");
                intent.putExtra("timer", total_sec);
                intent.putExtra("distance", distance);
                intent.putExtra("calories", calories);
                sendBroadcast(intent);

                // notification 업데이트
                builder.setContentText(String.format(Locale.KOREA, "%02d:%02d:%02d / %.2f km", hour, min, sec, distance));
                startForeground(1, builder.build());
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    // 타이머 정지
    public void pauseTimer() {
        recordPressed = false;
        recordStart = false;
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
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
                .setPriority(NotificationManagerCompat.IMPORTANCE_DEFAULT) // 알림음 없음
                .setContentIntent(pendingIntent)
                .setOngoing(true); // 알림바 지우지 못하게 유지
        notificationManagerCompat = NotificationManagerCompat.from(this);
        startForeground(NOTIFICATION_ID, builder.build());
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

    public void destroyNotification() {
        notificationManagerCompat.cancel(NOTIFICATION_ID);
    }

}