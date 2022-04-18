package com.example.project.Map;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.R;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.ticofab.androidgpxparser.parser.GPXParser;
import io.ticofab.androidgpxparser.parser.domain.Gpx;
import io.ticofab.androidgpxparser.parser.domain.Track;

public class MapViewFragment extends Fragment {
    public MapView mapView;
    private OnConnectListener onConnectListener;
    MapPolyline polyline = new MapPolyline();
    GPXParser parser = new GPXParser();
    Gpx parsedGpx = null;
    double lat, lon;
    ArrayList<HashMap<String, String>> mArrayList;
    String mJsonString;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_view_fragment, container, false);
        mapView = new MapView(getActivity()); //실제 핸드폰으로 디코딩해야 지도가 나옵니다.
        ViewGroup mapViewContainer = (ViewGroup) rootView.findViewById(R.id.mapView);
        mapViewContainer.addView(mapView);
        onConnectListener.onConnect(mapView);
        polyline.setTag(1000);
        polyline.setLineColor(Color.argb(240, 255, 0, 255)); // Polyline 컬러 지정.

        new Thread(){
            public void run(){
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
    private String getData(){
        String gpxpt = "";
        URL url =null;
        HttpURLConnection http = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try{
            url = new URL("https://www.durunubi.kr/editImgUp.do?filePath=/data/koreamobility/file/manual/2018gpx/034_04_PartGPX_GPX_01.gpx");
            http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(3*1000);
            http.setReadTimeout(3*1000);
            isr = new InputStreamReader(http.getInputStream());
            br = new BufferedReader(isr);
            String str = null;
            while ((str = br.readLine()) != null)
            {
                if(str.contains("trkpt")){
                    Pattern pattern = Pattern.compile("[\"](.*?)[\"]");
                    Matcher matcher = pattern.matcher(str);
                    while (matcher.find()) {  // 일치하는 게 있다면
                        gpxpt += matcher.group(1)+" ";
                        if(matcher.group(1) ==  null)
                            break;

                    }
                }
            }
        }catch(Exception e){
            Log.e("Exception", e.toString());
        }finally
        {
            if(http != null){
                try{http.disconnect();
                }catch(Exception e){}
            } if(isr != null){
            try{isr.close();
            }catch(Exception e){}
        } if(br != null){
            try{br.close();
            }catch(Exception e){}
        }
        } return gpxpt;
    }

    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            Bundle bun = msg.getData();
            String gpxpt = bun.getString("gpxpt");
            String[] latLon = gpxpt.split(" ");
            for(int i=0;i<latLon.length;i+=2){
                polyline.addPoint(MapPoint.mapPointWithGeoCoord(Double.valueOf(latLon[i]), Double.valueOf(latLon[i+1])));
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
}
