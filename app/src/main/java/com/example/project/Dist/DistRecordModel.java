package com.example.project.Dist;

public class DistRecordModel {
    String day;
    String startEndTime;
    String totalTime;
    String km;

    public DistRecordModel(String day, String startEndTime, String totalTime, String km) {
        this.day = day;
        this.startEndTime = startEndTime;
        this.totalTime = totalTime;
        this.km = km;
    }

    public String getDay() {
        return day;
    }

    public String getStartEndTime() {
        return startEndTime;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public String getStep() {
        return km;
    }
}
