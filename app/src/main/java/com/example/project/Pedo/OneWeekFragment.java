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

import com.example.project.Formatter.OneWeekXAxisValueFormatter;
import com.example.project.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class OneWeekFragment extends Fragment {
    private BarChart barChart; // 만보기 7일 막대그래프
    RecyclerView recycler_view;
    OneWeekRecordAdapter adapter;

    private TextView day_today_oneweekPedo;
    private TextView date_today_oneweekPedo;
    private TextView totalTime_today_oneweekPedo;
    private TextView step_today_oneweekPedo;
    private TextView pedo_avg_time;

    //외부 class에 데이터 저장후 UI에 뿌려줌
    PedoRecordData data = new PedoRecordData();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.pedo_one_week_fragment, container, false);

        recycler_view = v.findViewById(R.id.recycler_view);

        day_today_oneweekPedo = v.findViewById(R.id.day_today_oneweekPedo);
        date_today_oneweekPedo = v.findViewById(R.id.date_today_oneweekPedo);
        totalTime_today_oneweekPedo = v.findViewById(R.id.totalTime_today_oneweekPedo);
        step_today_oneweekPedo = v.findViewById(R.id.step_today_oneweekPedo);
        pedo_avg_time = v.findViewById(R.id.pedo_avg_time);
        // <--- 막대 그래프 --->
        barChart = v.findViewById(R.id.pedo_oneweek_barchart);
        ArrayList<Float> barChartValues = new ArrayList<>();
        // ***** 이 곳에서 오늘의 만보기 기록 DB 값을 표시합니다 *****
        int time = Integer.parseInt(data.PedoRecord7_time[6]);
        int sec = time % 60;
        int min = time / 60 % 60;
        int hour = time / 3600;

        //value = 1이면 어제..
        float value = 0;
        //오늘 요일 구하기
        Calendar cal_day = Calendar.getInstance();
        cal_day.setTime(new Date());
        cal_day.add(Calendar.DATE, (int) value);
        DateFormat df_day = new SimpleDateFormat("EE");

        //오늘 일자 구하기
        Calendar cal_week = Calendar.getInstance();
        cal_week.setTime(new Date());
        cal_week.add(Calendar.DATE, (int) value);
        DateFormat df_week = new SimpleDateFormat("y/M/d");

        //오늘의 만보기 기록
        if(hour!=0)
            setTodayRecord("오늘("+df_day.format(cal_day.getTime())+")", df_week.format(cal_week.getTime())+"", hour+"시간"+min+"분", data.PedoRecord7_step[6]+"걸음");
        else
            setTodayRecord("오늘("+df_day.format(cal_day.getTime())+")", df_week.format(cal_week.getTime())+"", min+"분", data.PedoRecord7_step[6]+"걸음");

        setAvgTime();
        setRecyclerView();
        data.getPedoRecord7();
        // 최근 7일의 운동량 값 받아오기 -> DB 값으로 추후에 수정
        for (int i = 0; i < 7; i++) {
            //float rand = (float) Math.round(new Random().nextFloat() * 15000);
            float rand = Float.parseFloat(data.PedoRecord7_step[i]);
            //Log.d("RAND", String.valueOf(rand));
            barChartValues.add(rand); // 0 ~ 15,000 사이의 랜덤값
        }

        barchartConfigureAppearance();
        BarData barChartData = createBarchartData(barChartValues);
        barChart.setData(barChartData); // BarData 전달
        barChart.invalidate(); // BarChart 갱신해 데이터 표시

        return v;
    } // onCreateView

    private void setAvgTime() {
        // 이곳에 DB에서 불러온 운동시간들의 평균 구하는 알고리즘 작성... 추후에 추가
        int time = 0;
        for(int i=0; i<6; i++)
            time += Integer.parseInt(data.PedoRecord7_time[i]);
        time /= 6;
        int minutes = time / 60 % 60;
        int hours = time / 3600;
        if(hours != 0)
            pedo_avg_time.setText(hours + "시간 " + minutes + "분");
        else
            pedo_avg_time.setText(minutes + "분");
    }

    private void setTodayRecord(String day, String date, String totalTime, String step) {
        day_today_oneweekPedo.setText(day);
        date_today_oneweekPedo.setText(date);
        totalTime_today_oneweekPedo.setText(totalTime);
        step_today_oneweekPedo.setText(step);
    }

    private void setRecyclerView() {
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OneWeekRecordAdapter(getContext(), getList());
        recycler_view.setAdapter(adapter);
    }

    private List<OneWeekRecordModel> getList() {

        List<OneWeekRecordModel> record_list = new ArrayList<>();
        // ***** 이 곳에서 일주일 만보기 기록 DB 값을 표시합니다(하루 단위로, 오늘 기록 제외) *****
        //value = -1이면 어제..
        float value = 0;
        for(int i=5; i>=0; i--) {
            int time = Integer.parseInt(data.PedoRecord7_time[i]);
            int sec = time % 60;
            int min = time / 60 % 60;
            int hour = time / 3600;

            //오늘 요일 구하기
            value--;

            Calendar cal_day = Calendar.getInstance();
            cal_day.setTime(new Date());
            cal_day.add(Calendar.DATE, (int) value);
            DateFormat df_day = new SimpleDateFormat("EE");

            //오늘 일자 구하기
            Calendar cal_week = Calendar.getInstance();
            cal_week.setTime(new Date());
            cal_week.add(Calendar.DATE, (int) value);
            DateFormat df_week = new SimpleDateFormat("y/M/d");

            if(hour!=0)
                record_list.add(new OneWeekRecordModel(df_day.format(cal_day.getTime())+"",
                        df_week.format(cal_week.getTime())+"",
                        hour+"시간"+min+"분",
                        data.PedoRecord7_step[i]+"걸음"));
            else
                record_list.add(new OneWeekRecordModel(df_day.format(cal_day.getTime())+"",
                        df_week.format(cal_week.getTime())+"",
                        min+"분",
                        data.PedoRecord7_step[i]+"걸음"));
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
        xAxis.setAxisMaximum(6.5f);
        xAxis.setDrawAxisLine(true); // 축 그리기 설정
        xAxis.setAxisLineWidth(1.5f);
        xAxis.setAxisLineColor(Color.parseColor("#5e5b5f")); // X축 색 설정
        xAxis.setGranularity(1f); // 간격 설정(표시되는 값)
        xAxis.setTextSize(13f);
        xAxis.setTextColor(Color.parseColor("#909090"));
        xAxis.setDrawGridLines(false); // 격자
        //xAxis.setGridLineWidth(25f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // X축 데이터 표시 위치
        xAxis.setValueFormatter(new OneWeekXAxisValueFormatter()); // X축에 요일 표시
        // X축 폰트 설정
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/nanumsquareroundeb.ttf");
        xAxis.setTypeface(tf);

        // y축 설정(막대그래프 기준 왼쪽)
        YAxis axisLeft = barChart.getAxisLeft();
        Float max = Float.parseFloat(data.pedo_max_day);
        axisLeft.setAxisMaximum(max); // y축 최대값 설정
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
        for (int i = 0; i < 7; i++) {
            float x = i;
            float y = chartValues.get(i);
            values.add(new BarEntry(x, y));
        }

        // 2. [BarDataSet] 단순 데이터를 막대 모양으로 표시, BarChart의 막대 커스텀
        BarDataSet set = new BarDataSet(values, "막대그래프");
        set.setDrawIcons(true);
        set.setDrawValues(true); // 막대 위에 값 표시 설정
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