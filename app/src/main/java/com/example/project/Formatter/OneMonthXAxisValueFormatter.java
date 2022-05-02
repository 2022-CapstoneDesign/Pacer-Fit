package com.example.project.Formatter;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OneMonthXAxisValueFormatter extends IndexAxisValueFormatter {
    @Override
    public String getFormattedValue(float value) {

        // 오늘 값은 나오게, 5일 간격으로 표시함
        if (value % 5.0f == 0f) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, -30 + (int) value);
            DateFormat df = new SimpleDateFormat("M.d");
            return df.format(cal.getTime());
        }
        else {
            return "";
        }
//        float dateNum = 29 - value;
//        long now = System.currentTimeMillis();
//        Date date = new Date(now);

    }
}
