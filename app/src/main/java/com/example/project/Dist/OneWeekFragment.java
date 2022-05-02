package com.example.project.Dist;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Formatter.OneWeekXAxisValueFormatter;
import com.example.project.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OneWeekFragment extends Fragment {
    private LineChart lineChart; // 거리 7일 라인그래프
    RecyclerView recycler_view;
    OneWeekRecordAdapter adapter;

    private TextView day_today_oneweekDist;
    private TextView date_today_oneweekDist;
    private TextView totalTime_today_oneweekDist;
    private TextView km_today_oneweekDist;

    private TextView dist_avg_time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.dist_one_week_fragment, container, false);

        recycler_view = v.findViewById(R.id.recycler_view);
        lineChart = v.findViewById(R.id.dist_oneweek_linechart);

        day_today_oneweekDist = v.findViewById(R.id.day_today_oneweekDist);
        date_today_oneweekDist = v.findViewById(R.id.date_today_oneweekDist);
        totalTime_today_oneweekDist = v.findViewById(R.id.totalTime_today_oneweekDist);
        km_today_oneweekDist = v.findViewById(R.id.km_today_oneweekDist);
        dist_avg_time = v.findViewById(R.id.dist_avg_time);

        // <--- 테이블 --->
        setRecyclerView();
        // ***** 이 곳에서 오늘의 거리 기록 DB 값을 표시합니다 *****
        setTodayRecord("오늘(금)", "2022/4/18", "2시간 6분", "24km");
        setAvgTime();

        // <--- 라인 그래프 --->
        ArrayList<Float> lineChartValues = new ArrayList<>();
        // 최근 7일의 운동량 값 받아오기 -> DB 값으로 추후에 수정
        for (int i = 0; i < 7; i++) {
            float rand = (float) Math.round(new Random().nextFloat() * 100);
            //Log.d("RAND", String.valueOf(rand));
            lineChartValues.add(rand); // 0 ~ 15,000 사이의 랜덤값
        }

        linechartConfigureAppearance();
        LineData lineChartData = createLinechartData(lineChartValues);
        lineChart.setData(lineChartData); // BarData 전달
        lineChart.invalidate(); // BarChart 갱신해 데이터 표시

        return v;
    } // onCreateView

    private void setAvgTime() {
        // 이곳에 DB에서 불러온 운동시간들의 평균 구하는 알고리즘 작성... 추후에 추가
        int hours = 1;
        int minutes = 52;
        dist_avg_time.setText(hours + "시간 " + minutes + "분");
    }

    private void setTodayRecord(String day, String date, String totalTime, String km) {
        day_today_oneweekDist.setText(day);
        date_today_oneweekDist.setText(date);
        totalTime_today_oneweekDist.setText(totalTime);
        km_today_oneweekDist.setText(km);
    }

    private void setRecyclerView() {
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OneWeekRecordAdapter(getContext(), getList());
        recycler_view.setAdapter(adapter);
    }

    private List<OneWeekRecordModel> getList() {
        // <--- 테이블 --->
        List<OneWeekRecordModel> record_list = new ArrayList<>();
        // ***** 이 곳에서 일주일 거리 기록 DB 값을 표시합니다(하루단위로, 오늘 기록 제외) *****
        record_list.add(new OneWeekRecordModel("목", "2022/4/17", "40분", "8km"));
        record_list.add(new OneWeekRecordModel("수", "2022/4/16", "2시간 33분", "31km"));
        record_list.add(new OneWeekRecordModel("화", "2022/4/15", "4시간 7분", "58km"));
        record_list.add(new OneWeekRecordModel("월", "2022/4/14", "1시간 22분", "13km"));
        record_list.add(new OneWeekRecordModel("일", "2022/4/13", "41분", "8.5km"));
        record_list.add(new OneWeekRecordModel("토", "2022/4/12", "2시간 8분", "12.8km"));

        return record_list;
    }

    // 꺾은선그래프 각종 설정
    private void linechartConfigureAppearance() {
        lineChart.setTouchEnabled(false);
        lineChart.setTouchEnabled(false); // 터치 유무
        lineChart.setPinchZoom(false); // 두 손가락으로 줌인,줌아웃 설정
        lineChart.setDrawGridBackground(false); // 격자무늬 유무
        lineChart.getLegend().setEnabled(false); // legend는 차트의 범례
        lineChart.getDescription().setEnabled(false); // 우측 하단의 DescriptionLabel 삭제
        //lineChart.animateY(1500); // 밑에서부터 올라오는 애니메이션 적용
        lineChart.animateX(500); // 왼쪽-오른쪽 방향의 애니메이션 적용
        lineChart.setExtraBottomOffset(5f); // X축 글자 깨짐 방지

        // x축 설정(꺾은선그래프 기준 아래쪽)
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setAxisMinimum(-0.5f); // 라인그래프만 x축 좌측 여유 공간 필요
        xAxis.setAxisMaximum(6.5f); // x : 0, 1, ... , 6 -> 7개
        xAxis.setDrawAxisLine(true); // 축 그리기 설정
        xAxis.setLabelCount(7); // 이걸 써야 setGranularity가 작동함
        xAxis.setGranularity(1f); // 간격 설정(표시되는 값)
        xAxis.setAxisLineWidth(1.5f);
        xAxis.setAxisLineColor(Color.parseColor("#5e5b5f")); // X축 색 설정
        xAxis.setTextSize(13f);
        xAxis.setTextColor(Color.parseColor("#909090"));
        xAxis.setDrawGridLines(false); // 격자
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X축 데이터 표시 위치
        xAxis.setValueFormatter(new OneWeekXAxisValueFormatter());
        // X축 폰트 설정
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/nanumsquareroundeb.ttf");
        xAxis.setTypeface(tf);

        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setAxisMaximum(101f); // y축 최대값 설정
        yAxisLeft.setAxisMinimum(0f); // y축 최소값 설정
        yAxisLeft.setDrawLabels(false); // 값 표기 설정
        yAxisLeft.setDrawGridLines(false); // 격자
        yAxisLeft.setDrawAxisLine(false); // 축 그리기 설정

        // (꺾은선그래프 기준 오른쪽)
        YAxis axisRight = lineChart.getAxisRight();
        axisRight.setDrawLabels(false); // 값 표기 설정
        axisRight.setDrawGridLines(false); // 격자
        axisRight.setDrawAxisLine(false); // 축 그리기 설정
    }

    // 이 함수에서 생성된 LineData를 실제 LineData 객체에 전달하고 LineChart를 갱신해 데이터를 표시
    private LineData createLinechartData(ArrayList<Float> chartValues) {
        // 1. [Entry] LineChart에 표시될 데이터 값 생성
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            float x = i;
            float y = chartValues.get(i);
            values.add(new Entry(x, y));
        }

        // 2. [LineDataSet] 단순 데이터를 꺾은선 모양으로 표시, LineChart의 막대 커스텀
        LineDataSet set = new LineDataSet(values, "꺾은선그래프");
        set.setDrawIcons(true);
        set.setDrawValues(true);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/nanumsquareroundeb.ttf");
        set.setValueTypeface(tf);
        set.setValueTextColor(Color.parseColor("#90DEC1")); // 라인 위에 값 색 설정
        set.setColor(Color.parseColor("#00C87D")); // 색상 설정(초록)
        int green = ContextCompat.getColor(getContext(), R.color.green_project);
        int gray = ContextCompat.getColor(getContext(), R.color.gray_project);
        set.setFillColor(green);
        set.setColor(green);
        set.setDrawCircleHole(true);
        set.setCircleColor(green); // 바깥 원 색
        set.setCircleHoleColor(gray); // 안쪽 원 색
        set.setLineWidth(1.5f); // 선 두께
        set.setCircleSize(4f);
        set.setDrawCircles(true); //선 둥글게 만들기
        set.setDrawFilled(false); //그래프 밑부분 색칠X

        // 3. [LineData] 보여질 데이터 구성
        LineData data = new LineData(set);
        data.setValueTextSize(15);

        return data;
    }
}