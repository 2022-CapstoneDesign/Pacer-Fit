package com.example.project.Formatter;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SixMonthXAxisValueFormatter extends IndexAxisValueFormatter {
    @Override
    public String getFormattedValue(float value) {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(new Date());
//        cal.add(Calendar.DATE, -23 + (int) value);
//        DateFormat df = new SimpleDateFormat("M.d");
//        return df.format(cal.getTime());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, (-24 + (int)value)*7);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        SimpleDateFormat df = new SimpleDateFormat("M.d");
        return df.format(cal.getTime());

    }
}
