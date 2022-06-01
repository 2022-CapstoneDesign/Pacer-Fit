package com.example.project.Map;

import java.util.Locale;

public class RecordUtility {
    // 포매팅
    public static String formattedResultTime(int time) {
        int hour = time / 3600;
        int min = time / 60 % 60;
        int sec = time % 60;
        return String.format(Locale.KOREA, "%02d:%02d:%02d", hour, min, sec);
    }

    public static String formattedResultDist(double dist) {
        return String.format(Locale.KOREA, "%.2fkm", Math.round(dist * 100) / 100.0);
    }

    public static String formattedResultCal(double cal) {
        return String.format(Locale.KOREA, "%.2fkcal", Math.round(cal * 100) / 100.0);
    }

    public static String formattedResultSpeed(double speed) {
        return String.format(Locale.KOREA, "%.2fkm/h", Math.round(speed * 100) / 100.0);
    }

    public static String formattedCrsHour(String sHour) {
        int time = Integer.parseInt(sHour);
        int hour = time / 60;
        int min = time % 60;
        return String.format(Locale.KOREA, "#%d시간%d분", hour, min);
    }

    public static String formattedCrsDist(String dist) {
        return String.format(Locale.KOREA, "# %sKM", dist);
    }

    public static String formattedRecordTime(int time) {
        int sec = time % 60;
        int min = time / 60 % 60;
        int hour = time / 3600;
        return String.format(Locale.KOREA, "%dH %dM %dS", hour, min, sec);
    }

    public static String formattedRecordDist(double dist) {
        if(dist == 0.0){
            return String.format(Locale.KOREA, "%.1f", dist);
        }
        return String.format(Locale.KOREA, "%.2f", dist);
    }

    public static String formattedRecordCal(double cal) {
        return String.format(Locale.KOREA, "%.2fkcal", cal);
    }

}
