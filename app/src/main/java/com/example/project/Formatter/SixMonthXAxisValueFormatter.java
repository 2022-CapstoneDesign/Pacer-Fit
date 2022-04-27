package com.example.project.Formatter;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SixMonthXAxisValueFormatter extends IndexAxisValueFormatter {
    @Override
    public String getFormattedValue(float value) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -168 + (int) value);
        DateFormat df = new SimpleDateFormat("M.d");
        return df.format(cal.getTime());


//        float dateNum = 29 - value;
//        long now = System.currentTimeMillis();
//        Date date = new Date(now);

    }
}
