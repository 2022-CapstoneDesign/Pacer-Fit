package com.example.project.Dist;

public class OneMonthRecordModel {
    String date;
    String totalTime;
    String km;

    public OneMonthRecordModel(String date, String totalTime, String km) {
        this.date = date;
        this.totalTime = totalTime;
        this.km = km;
    }

    public String getDate() {
        return date;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public String getKm() {
        return km;
    }
}
