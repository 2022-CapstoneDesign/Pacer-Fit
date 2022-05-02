package com.example.project.Formatter;

import android.util.Log;

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

        // x축 값이 끝에서부터 나오게 설정.
        // 맨 끝값 : 이번 주의 월요일 날짜 / 그 전값 : 4주 전의 월요일 날짜 / ...
        // => 총 6개의 x값이 표시됨
        if (value % 4.0f == 3.0f) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, (-23 + (int)value)*7);
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            SimpleDateFormat df = new SimpleDateFormat("M.d");
            return df.format(cal.getTime());
        }
        else {
            return "";
        }

    }
}
