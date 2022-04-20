package com.example.project.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.project.R;
import com.example.project.Weather.GpsTrackerService;
import com.example.project.Weather.Weather;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapViewFragment extends Fragment {
    private static String TAG = "MapViewFragment_getPath";
    private static final String TAG_JSON="pacerfit";
    private static final String TAG_PATH = "gpxPath";
    private static final String TAG_SIGUN = "sigun";

    public MapView mapView;
    private OnConnectListener onConnectListener;
    MapPolyline polyline = new MapPolyline();
    private GpsTrackerService gpsTracker;
    private String address = ""; // x,y는 격자x,y좌표
    String location = null;
    String pathJsonString;
    ArrayList<HashMap<String, String>> pathArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_view_fragment, container, false);
        Context ct = container.getContext();
        mapView = new MapView(getActivity()); //실제 핸드폰으로 디코딩해야 지도가 나옵니다.
        ViewGroup mapViewContainer = (ViewGroup) rootView.findViewById(R.id.mapView);
        mapViewContainer.addView(mapView);
        onConnectListener.onConnect(mapView);
        polyline.setTag(1000);
        polyline.setLineColor(Color.argb(240, 255, 0, 255)); // Polyline 컬러 지정.

        Weather weatherMethod = new Weather();
        gpsTracker = new GpsTrackerService(ct);
        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();

        address = weatherMethod.getCurrentAddress(ct, latitude, longitude);
        String[] local = address.split(" ");  //주소를 대한민국, 서울특별시, xx구 ... 로 나눔
        local[1] = AreaChange(local[1]);  //서울특별시, 경기도등의 이름을 db에 맞게 수정
        location=(local[1] + " " + local[2]);
        Log.d(TAG, "location - " + location);
        pathArrayList = new ArrayList<>();

        GetGpxPathData task = new GetGpxPathData();
        task.execute("http://pacerfit.dothome.co.kr/getPathWithArea.php");






        new Thread() {
            public void run() {
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String gpxpt = getData();
                Bundle bun = new Bundle();
                bun.putString("gpxpt", gpxpt);
                Message msg = handler.obtainMessage();
                msg.setData(bun);
                handler.sendMessage(msg);
            }
        }.start();


        return rootView;

    }
    private class GetGpxPathData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(),
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) { //doInBackground에서 return한 값을 받음
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d(TAG, "response  - " + result);
            if (result == null){
                Log.d(TAG, errorString);
            }
            else {
                pathJsonString = result;
                System.out.println(pathJsonString);
                showResult();
            }
        }
        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];

            try {
                String selectLocation = "location="+location;
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
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }//연결상태 확인

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
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
    private void showResult(){
        try {
            JSONObject jsonObject = new JSONObject(pathJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String gpxPath = item.getString(TAG_PATH);
                String sigun = item.getString(TAG_SIGUN);


                HashMap<String,String> hashMap = new HashMap<>();

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
        HashMap<String,String> hashMap1 = pathArrayList.get(0);
        HashMap<String,String> hashMap2 = pathArrayList.get(1);

        try {
            url = new URL(hashMap2.get(TAG_PATH));
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
        return gpxpt;
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bundle bun = msg.getData();
            String gpxpt = bun.getString("gpxpt");
            String[] latLon = gpxpt.split(" ");
            for (int i = 0; i < latLon.length; i += 2) {
                polyline.addPoint(MapPoint.mapPointWithGeoCoord(Double.valueOf(latLon[i]), Double.valueOf(latLon[i + 1])));
            }
            mapView.addPolyline(polyline);

        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnConnectListener) {
            onConnectListener = (OnConnectListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnConnectListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onConnectListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public interface OnConnectListener {
        void onConnect(MapView mapView);
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

