package com.example.project.Dist;

public class OneWeekRecordModel {
    String day;
    String date;
    String totalTime;
    String km;

    public OneWeekRecordModel(String day, String date, String totalTime, String km) {
        this.day = day;
        this.date = date;
        this.totalTime = totalTime;
        this.km = km;
    }

    public String getDay() {
        return day;
    }

    public String getDate() {
        return date;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public String getStep() {
        return km;
    }
}
