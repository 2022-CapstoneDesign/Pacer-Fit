package com.example.project.Pedo;

public class PedoRecordData {
    static String[] PedoRecord7_step = new String[7];
    static String[] PedoRecord7_time = new String[7];
    static String pedo_max_day;

    static String[] PedoRecord30_step = new String[31];
    static String[] PedoRecord30_time = new String[31];
    static String pedo_max_month;

    static String[] PedoRecord180_step = new String[25];
    static String[] PedoRecord180_time = new String[25];
    static String pedo_max_180;

    static String[] PedoRecordYear_step = new String[12];
    static String[] PedoRecordYear_time = new String[12];
    static String pedo_max_year;

    //setter
    public void setPedoRecord7(int i, String step, String time){
        PedoRecord7_step[i] = step;
        PedoRecord7_time[i] = time;
    }
    public void setPedoRecord30(int i, String step, String time){
        PedoRecord30_step[i] = step;
        PedoRecord30_time[i] = time;
    }
    public void setPedoRecord180(int i, String step, String time){
        PedoRecord180_step[i] = step;
        PedoRecord180_time[i] = time;
    }
    public void setPedoRecordYear(int i, String step, String time){
        PedoRecordYear_step[i] = step;
        PedoRecordYear_time[i] = time;
    }

    //getter
    public void getPedoRecord7(){
        for(int i=0; i<7; i++) {
            System.out.println("--getPedoRecord7-->"+PedoRecord7_step[i]);
        }
    }
}
