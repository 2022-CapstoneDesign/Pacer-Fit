package com.example.project.Formatter;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OneWeekXAxisValueFormatter extends IndexAxisValueFormatter {
    @Override
    public String getFormattedValue(float value) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -6 + (int) value);
        DateFormat df = new SimpleDateFormat("EE");
        return df.format(cal.getTime());

//        float dateNum = 29 - value;
//        long now = System.currentTimeMillis();
//        Date date = new Date(now);
    }
}
