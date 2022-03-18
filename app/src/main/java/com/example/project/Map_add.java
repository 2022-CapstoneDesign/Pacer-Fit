package com.example.project;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

public class Map_add extends AppCompatActivity implements MapView.CurrentLocationEventListener {
    private final static String KEY = "gK02LAH%2FlvryeAYsHR08%2Byds3IuKYwmKnKEjPkvtot7WECTfDCyLeh9snhRqmJiWCWhHHwev8Sd3wvJTgXcVNA%3D%3D";
    private final static String TAG = "MapTAG";
    private final int ACCESS_FINE_LOCATION = 1000;
    private MapView mapView = null;
    private EditText search_pos = null;
    private TextView result = null;
    private String data = null;
    private MapPolyline polyline = null;
    private ImageButton searchBtn = null;
    private Button startBtn = null;
    private Button stopBtn = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_add);

        search_pos = findViewById(R.id.search_pos);
        searchBtn = findViewById(R.id.search_btn);
        startBtn = findViewById(R.id.start_btn);
        stopBtn = findViewById(R.id.stop_btn);
        result = findViewById(R.id.result);

        polyline = new MapPolyline();
        polyline.setLineColor(Color.argb(128, 255, 51, 0)); // Polyline 컬러 지정.
        mapView = new MapView(this); //실제 핸드폰으로 디코딩해야 지도가 나옵니다.

        searchBtn.setOnClickListener(v -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    data = getXmlData();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            result.setText(data);
                        }
                    });
                }
            }).start();

        });

        //지도 표시 (activity_map_add.xml)

        mapView.setCurrentLocationEventListener(this);
        // 중심점 변경
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.6645928, 126.8862906), true);
        // 줌 레벨 변경
        mapView.setZoomLevel(1, true);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        //마커 찍기
        MapPoint MARKER_POINT = MapPoint.mapPointWithGeoCoord(37.6645928, 126.8962906);
        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("Default Marker");
        marker.setTag(0);
        marker.setMapPoint(MARKER_POINT);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        mapView.addPOIItem(marker);

        // 시작 버튼을 누르면 GSP 권한 체크 후 추적 시작
        startBtn.setOnClickListener(v -> {
            if (checkLocationService()) {
                permissionCheck();
            } else {
                Toast.makeText(this, "GPS를 켜주세요", Toast.LENGTH_SHORT).show();
            }
        });

        // 중지 버튼을 누르면 추적 중지
        stopBtn.setOnClickListener(v -> {
            stopTracking();
        });

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
                        ActivityCompat.requestPermissions(Map_add.this, permissions, ACCESS_FINE_LOCATION);
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
                    ActivityCompat.requestPermissions(Map_add.this, permissions, ACCESS_FINE_LOCATION);
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

    private boolean checkLocationService() {
        LocationManager locationManger = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManger.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void startTracking() {
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
    }


    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
        Log.i(TAG, "MapFragment:onCurrentLocationUpdate()");
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        polyline.addPoint(MapPoint.mapPointWithGeoCoord(mapPointGeo.latitude, mapPointGeo.longitude));
        mapView.addPolyline(polyline);
        Log.i(TAG, String.format("(%f, %f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, v));
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {
        Log.i(TAG, "onCurrentLocationUpdateFailed");
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {
        Log.i(TAG, "onCurrentLocationUpdateCancelled");
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
    }

    private void stopTracking() {
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);

    }


    // xml 데이터를 가져오는 함수
    public String getXmlData() {
        StringBuffer buffer = new StringBuffer();
        String str = search_pos.getText().toString();//EditText에 작성된 Text얻어오기
        String location = URLEncoder.encode(str);//한글의 경우 인식이 안되기에 utf-8 방식으로 encoding..
        String queryUrl = "http://api.visitkorea.or.kr/openapi/service/rest/Durunubi/courseList?serviceKey=" +
                KEY +
                "&pageNo=1" +
                "&numOfRows=10" +
                "&MobileOS=ETC" +
                "&MobileApp=AppTest" +
                "&crsKorNm=" +
                location +
                "&brdDiv=DNWW";
        //검색 URL부분
        try {
            URL url = new URL(queryUrl); //검색 URL부분
            InputStream is = url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8")); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();//테그 이름 얻어오기
                        if (tag.equals("item")) ;
                        else if (tag.equals("routeIdx")) { //title 만나면 내용을 받을수 있게 하자
                            buffer.append("길 고유번호 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("crsIdx")) {
                            buffer.append("코스 고유번호 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("crsKorNm")) {
                            buffer.append("코스 명 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("crsDstnc")) {
                            buffer.append("코스 길이 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("crsTotlRqrmHour")) {
                            buffer.append("총 소요시간: ");
                            xpp.next();
                            buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("crsLevel")) {
                            buffer.append("난이도 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("crsCycle")) {
                            buffer.append("순환형태 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("crsContents")) {
                            buffer.append("코스 설명 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("crsSummary")) {
                            buffer.append("코스 개요 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("crsTourInfo")) {
                            buffer.append("관광 포인트 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("travelerinfo")) {
                            buffer.append("여행자정보 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("sigun")) {
                            buffer.append("행정구역 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("brdDiv")) {
                            buffer.append("걷기/자전거 구분 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        } else if (tag.equals("gpxpath")) {
                            buffer.append("GPX 경로 : ");
                            xpp.next();
                            buffer.append(xpp.getText());//title 요소의 TEXT 읽어와서 문자열버퍼에 추가
                            buffer.append("\n"); //줄바꿈 문자 추가
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName(); //테그 이름 얻어오기

                        if (tag.equals("item")) buffer.append("\n");// 첫번째 검색결과종료..줄바꿈
                        break;

                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            result.setText("에러가..났습니다...");
        }
        return buffer.toString();//StringBuffer 문자열 객체 반환
    }

}
