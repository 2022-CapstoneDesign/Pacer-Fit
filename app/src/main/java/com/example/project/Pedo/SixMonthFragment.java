package com.example.project.Pedo;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Formatter.OneMonthXAxisValueFormatter;
import com.example.project.Formatter.SixMonthXAxisValueFormatter;
import com.example.project.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SixMonthFragment extends Fragment {
    private BarChart barChart; // 만보기 6달 막대그래프
    RecyclerView recycler_view;
    SixMonthRecordAdapter adapter;

    private TextView date_sixmonthPedo;
    private TextView totalTime_sixmonthPedo;
    private TextView step_sixmonthPedo;
    private TextView pedo_avg_time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.pedo_six_months_fragment, container, false);

        recycler_view = v.findViewById(R.id.recycler_view);

        date_sixmonthPedo = v.findViewById(R.id.date_sixmonthPedo);
        totalTime_sixmonthPedo = v.findViewById(R.id.totalTime_sixmonthPedo);
        step_sixmonthPedo = v.findViewById(R.id.step_sixmonthPedo);
        pedo_avg_time = v.findViewById(R.id.pedo_avg_time);

        // <--- 막대 그래프 --->
        barChart = v.findViewById(R.id.pedo_sixmonth_barchart);
        ArrayList<Float> barChartValues = new ArrayList<>();

        // ***** 이 곳에서 오늘의 만보기 기록 DB 값을 표시합니다 *****
        setTodayRecord("2022/4/22 ~ 2022/4/28", "2시간 6분", "2,351걸음");
        setAvgTime();
        setRecyclerView();

        // 최근 7일의 운동량 값 받아오기 -> DB 값으로 추후에 수정
        for (int i = 0; i < 24; i++) {
            float rand = (float) Math.round(new Random().nextFloat() * 105000);
            //Log.d("RAND", String.valueOf(rand));
            barChartValues.add(rand); // 0 ~ 105,000 사이의 랜덤값
        }

        barchartConfigureAppearance();
        BarData barChartData = createBarchartData(barChartValues);
        barChart.setData(barChartData); // BarData 전달
        barChart.invalidate(); // BarChart 갱신해 데이터 표시

        return v;
    }

    private void setAvgTime() {
        // 이곳에 DB에서 불러온 운동시간들의 평균 구하는 알고리즘 작성... 추후에 추가
        int hours = 10;
        int minutes = 52;
        pedo_avg_time.setText(hours + "시간 " + minutes + "분");
    }

    private void setTodayRecord(String date, String totalTime, String step) {
        date_sixmonthPedo.setText(date);
        totalTime_sixmonthPedo.setText(totalTime);
        step_sixmonthPedo.setText(step);
    }

    private void setRecyclerView() {
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SixMonthRecordAdapter(getContext(), getList());
        recycler_view.setAdapter(adapter);
    }

    private List<SixMonthRecordModel> getList() {
        List<SixMonthRecordModel> record_list = new ArrayList<>();
        // ***** 이 곳에서 일주일 만보기 기록 DB 값을 표시합니다(오늘 기록 제외) *****
        for (int i = 1; i < 24*7; i+=7) {
            record_list.add(new SixMonthRecordModel("2022/4/" + 1 + " ~ 2022/4/" + (1 + 6), "40분", "1,218걸음"));
        }

        return record_list;
    }

    // 막대그래프 각종 설정
    private void barchartConfigureAppearance() {
        barChart.setTouchEnabled(false); // 터치 유무
        barChart.setPinchZoom(false); // 두 손가락으로 줌인,줌아웃 설정
        barChart.setDrawBarShadow(false); // 그래프의 그림자
        barChart.setDrawGridBackground(false); // 격자무늬 유무
        barChart.getLegend().setEnabled(false); // legend는 차트의 범례
        barChart.getDescription().setEnabled(false); // 우측 하단의 DescriptionLabel 삭제
        barChart.animateY(1500); // 밑에서부터 올라오는 애니메이션 적용
        barChart.animateX(1500); // 왼쪽-오른쪽 방향의 애니메이션 적용
        barChart.setExtraBottomOffset(5f); // X축 글자 깨짐 방지
        //barChart.setDescription();
        //barChart.setExtraOffsets(10f, 0f, 40f, 0f);

        // x축 설정(막대그래프 기준 아래쪽)
        XAxis xAxis = barChart.getXAxis();
        xAxis.setAxisMaximum(24f);
        xAxis.setDrawAxisLine(true); // 축 그리기 설정
        xAxis.setAxisLineWidth(1.5f);
        xAxis.setAxisLineColor(Color.parseColor("#5e5b5f")); // X축 색 설정
        xAxis.setGranularity(4f); // 간격 설정(표시되는 값)
        xAxis.setTextSize(13f);
        xAxis.setTextColor(Color.parseColor("#909090"));
        xAxis.setDrawGridLines(false); // 격자
        //xAxis.setGridLineWidth(25f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X축 데이터 표시 위치
        xAxis.setValueFormatter(new SixMonthXAxisValueFormatter()); // X축에 요일 표시
        // X축 폰트 설정
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/nanumsquareroundeb.ttf");
        xAxis.setTypeface(tf);

        // y축 설정(막대그래프 기준 왼쪽)
        YAxis axisLeft = barChart.getAxisLeft();
        axisLeft.setAxisMaximum(105001f); // y축 최대값 설정
        axisLeft.setAxisMinimum(0f); // y축 최소값 설정
        axisLeft.setDrawLabels(false); // 값 표기 설정
        axisLeft.setDrawGridLines(false); // 격자
        axisLeft.setDrawAxisLine(false); // 축 그리기 설정
        //axisLeft.setAxisLineColor(Color.parseColor("#FFFFFFFF")); // 축 색깔 설정
        //axisLeft.setLabelCo

        // (막대그래프 기준 오른쪽)
        YAxis axisRight = barChart.getAxisRight();
        axisRight.setDrawLabels(false); // 값 표기 설정
        axisRight.setDrawGridLines(false); // 격자
        axisRight.setDrawAxisLine(false); // 축 그리기 설정
        //axisRight.setEnabled(false); // 오른쪽 y축을 안 보이게 해줌
    }

    // 이 함수에서 생성된 BarData를 실제 BarData 객체에 전달하고 BarChart를 갱신해 데이터를 표시
    private BarData createBarchartData(ArrayList<Float> chartValues) {
        // 1. [BarEntry] BarChart에 표시될 데이터 값 생성
        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            float x = i;
            float y = chartValues.get(i);
            values.add(new BarEntry(x, y));
        }

        // 2. [BarDataSet] 단순 데이터를 막대 모양으로 표시, BarChart의 막대 커스텀
        BarDataSet set = new BarDataSet(values, "막대그래프");
        set.setDrawIcons(true);
        set.setDrawValues(false); // 막대 위에 값 표시 설정
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/nanumsquareroundeb.ttf");
        set.setValueTypeface(tf);
        //set.setValueTextSize(30f); // 막대 위에 값 크기 설정.. 적용 안됨
        set.setValueTextColor(Color.parseColor("#E6ADA3")); // 막대 위에 값 색 설정
        set.setColor(Color.parseColor("#EB3314")); // 색상 설정(빨강)

        // 3. [BarData] 보여질 데이터 구성
        BarData data = new BarData(set);
        data.setBarWidth(0.6f);
        data.setValueTextSize(15);

        return data;
    }
}