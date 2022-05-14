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
        List<ActivityManager.RunningTaskInfo> taskInfos = activityManager.getRunningTasks(Integer.MAX_VALUE);
        if (!taskInfos.isEmpty()) {
            int taskSize = taskInfos.size();
            for (int i = 0; i < taskSize; i++) {
                ActivityManager.RunningTaskInfo taskInfo = taskInfos.get(i);
                if (taskInfo.topActivity.getPackageName().equals(context.getApplicationContext().getPackageName())) {
                    activityManager.moveTaskToFront(taskInfo.id, 0);
                }
            }
        }
        Log.d(TAG, "onReceive");
    }
}
