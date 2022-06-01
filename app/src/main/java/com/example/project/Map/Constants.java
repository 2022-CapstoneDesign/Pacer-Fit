package com.example.project.Map;

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

    static final long FUSED_LOCATION_INTERVAL = 5000L;
    static final long FUSED_LOCATION_FASTEST = 2000L;
    static final long FUSED_LOCATION_MAX_WAIT = 5000L;

    static final long CHECK_RATING_CRS_TIME = 1;


    // notification
    static final String CHANNEL_ID = "Notification Channel";
    static final String CHANNEL_NAME = "Map Channel";
    static final String CHANNEL_DESCRIPTION = "Map";
    static final int NOTIFICATION_ID = 1;



}