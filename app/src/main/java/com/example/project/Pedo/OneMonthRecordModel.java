package com.example.project.Pedo;

public class OneMonthRecordModel {
    String date;
    String totalTime;
    String step;

    public OneMonthRecordModel(String date, String totalTime, String step) {
        this.date = date;
        this.totalTime = totalTime;
        this.step = step;
    }

    public String getDate() {
        return date;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public String getStep() {
        return step;
    }
}
