package com.example.project.Map;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.se.omapi.SEService;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.R;

import net.daum.android.map.MapActivity;
import net.daum.mf.map.api.MapLayout;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import io.ticofab.androidgpxparser.parser.GPXParser;
import io.ticofab.androidgpxparser.parser.domain.Gpx;
import io.ticofab.androidgpxparser.parser.domain.Track;
import me.himanshusoni.gpxparser.modal.GPX;

public class MapFragment extends Fragment {

    public MapView mapView;
    private OnConnectListener onConnectListener;
    GPXParser parser = new GPXParser();
    Gpx parsedGpx = null;
    GPX gpx = new GPX();
    MapPolyline polyline = new MapPolyline();
    double lat, lon;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = new MapView(getActivity()); //실제 핸드폰으로 디코딩해야 지도가 나옵니다.
        ViewGroup mapViewContainer = (ViewGroup) rootView.findViewById(R.id.mapView);
        mapViewContainer.addView(mapView);
        onConnectListener.onConnect(mapView);
        polyline.setTag(1000);
        polyline.setLineColor(Color.argb(240, 255, 0, 255)); // Polyline 컬러 지정.
        try {
            Track str;
            InputStream in = getContext().getResources().openRawResource(R.raw.examplefile);
            //InputStream in = getContext().getAssets().open("examplefile.gpx");
            parsedGpx = parser.parse(in); // consider using a background thread
            System.out.println(parsedGpx.getCreator()+"========================1");
            System.out.println(parsedGpx.getMetadata()+"========================2");
            System.out.println(parsedGpx.getRoutes()+"========================3");
            System.out.println(parsedGpx.getTracks().get(0).getTrackName()+"========================4");
            System.out.println(parsedGpx.getTracks().get(0).getTrackLink()+"========================7");
            System.out.println(parsedGpx.getTracks().get(0).getTrackSrc()+"========================9");
            System.out.println(parsedGpx.getTracks().get(0).getTrackType()+"========================10");
            System.out.println(parsedGpx.getTracks().get(0).getTrackSegments().get(0).getTrackPoints().get(0).getLatitude()+"========================11");
            System.out.println(parsedGpx.getTracks().get(0).getTrackSegments().get(0).getTrackPoints().get(0).getLongitude()+"========================11");
            // getTracks()해서 track(이 track정보는 1개 이상일것)을 가져오고 첫번째 SegmentsTrack(거의 이 track정보는 1개일것)의 첫번째 track의 코스 정보
            // 가져와서 해당 트랙의 n번째 위도, 경도 정보를 뽑음.
            System.out.println(parsedGpx.getTracks().get(0).getTrackSegments().size()+"==================segment사이즈?");
            System.out.println(parsedGpx.getTracks().size()+"==================track사이즈?");
            System.out.println(parsedGpx.getTracks().get(0).getTrackSegments().get(0).getTrackPoints().size()+"==================trackpoint사이즈?");
            for(int j=0; j<parsedGpx.getTracks().size(); j++) {
                System.out.println(j+"번째 코스=======================");
                for (int i = 0; i < parsedGpx.getTracks().get(j).getTrackSegments().get(0).getTrackPoints().size(); i++) {
                    lat = parsedGpx.getTracks().get(j).getTrackSegments().get(0).getTrackPoints().get(i).getLatitude();
                    lon = parsedGpx.getTracks().get(j).getTrackSegments().get(0).getTrackPoints().get(i).getLongitude();
                    System.out.print(lat + ", ");
                    System.out.println(lon + " " + i + "번째 위치값");
                    /* 끊어진 좌표 처리... 잘안됨
                    if( (j>0) && (i==0) ) {
                        if((lat - parsedGpx.getTracks().get(j-1).getTrackSegments().get(0).getTrackPoints().get(parsedGpx.getTracks().get(j-1).getTrackSegments().get(0).getTrackPoints().size()-1).getLatitude() < 0.00002 &&
                                lat - parsedGpx.getTracks().get(j-1).getTrackSegments().get(0).getTrackPoints().get(parsedGpx.getTracks().get(j-1).getTrackSegments().get(0).getTrackPoints().size()-1).getLatitude() > -0.00002)
                        ||(lon - parsedGpx.getTracks().get(j-1).getTrackSegments().get(0).getTrackPoints().get(parsedGpx.getTracks().get(j-1).getTrackSegments().get(0).getTrackPoints().size()-1).getLongitude() < 0.00002 &&
                                lon - parsedGpx.getTracks().get(j-1).getTrackSegments().get(0).getTrackPoints().get(parsedGpx.getTracks().get(j-1).getTrackSegments().get(0).getTrackPoints().size()-1).getLongitude() > -0.00002)){
                            System.out.println("======================"+ i + "번째 위치값 continue;");
                            continue;
                        }
                    }
                    */
                    polyline.addPoint(MapPoint.mapPointWithGeoCoord(lat, lon));
                }
            }
            mapView.addPolyline(polyline);
        } catch (IOException | XmlPullParserException e) {
            // do something with this exception
            e.printStackTrace();
        }
        if (parsedGpx == null) {
            // error parsing track
        } else {
            // do something with the parsed track
            // see included example app and tests
        }
        return rootView;
    }

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
