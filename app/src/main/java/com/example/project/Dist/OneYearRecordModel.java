package com.example.project.Dist;

public class OneYearRecordModel {
    String date;
    String totalTime;
    String km;

    public OneYearRecordModel(String date, String totalTime, String km) {
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
