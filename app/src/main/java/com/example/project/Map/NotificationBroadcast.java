package com.example.project.Map;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

public class NotificationBroadcast extends BroadcastReceiver {

    private final String TAG = "NotificationBroadcast";

    @Override
    public void onReceive(Context context, Intent intent) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfos = activityManager.getRunningTasks(1);
        if (!taskInfos.isEmpty()) {
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfos) {
                if (taskInfo.topActivity.getPackageName().equals("com.example.project.Map.RecordMapActivity")) {
                    activityManager.moveTaskToFront(taskInfo.id, 0);
                }
            }
        }
        Log.d(TAG, "onReceive");
    }
}
