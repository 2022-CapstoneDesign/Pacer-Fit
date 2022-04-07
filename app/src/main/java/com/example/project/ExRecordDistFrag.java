package com.example.project;

import static java.lang.Math.round;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;


public class ExRecordDistFrag extends Fragment implements View.OnClickListener {

    private ImageButton btn1;
    private ImageButton btn2;
    private ImageButton btn3;
    private ImageButton btn4;
    private ImageButton btn5;
    private LineChart lineChart;
    private ListView listView;
    private TextView date;
    private ArrayList<String> arrayList = new ArrayList<>();
    final String[] weekdays = {"일", "월", "화", "수", "목", "금", "토"};

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_dist, container, false);

        btn1 = view.findViewById(R.id.imageButton5);
        btn2 = view.findViewById(R.id.imageButton6);
        btn3 = view.findViewById(R.id.imageButton7);
        btn4 = view.findViewById(R.id.imageButton8);
        btn5 = view.findViewById(R.id.imageButton9);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);

        lineChart = view.findViewById(R.id.recordLineChart);
        LineChartManager lineChartManager = new LineChartManager(getContext(), lineChart);
        lineChart = lineChartManager.getLineChart();
        lineChart.invalidate();

        listView = view.findViewById(R.id.listview);
        ArrayList<ListData> listViewData = new ArrayList<>();

        for (int i = 0; i < 7; ++i) {
            ListData listData = new ListData();
            if (i == 0) {
                listData.date = "오늘(" + weekdays[i] + ")";
            } else
                listData.date = weekdays[i];
            listData.ste = "오후 08:05 - 오후 10:11";
            int time = (int) (Math.random()*500);
            listData.tt = time/60 + "시간 " + time%60 + "분";
            listData.td = round(Math.random()*100)  + "km";
            listViewData.add(listData);
        }
        ListAdapter oAdapter = new CustomListView(listViewData);
        listView.setAdapter(oAdapter);

        date = view.findViewById(R.id.date_tv);

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.imageButton5:
                btn1.setImageResource(R.drawable.dist_week_over);
                btn2.setImageResource(R.drawable.dist_mon);
                btn3.setImageResource(R.drawable.dist_thd_mon);
                btn4.setImageResource(R.drawable.dist_six_mon);
                btn5.setImageResource(R.drawable.dist_year);
                WeekChart();
                break;
            case R.id.imageButton6:
                btn1.setImageResource(R.drawable.dist_week);
                btn2.setImageResource(R.drawable.dist_mon_over);
                btn3.setImageResource(R.drawable.dist_thd_mon);
                btn4.setImageResource(R.drawable.dist_six_mon);
                btn5.setImageResource(R.drawable.dist_year);
                MonthChart();
                break;
            case R.id.imageButton7:
                btn1.setImageResource(R.drawable.dist_week);
                btn2.setImageResource(R.drawable.dist_mon);
                btn3.setImageResource(R.drawable.dist_thd_mon_over);
                btn4.setImageResource(R.drawable.dist_six_mon);
                btn5.setImageResource(R.drawable.dist_year);
                ThdMonthChart();
                break;
            case R.id.imageButton8:
                btn1.setImageResource(R.drawable.dist_week);
                btn2.setImageResource(R.drawable.dist_mon);
                btn3.setImageResource(R.drawable.dist_thd_mon);
                btn4.setImageResource(R.drawable.dist_six_mon_over);
                btn5.setImageResource(R.drawable.dist_year);
                SixMonthChart();
                break;
            case R.id.imageButton9:
                btn1.setImageResource(R.drawable.dist_week);
                btn2.setImageResource(R.drawable.dist_mon);
                btn3.setImageResource(R.drawable.dist_thd_mon);
                btn4.setImageResource(R.drawable.dist_six_mon);
                btn5.setImageResource(R.drawable.dist_year_over);
                YearChart();
                break;
        }
    }

    private void YearChart() {

        date.setText("1년");
    }

    private void SixMonthChart() {

        date.setText("6개월");
    }

    private void ThdMonthChart() {

        date.setText("3개월");
    }

    private void MonthChart() {
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setAxisMaximum(30f);
        xAxis.setDrawAxisLine(false); // 축 그리기 설정
        xAxis.setGranularity(5f); // 간격 설정(표시되는 값)
        xAxis.setTextSize(13f);
        xAxis.setTextColor(Color.parseColor("#909090"));
        xAxis.setDrawGridLines(false); // 격자
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X축 데이터 표시 위치
        xAxis.setValueFormatter(new MyXAxisValueFormatter());
        lineChart.invalidate();
        date.setText("30일");
    }

    private void WeekChart() {
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setAxisMaximum(6f);
        xAxis.setDrawAxisLine(false); // 축 그리기 설정
        xAxis.setGranularity(1f); // 간격 설정(표시되는 값)
        xAxis.setLabelCount(7);
        xAxis.setTextSize(13f);
        xAxis.setTextColor(Color.parseColor("#909090"));
        xAxis.setDrawGridLines(false); // 격자
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X축 데이터 표시 위치
        xAxis.setValueFormatter(new IndexAxisValueFormatter(weekdays));
        lineChart.invalidate();
        date.setText("7일");

    }

}
