package com.example.project.Pedo;

public class RecordModel {
    String day;
    String startEndTime;
    String totalTime;
    String step;

    public RecordModel(String day, String startEndTime, String totalTime, String step) {
        this.day = day;
        this.startEndTime = startEndTime;
        this.totalTime = totalTime;
        this.step = step;
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
        return step;
    }
}
