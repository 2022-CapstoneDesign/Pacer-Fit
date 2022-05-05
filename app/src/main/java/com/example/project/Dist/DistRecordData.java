package com.example.project.Dist;

public class DistRecordData {
    static String[] KmRecord7_km = new String[7];
    static String[] KmRecord7_time = new String[7];
    static String km_max_day;

    static String[] KmRecord30_km = new String[31];
    static String[] KmRecord30_time = new String[31];
    static String km_max_month;

    static String[] KmRecord180_km = new String[25];
    static String[] KmRecord180_time = new String[25];
    static String km_max_180;

    static String[] KmRecordYear_km = new String[12];
    static String[] KmRecordYear_time = new String[12];
    static String km_max_year;

    //setter
    public void setKmRecord7(int i, String km, String time){
        KmRecord7_km[i] = km;
        KmRecord7_time[i] = time;
    }
    public void setKmRecord30(int i, String km, String time){
        KmRecord30_km[i] = km;
        KmRecord30_time[i] = time;
    }
    public void setKmRecord180(int i, String km, String time){
        KmRecord180_km[i] = km;
        KmRecord180_time[i] = time;
    }
    public void setKmRecordYear(int i, String km, String time){
        KmRecordYear_km[i] = km;
        KmRecordYear_time[i] = time;
    }

    //getter
    public void getKmRecord7(){
        for(int i=0; i<7; i++) {
            System.out.println("--getKmRecord7-->"+KmRecord7_km[i]);
        }
    }
}

