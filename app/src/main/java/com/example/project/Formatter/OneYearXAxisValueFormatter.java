package com.example.project.Formatter;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class OneYearXAxisValueFormatter extends IndexAxisValueFormatter {
    @Override
    public String getFormattedValue(float value) {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(new Date());
//        cal.add(Calendar.DATE, -23 + (int) value);
//        DateFormat df = new SimpleDateFormat("M.d");
//        return df.format(cal.getTime());

        // 자꾸 끝에 다음달 값이 나와서 수정합니다
        if (value == 12)
            return "";

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -11 + (int)value);
        //cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        SimpleDateFormat df = new SimpleDateFormat("M월");
        return df.format(cal.getTime());

    }
}
