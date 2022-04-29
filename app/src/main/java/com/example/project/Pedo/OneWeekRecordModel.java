package com.example.project.Pedo;

public class OneWeekRecordModel {
    String day;
    String date;
    String totalTime;
    String step;

    public OneWeekRecordModel(String day, String date, String totalTime, String step) {
        this.day = day;
        this.date = date;
        this.totalTime = totalTime;
        this.step = step;
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
        return step;
    }
}
