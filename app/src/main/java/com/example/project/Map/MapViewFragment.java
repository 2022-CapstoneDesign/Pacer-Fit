package com.example.project.Map;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.R;
import com.example.project.Weather.GpsTrackerService;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;

public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    private static String TAG = "MapViewFragment";

    // 네이버 지도
    private MapView mapView;
    private OnConnectListener onConnectListener;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.map_view_fragment,
                container, false);
        mapView = rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        onConnectListener.onConnect(mapView);

        return rootView;
    }

    public interface OnConnectListener{
        void onConnect(MapView mapView);
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
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        //배경 지도 선택
        naverMap.setMapType(NaverMap.MapType.Basic);
        naverMap.setIndoorEnabled(true);

        //건물 표시
        naverMap.setLayerGroupEnabled(naverMap.LAYER_GROUP_BUILDING, true);

        // 현재 좌표로 변경
        double curLat;
        double curLng;
        GpsTrackerService gps = new GpsTrackerService(getActivity().getApplicationContext());
        curLat = gps.getLatitude();
        curLng = gps.getLongitude();

        Log.d(TAG, curLat + ", " + curLng);

        //위치 및 각도 조정
        CameraPosition cameraPosition = new CameraPosition(
                new LatLng(curLat, curLng),         // 위치 지정
                15,                           // 줌 레벨
                20,                             // 기울임 각도
                0                            // 방향
        );
        naverMap.setCameraPosition(cameraPosition);

        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);
        naverMap.setIndoorEnabled(true);

        Log.d(TAG, "onMapReady");
    }
}
