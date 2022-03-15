package com.example.project;


import android.content.ContentValues;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Weather {

    Date mReDate;
    SimpleDateFormat mFormatYDM;
    String formatYDM; //날짜 변화를 위한 변수



    public String weather(double x, double y){
        long mNow = System.currentTimeMillis();
        mReDate = new Date(mNow);
        mFormatYDM = new SimpleDateFormat("yyyyMMdd");
        formatYDM = mFormatYDM.format(mReDate);
        SimpleDateFormat mFormatTime = new SimpleDateFormat("HH00");
        String formatTime =String.valueOf( String.format("%04d",(Integer.parseInt(mFormatTime.format(mReDate))-100)));

        // URL 설정.
        // String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst"
        String service_key = "%2BueDsfUzs4BpeCUpo7OeKkoAHisGHzMO%2BMk6%2FlpamxNi1A47Of%2FoxIgPqC49WXQN5nCbRGVSCv6t9u3R6X1EaA%3D%3D";
        String num_of_rows = "10";
        String page_no = "1";
        String date_type = "JSON";
        String base_date = formatYDM;
        String base_time = timeChange(formatTime);
        String nx = String.format("%.0f",x);
        String ny = String.format("%.0f",y);

        String url ="http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?"+
                "serviceKey="+service_key+
                "&numOfRows="+num_of_rows+
                "&pageNo="+page_no+
                "&dataType="+date_type+
                "&base_date="+base_date+
                "&base_time="+base_time+
                "&nx="+nx+
                "&ny="+ny;

        Log.d("url", url);


        // AsyncTask를 통해 HttpURLConnection 수행.


        return url;
    }


    public String getCurrentAddress(Context context, double latitude, double longitude) {
        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(context, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(context, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }
        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(context, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        }
        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";
    }


    public String timeChange(String time)
    {
        // 현재 시간에 따라 데이터 시간 설정(3시간 마다 업데이트) //
        /**
         시간은 3시간 단위로 조회해야 한다. 안그러면 정보가 없다고 뜬다.
         0200, 0500, 0800 ~ 2300까지
         그래서 시간을 입력했을때 switch문으로 조회 가능한 시간대로 변경해주었다.
         **/
        switch(time) {
            case "0200":
            case "0300":
            case "0400":
                time = "0200";
                break;
            case "0500":
            case "0600":
            case "0700":
                time = "0500";
                break;
            case "0800":
            case "0900":
            case "1000":
                time = "0800";
                break;
            case "1100":
            case "1200":
            case "1300":
                time = "1100";
                break;
            case "1400":
            case "1500":
            case "1600":
                time = "1400";
                break;
            case "1700":
            case "1800":
            case "1900":
                time = "1700";
                break;
            case "2000":
            case "2100":
            case "2200":
                time = "2000";
                break;
            case "2300":
                time = "2300";
                break;
            default:
                formatYDM =String.valueOf(Integer.parseInt(mFormatYDM.format(mReDate))-1);
                time = "2300";
        }
        return time;
    }


}
