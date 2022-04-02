package com.example.project.Map;

import android.content.Context;
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
import net.daum.mf.map.api.MapView;

import java.util.Map;

public class MapFragment extends Fragment {

    public MapView mapView;
    private OnConnectListener onConnectListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = new MapView(getActivity()); //실제 핸드폰으로 디코딩해야 지도가 나옵니다.
        ViewGroup mapViewContainer = (ViewGroup) rootView.findViewById(R.id.mapView);
        mapViewContainer.addView(mapView);
        onConnectListener.onConnect(mapView);

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
