package com.example.project.Dist;

public class RecordModel {
    String day;
    String startEndTime;
    String totalTime;
    String km;

    public RecordModel(String day, String startEndTime, String totalTime, String km) {
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