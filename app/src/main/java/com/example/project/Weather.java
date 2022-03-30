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


    public String weather(double x, double y) {
        long mNow = System.currentTimeMillis();
        mReDate = new Date(mNow);
        mFormatYDM = new SimpleDateFormat("yyyyMMdd");
        formatYDM = mFormatYDM.format(mReDate);
        SimpleDateFormat mFormatTime = new SimpleDateFormat("HH00");
        String formatTime =String.format("%04d",(Integer.parseInt(mFormatTime.format(mReDate))));

        // URL 설정.

        String service_key = "%2BueDsfUzs4BpeCUpo7OeKkoAHisGHzMO%2BMk6%2FlpamxNi1A47Of%2FoxIgPqC49WXQN5nCbRGVSCv6t9u3R6X1EaA%3D%3D";
        String num_of_rows = "100";
        String page_no = "1";
        String date_type = "JSON";
        String base_time = timeChange(formatTime);
        String base_date = formatYDM;
        String nx = String.format("%.0f",x);
        String ny = String.format("%.0f",y);

        String url ="http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst?"+
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
        return address.getAddressLine(0).toString() + "\n";
    }


    public String timeChange(String time)
    {
        // 현재 시간에 따라 데이터 시간 설정(1시간 마다 업데이트) //
        /**
            매시간 30분에 T+1...T+5 예보데이터가 생성되고 45분에 조회가 가능하다.
            1930에 20시,21시,22시,23시,00시 예보데이터가 생성되고 45분에 조회가 가능하다.
            20:XX 인경우 20시의 예보를 보기 위해선 1930 시간을 검색해야 한다.
         **/
        switch(time) {
            case "0100":
                time = "0030";
                break;
            case "0200":
                time = "0130";
                break;
            case "0300":
                time = "0230";
                break;
            case "0400":
                time = "0330";
                break;
            case "0500":
                time = "0430";
                break;
            case "0600":
                time = "0530";
                break;
            case "0700":
                time = "0630";
                break;
            case "0800":
                time = "0730";
                break;
            case "0900":
                time = "0830";
                break;
            case "1000":
                time = "0930";
                break;
            case "1100":
                time = "1030";
                break;
            case "1200":
                time = "1130";
                break;
            case "1300":
                time = "1230";
                break;
            case "1400":
                time = "1330";
                break;
            case "1500":
                time = "1430";
                break;
            case "1600":
                time = "1530";
                break;
            case "1700":
                time = "1630";
                break;
            case "1800":
                time = "1730";
                break;
            case "1900":
                time = "1830";
                break;
            case "2000":
                time = "1930";
                break;
            case "2100":
                time = "2030";
                break;
            case "2200":
                time = "2130";
                break;
            case "2300":
                time = "2230";
                break;
            default:
                formatYDM =String.valueOf(Integer.parseInt(mFormatYDM.format(mReDate))-1);
                time = "2330";
        }
        return time;
    }


}