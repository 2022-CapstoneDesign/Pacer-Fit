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

import com.example.project.Formatter.SixMonthXAxisValueFormatter;
import com.example.project.GraphMarker.GraphMarkerView;
import com.example.project.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class SixMonthFragment extends Fragment {
    private LineChart lineChart; // 거리 6개월 라인그래프
    RecyclerView recycler_view;
    SixMonthRecordAdapter adapter;

    private TextView date_sixmonthDist;
    private TextView totalTime_sixmonthDist;
    private TextView km_sixmonthDist;

    private TextView dist_avg_time;
    DistRecordData data = new DistRecordData();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.dist_six_months_fragment, container, false);

        recycler_view = v.findViewById(R.id.recycler_view);
        lineChart = v.findViewById(R.id.dist_sixmonth_linechart);

        date_sixmonthDist = v.findViewById(R.id.date_sixmonthDist);
        totalTime_sixmonthDist = v.findViewById(R.id.totalTime_sixmonthDist);
        km_sixmonthDist = v.findViewById(R.id.km_sixmonthDist);
        dist_avg_time = v.findViewById(R.id.dist_avg_time);

        // <--- 테이블 --->
        setRecyclerView();
        // ***** 이 곳에서 제일 최근 일주일 거리 기록 DB 값을 표시합니다 *****
        // 일주일 기준 -> 시작 : 월요일, 끝 : 일요일
        int time = Integer.parseInt(data.KmRecord180_time[0]);
        int sec = time % 60;
        int min = time / 60 % 60;
        int hour = time / 3600;

        float value_lastweek = 0;
        //오늘 일자 구하기
        Calendar cal_lastweek = Calendar.getInstance();
        cal_lastweek.add(Calendar.DATE, ((int)value_lastweek)*7);
        cal_lastweek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        DateFormat df_lastweek = new SimpleDateFormat("y/M/d");

        float value = 1;
        //오늘 일자 구하기
        Calendar cal_week = Calendar.getInstance();
        cal_week.add(Calendar.DATE, ((int)value)*7);
        cal_week.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        SimpleDateFormat df_week = new SimpleDateFormat("y/M/d");

        //오늘의 만보기 기록
        if(hour!=0) {
            setTodayRecord(df_lastweek.format(cal_lastweek.getTime()) + " ~ " + df_week.format(cal_week.getTime()),
                    hour + "시간" + min + "분",
                    data.KmRecord180_km[0] + "km");
        }
        else {
            setTodayRecord(df_lastweek.format(cal_lastweek.getTime()) + " ~ " + df_week.format(cal_week.getTime()),
                    min + "분",
                    data.KmRecord180_km[0] + "km");
        }
        setAvgTime();

        // <--- 라인 그래프 --->
        ArrayList<Float> lineChartValues = new ArrayList<>();
        // 최근 6개월(주차별)의 운동량 값 받아오기 -> DB 값으로 추후에 수정
        for (int i = 23; i >= 0; i--) {
            //float rand = (float) Math.round(new Random().nextFloat() * 105000);
            float rand = Float.parseFloat(data.KmRecord180_km[i]);
            //Log.d("RAND", String.valueOf(rand));
            lineChartValues.add(rand); // 0 ~ 700 사이의 랜덤값
        }

        linechartConfigureAppearance();
        LineData lineChartData = createLinechartData(lineChartValues);
        lineChart.setData(lineChartData); // lineData 전달
        lineChart.invalidate(); // lineChart 갱신해 데이터 표시

        return v;
    } // onCreateView

    private void setAvgTime() {
        // 이곳에 DB에서 불러온 운동시간들의 평균 구하는 알고리즘 작성... 추후에 추가
        int time = 0;
        for(int i=0; i<24; i++)
            time += Integer.parseInt(data.KmRecord180_time[i]);
        time /= data.KmRecord180_time.length;
        int minutes = time / 60 % 60;
        int hours = time / 3600;
        if(hours != 0)
            dist_avg_time.setText(hours + "시간 " + minutes + "분");
        else
            dist_avg_time.setText(minutes + "분");
    }

    private void setTodayRecord(String date, String totalTime, String km) {
        date_sixmonthDist.setText(date);
        totalTime_sixmonthDist.setText(totalTime);
        km_sixmonthDist.setText(km);
    }

    private void setRecyclerView() {
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SixMonthRecordAdapter(getContext(), getList());
        recycler_view.setAdapter(adapter);
    }

    private List<SixMonthRecordModel> getList() {
        // <--- 테이블 --->
        List<SixMonthRecordModel> record_list = new ArrayList<>();
        // ***** 이 곳에서 6개월 거리 기록 DB 값을 표시합니다(일주일 단위로, 이번주 기록 제외) *****
        // 일주일 기준 -> 시작 : 월요일, 끝 : 일요일
        float value_lastweek = 0;
        float value = 1;

        for (int i = 1; i < 24; i++) {
            int time = Integer.parseInt(data.KmRecord180_time[i]);
            int sec = time % 60;
            int min = time / 60 % 60;
            int hour = time / 3600;

            value--;
            value_lastweek--;

            //오늘 일자 구하기
            Calendar cal_lastweek = Calendar.getInstance();
            cal_lastweek.add(Calendar.DATE, ((int)value_lastweek)*7);
            cal_lastweek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            DateFormat df_lastweek = new SimpleDateFormat("y/M/d");

            //오늘 일자 구하기
            Calendar cal_week = Calendar.getInstance();
            cal_week.add(Calendar.DATE, ((int)value)*7);
            cal_week.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            SimpleDateFormat df_week = new SimpleDateFormat("y/M/d");

            //record_list.add(new SixMonthRecordModel("2022/4/" + 1 + " ~ 2022/4/" + (1 + 6), "40분", "1,218걸음"));
            if(hour!=0)
                record_list.add(new SixMonthRecordModel(df_lastweek.format(cal_lastweek.getTime()) + " ~ " + df_week.format(cal_week.getTime()),
                        hour + "시간" + min + "분",
                        data.KmRecord180_km[i]+"km"));
            else
                record_list.add(new SixMonthRecordModel(df_lastweek.format(cal_lastweek.getTime()) + " ~ " + df_week.format(cal_week.getTime()),
                        min + "분",
                        data.KmRecord180_km[i]+"km"));
        }
        return record_list;
    }

    // 꺾은선그래프 각종 설정
    private void linechartConfigureAppearance() {
        lineChart.setTouchEnabled(true); // 터치 유무
        lineChart.setPinchZoom(false); // 두 손가락으로 줌인,줌아웃 설정
        lineChart.setDrawGridBackground(false); // 격자무늬 유무
        lineChart.getLegend().setEnabled(false); // legend는 차트의 범례
        lineChart.getDescription().setEnabled(false); // 우측 하단의 DescriptionLabel 삭제
        //lineChart.animateY(1500); // 밑에서부터 올라오는 애니메이션 적용
        lineChart.animateX(1000); // 왼쪽-오른쪽 방향의 애니메이션 적용
        // 값 클릭 시 마커뷰 보이도록 설정
        GraphMarkerView marker = new GraphMarkerView(getContext(), R.layout.graph_marker_view, "dist", 6);
        marker.setChartView(lineChart);
        marker.setPadding(0, 0, 0, 5); // 라인그래프는 default가 마커가 딱 붙어서 나옴
        lineChart.setMarker(marker);

        // x축 설정(꺾은선그래프 기준 아래쪽)
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setAxisMinimum(-0.5f); // 라인그래프만 x축 좌측 여유 공간 필요
        xAxis.setAxisMaximum(23.5f); // x : 0, 1, ... , 23 -> 24개
        //xAxis.setLabelCount(7, true);
        //xAxis.setAvoidFirstLastClipping(true);
        xAxis.setDrawAxisLine(true); // 축 그리기 설정
        xAxis.setLabelCount(24); // 이걸 써야 setGranularity가 작동함
        xAxis.setGranularity(1f); // 간격 설정(표시되는 값) -> SixMonthXAxisValueFormatter.java에서 값 번갈아서 나오게 커스텀
        xAxis.setTextSize(13f);
        xAxis.setAxisLineWidth(1.5f);
        xAxis.setAxisLineColor(Color.parseColor("#5e5b5f")); // X축 색 설정
        xAxis.setTextColor(Color.parseColor("#909090"));
        xAxis.setDrawGridLines(false); // 격자
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X축 데이터 표시 위치
        xAxis.setValueFormatter(new SixMonthXAxisValueFormatter());
        // X축 폰트 설정
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/nanumsquareroundeb.ttf");
        xAxis.setTypeface(tf);
        //xAxis.setSpaceMax(200f); // 차트 맨 오른쪽 간격 띄우기

        YAxis yAxisLeft = lineChart.getAxisLeft();
        Float max = Float.parseFloat(data.km_max_180);
        max += max/10;
        max += max/3;
        yAxisLeft.setAxisMaximum(max); // y축 최대값 설정
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
        for (int i = 0; i < 24; i++) {
            float x = i;
            float y = chartValues.get(i);
            values.add(new Entry(x, y));
        }

        // 2. [LineDataSet] 단순 데이터를 꺾은선 모양으로 표시, LineChart의 막대 커스텀
        LineDataSet set = new LineDataSet(values, "꺾은선그래프");
        set.setDrawIcons(true);
        set.setDrawValues(false);
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
        set.setDrawHorizontalHighlightIndicator(false); // 마커 나올 때 강조 선 안 나오게
        set.setDrawVerticalHighlightIndicator(false); // 마커 나올 때 강조 선 안 나오게

        // 3. [LineData] 보여질 데이터 구성
        LineData data = new LineData(set);
        data.setValueTextSize(15);

        return data;
    }
}