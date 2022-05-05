package com.example.project.Pedo;

import android.content.Context;
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

public class PedoGraphMarkerView extends MarkerView {

    private TextView tvContent;
    private TextView markerDate;

    @Override
    public MPPointF getOffset() {
        //return super.getOffset();
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        // 날짜 받아오기
        int value = Integer.parseInt(Utils.formatNumber(e.getX(), 0, true));
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -30 + (int) value);
        DateFormat df = new SimpleDateFormat("M월 d일");
        //df.format(cal.getTime());

        // 천단위 구분 적용 (,)
        DecimalFormat df1 = new DecimalFormat("#,##0");


        if (e instanceof CandleEntry) { // 주식차트 종류일 때 인듯.. 없어도 되긴 함
            CandleEntry ce = (CandleEntry) e;
            tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
        } else { // 일반 차트
            // 걸음수 천단위 구분표 넣기
            String val = df1.format(Integer.parseInt(Utils.formatNumber(e.getY(), 0, false)));
            markerDate.setText("" + df.format(cal.getTime()));
            tvContent.setText("" + val + "걸음");
        }

        super.refreshContent(e, highlight);
    }

    public PedoGraphMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        tvContent = (TextView) findViewById(R.id.tvContent);
        markerDate = (TextView) findViewById(R.id.markerDate);


    }
}
