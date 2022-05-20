package com.example.project.Map;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.project.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecordFragment extends Fragment {
    ImageButton helpBtn;
    Button startStopBtn;
    CircleImageView detailBack;
    TextView distKm;
    TextView distKmText;
    TextView distTime;
    TextView distTimeText;
    TextView distCal;
    TextView distCalText;
    Handler h;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_record_fragment, container, false);
        helpBtn = view.findViewById(R.id.dist_help_btn);
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View map) {
                DetailBottomFragment helpFragment = new DetailBottomFragment(getActivity().getApplicationContext());
                helpFragment.show(getActivity().getSupportFragmentManager(), helpFragment.getTag());
            }
        });
        distKm = view.findViewById(R.id.dist_km);
        distKmText = view.findViewById(R.id.dist_km_text);
        distTime = view.findViewById(R.id.dist_time);
        distTimeText = view.findViewById(R.id.dist_time_text);
        distCal = view.findViewById(R.id.dist_cal);
        distCalText = view.findViewById(R.id.dist_cal_text);

        detailBack = view.findViewById(R.id.dist_detail_img);
        h = new Handler();
        // 초기 투명도 설정
        setTextAlpha(1f);
        // 어두운 배경
        detailBack.setImageResource(R.drawable.exer_dist_background02);

        return view;
    }

    // 원 내부에 textview들 투명도 설정
    public void setTextAlpha(float values) {
        distKm.setAlpha(values);
        distKmText.setAlpha(values);
        distTime.setAlpha(values);
        distTimeText.setAlpha(values);
        distCal.setAlpha(values);
        distCalText.setAlpha(values);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private class splashhandler implements Runnable {
        @Override
        public void run() {
            startStopBtn.setEnabled(true); // 클릭 유효화
        }
    }
}
