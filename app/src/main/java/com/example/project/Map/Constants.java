package com.example.project.Map;

import java.util.Locale;

public class Constants {

    // service
    static final String ACTION_START_LOCATION_SERVICE = "startLocationService";
    static final String ACTION_STOP_LOCATION_SERVICE = "stopLocationService";
    static final String ACTION_PAUSE_LOCATION_SERVICE = "pauseLocationService";

    static final String ACTION_RESUME_ACTIVITY = "resumeActivity";
    static final String ACTION_PAUSE_ACTIVITY = "pauseActivity";

    // location
    static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    static final String BROADCAST_ACTION = "LocationService";

    // notification
    static final String CHANNEL_ID = "Notification Channel";
    static final String CHANNEL_NAME = "Map Channel";
    static final String CHANNEL_DESCRIPTION = "Map";
    static final int NOTIFICATION_ID = 1;

    // 포매팅
    public static String formattingTime(int time) {
        int hour = time / 3600;
        int min = time / 60 % 60;
        int sec = time % 60;
        return String.format(Locale.KOREA,"%02d:%02d:%02d", hour, min, sec);
    }

    public static String formattingDist(double dist) {
        return String.format(Locale.KOREA,"%.2fkm", Math.round(dist * 100) / 100.0);
    }
    public static String formattingCal(double cal) {
        return String.format(Locale.KOREA,"%.2fkcal", Math.round(cal * 100) / 100.0);
    }
    public static String formattingSpeed(double speed) {
        return String.format(Locale.KOREA,"%.2fkm/h", Math.round(speed * 100) / 100.0);
    }

}