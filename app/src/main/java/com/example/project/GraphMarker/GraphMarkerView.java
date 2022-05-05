package com.example.project.GraphMarker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.example.project.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GraphMarkerView extends MarkerView {

    private TextView tvContent;
    private TextView markerDate;
    private View markerbubble;

    private String type;
    private int period;
    private String periodTxt;


    public GraphMarkerView(Context context, int layoutResource, String type, int period) {
        super(context, layoutResource);

        tvContent = findViewById(R.id.tvContent);
        markerDate = findViewById(R.id.markerDate);
        markerbubble = findViewById(R.id.markerbubble);
        this.period = period;
        this.type = type;

        if (period == 6) { // 6개월 마커
            markerbubble.setBackgroundResource(R.drawable.speechbubble_gray_large);
            markerbubble.getLayoutParams().width = 330;
        }
        else { // 30일, 1년 마커
            markerbubble.setBackgroundResource(R.drawable.speechbubble_gray);
            markerbubble.getLayoutParams().width = 180;
        }

        if (type.equals("pedo"))
            markerDate.setTextColor(Color.parseColor("#EAB7B7"));
        else if (type.equals("dist"))
            markerDate.setTextColor(Color.parseColor("#B7EAC4"));


    }

    @Override
    public MPPointF getOffset() {
        //return super.getOffset();
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        int value = Integer.parseInt(Utils.formatNumber(e.getX(), 0, true));
        SimpleDateFormat df;

        if (period == 30) { // 30일 날짜 표현
            // 날짜 받아오기
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, -30 + (int) value);

            df = new SimpleDateFormat("M월 d일");
            periodTxt = df.format(cal.getTime());
            //df.format(cal.getTime());
        }
        else if (period == 6) { // 6개월 날짜 표현
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, (-23 + (int)value)*7); // 해당 주로 설정
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 해당 주의 월요일로 설정

            Calendar cal2 = Calendar.getInstance();
            cal2.add(Calendar.DATE, (-23 + (int)value)*7); // 해당 주로 설정
            cal2.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY); // 해당 주의 월요일로 설정
            cal2.add(Calendar.DATE, 1); // 일주일 끝이 토요일이라 토요일에 하루를 더해서 일요일로 만든다

            df = new SimpleDateFormat("M월 d일");
            String p1 = df.format(cal.getTime());
            String p2 = df.format(cal2.getTime());
            periodTxt = p1 + " ~ " + p2;
        }
        else { // 1년 날짜 표현
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -11 + (int)value);

            df = new SimpleDateFormat("yy년 M월");
            periodTxt = df.format(cal.getTime());
        }

        if (e instanceof CandleEntry) { // 주식차트 종류일 때 인듯.. 없어도 되긴 함
            CandleEntry ce = (CandleEntry) e;
            tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
        } else { // 일반 차트
            markerDate.setText("" + periodTxt);

            // 천단위 구분 적용 (,)
            DecimalFormat df1 = new DecimalFormat("#,##0");

            //String val = df1.format(Integer.parseInt(Utils.formatNumber(e.getY(), 0, false)));
            String val;
            if (type.equals("pedo")) {
                val = df1.format(e.getY());
                tvContent.setText("" + val + "걸음");
            }
            else {
                val = String.valueOf(e.getY());
                tvContent.setText("" + val + "km");
            }
        }

        super.refreshContent(e, highlight);
    }


}
