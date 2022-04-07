package com.example.project;

import android.content.Context;
import android.graphics.Color;

import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Random;

public class LineChartManager {

    public LineChart lineChart;
    public LineData lineChartData;
    Context context;
    public static ArrayList<Float> lineChartValues = new ArrayList<>();

    public LineChartManager(Context context, LineChart lineChart) {
        this.context = context;
        this.lineChart = lineChart;
        // 최근 1달의 운동량 값 받아오기 -> DB 값으로 추후에 수정

        if (lineChartValues.size() == 0) {
            for (int i = 0; i < 60; i++) {
                float rand = (float) Math.round(new Random().nextFloat() * 15000);
                //Log.d("RAND", String.valueOf(rand));
                lineChartValues.add(rand); // 0 ~ 15,000 사이의 랜덤값
            }
        }

        linechartConfigureAppearance();
        lineChartData = createLinechartData(lineChartValues, context);
        lineChart.setData(lineChartData); // LineData 전달
    }

    public LineChart getLineChart() {
        return lineChart;
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
        lineChart.animateX(1500); // 왼쪽-오른쪽 방향의 애니메이션 적용

        // x축 설정(꺾은선그래프 기준 아래쪽)
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setAxisMaximum(30f);
        xAxis.setDrawAxisLine(false); // 축 그리기 설정
        xAxis.setGranularity(5f); // 간격 설정(표시되는 값)
        xAxis.setTextSize(13f);
        xAxis.setTextColor(Color.parseColor("#909090"));
        xAxis.setDrawGridLines(false); // 격자
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X축 데이터 표시 위치
        xAxis.setValueFormatter(new MyXAxisValueFormatter());

        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setAxisMaximum(15001f); // y축 최대값 설정
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
    private LineData createLinechartData(ArrayList<Float> chartValues, Context context) {
        // 1. [Entry] LineChart에 표시될 데이터 값 생성
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            float x = i;
            float y = chartValues.get(i);
            values.add(new Entry(x, y));
        }

        // 2. [LineDataSet] 단순 데이터를 꺾은선 모양으로 표시, LineChart의 막대 커스텀
        LineDataSet set = new LineDataSet(values, "꺾은선그래프");
        set.setDrawIcons(false);
        set.setDrawValues(false);
        int green = ContextCompat.getColor(context, R.color.green_project);
        int gray = ContextCompat.getColor(context, R.color.gray_project);
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
