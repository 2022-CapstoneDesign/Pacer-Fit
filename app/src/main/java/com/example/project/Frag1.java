package com.example.project;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import static com.example.project.TransLocalPoint.TO_GRID;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class Frag1 extends Fragment {

    private TextView weatherInfo;
    private ImageView weatherInfo_Image;
    private TransLocalPoint transLocalPoint;
    private GpsTracker gpsTracker;
    private String address = ""; // x,y는 격자x,y좌표
    String weather=""; // 날씨 결과
    String tmperature = ""; // 온도 결과



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.frag1,container,false);
        Context ct = container.getContext();
        TextView location = v.findViewById(R.id.location);
        weatherInfo = v.findViewById(R.id.weather);
        weatherInfo_Image = v.findViewById(R.id.weather_image);
        Button Km_button = v.findViewById(R.id.Km_button);
        Km_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View map) {
                Intent intent = new Intent(getActivity(),Map_add.class); //Fragment -> Activity로 이동 (Map_add.java)
                startActivity(intent);
            }
        });

        Button pedo_button = v.findViewById(R.id.pedo_button);
        pedo_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View map) {
                Intent intent = new Intent(getActivity(),StepCounter.class); //Fragment -> Activity로 이동 (StepCounter.java)
                startActivity(intent);
            }
        });

        Weather weatherMethod = new Weather();
        gpsTracker = new GpsTracker(ct);
        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();

        address = weatherMethod.getCurrentAddress(ct, latitude, longitude);
        String[] local = address.split(" ");  //주소를 대한민국, 서울특별시, xx구 ... 로 나눔
        // String localName = local[2]; //xx구 이름


        location.setText(local[1]+ " " + local[2]);

        transLocalPoint = new TransLocalPoint();
        TransLocalPoint.LatXLngY tmp = transLocalPoint.convertGRID_GPS(TO_GRID, latitude, longitude);
        Log.e(">>","x = "+ tmp.x + ", y = "+ tmp.y);

        String url = weatherMethod.weather(tmp.x,tmp.y);

        NetworkTask networkTask = new NetworkTask(url, null);
        networkTask.execute();

        Calendar now = Calendar.getInstance();
        int isAMorPM = now.get(Calendar.AM_PM);
        switch (isAMorPM) {
            case Calendar.AM:
                ((TextView)v.findViewById(R.id.ampm)).setText("오전");
                break;
            case Calendar.PM:
                ((TextView)v.findViewById(R.id.ampm)).setText("오후");
                break;
        }

        return v;

    }

    public String weatherJsonParser(String jsonString) throws JSONException {

        //response 키를 가지고 데이터를 파싱
        JSONObject jsonObject1 = new JSONObject(jsonString);
        String response = jsonObject1.getString("response");

        // response 로 부터 body 찾기
        JSONObject jsonObject2 = new JSONObject(response);
        String body = jsonObject2.getString("body");

        // body 로 부터 items 찾기
        JSONObject jsonObject3 = new JSONObject(body);
        String items = jsonObject3.getString("items");
        Log.i("ITEMS", items);

        // itmes로 부터 itemlist 를 받기
        JSONObject jsonObject4 = new JSONObject(items);
        JSONArray jsonArray = jsonObject4.getJSONArray("item");

        for(int i = 0; i < jsonArray.length(); i++){
            jsonObject4 = jsonArray.getJSONObject(i);
            String fcstValue = jsonObject4.getString("fcstValue");
            String category = jsonObject4.getString("category");

            if(category.equals("SKY")){
                if(fcstValue.equals("1")) {
                    weather = "맑음\n";
                } else if(fcstValue.equals("2")){
                    weather = "비\n";
                } else if(fcstValue.equals("3")){
                    weather = "구름 많음\n";
                } else if(fcstValue.equals("4")){
                    weather = "흐림\n";
                }
            }
            if(category.equals("TMP")){
                tmperature = fcstValue + "℃";
            }
        }
        return weather+tmperature;
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;
        String result;

        public NetworkTask(String url, ContentValues values){
            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {
            RequestHttpConnection requestHttpConnection = new RequestHttpConnection();
            result = requestHttpConnection.requset(url, values); //해당 URL로부터 결과
            try {
                weatherJsonParser(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            weatherInfo.setText(weather+tmperature);
            //doInBackground()로부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력
            if(weather.equals("맑음\n")){
                weatherInfo_Image.setImageResource(R.color.lightgray_project);
                weatherInfo_Image.setImageResource(R.drawable.weather_sunny);
            }
            else if(weather.equals("비\n")){
                weatherInfo_Image.setImageResource(R.color.lightgray_project);
                weatherInfo_Image.setImageResource(R.drawable.weather_rainy);
            }
            else if(weather.equals("구름 많음\n")){
                weatherInfo_Image.setImageResource(R.color.lightgray_project);
                weatherInfo_Image.setImageResource(R.drawable.weather_heavy_cloudy);
            }
            else if(weather.equals("흐림\n")){
                weatherInfo_Image.setImageResource(R.color.lightgray_project);
                weatherInfo_Image.setImageResource(R.drawable.weather_cloudy);
            }
        }
    }


}

