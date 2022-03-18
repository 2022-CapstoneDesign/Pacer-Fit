package com.example.project;

import android.content.ContentValues;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

// URL을 통한 API 접근을 위한 클래스
public class RequestHttpConnection {
    public String requset(String _url, ContentValues _params) {
        //HttpURLConnection 참조 변수.
        HttpURLConnection urlConn = null;
        //URL 뒤에 붙여서 보낼 파라미터.
        StringBuffer sbParams = new StringBuffer();


        // 1. StringBuffer에 파라미터 연결
        // 보낼 데이터가 없으면 파라미터를 비운다.
        if(_params == null)
            sbParams.append("");
            // 보낼 데이터가 있으면 파라미터를 채운다.
        else {
            // 파라미터가 2개 이상이면 파라미터 연결에 &가 필요하므로 스위칭할 변수 생성.
            boolean isAnd = false;
            // 파라미터 키와 값.
            String key;
            String value;

            for(Map.Entry<String, Object> parameter : _params.valueSet()){
                key = parameter.getKey();
                value = parameter.getValue().toString();

                // 파라미터가 두개 이상일때, 파라미터 사이에 &를 붙인다.
                if(isAnd)
                    sbParams.append("&");

                sbParams.append(key).append("=").append(value);

                // 파라미터가 두개 이상이면 isAnd를 true로 바꾸고 다음 루프부터 &를 붙인다.
                if(!isAnd)
                    if(_params.size() >= 2)
                        isAnd = true;
            }
        }

        // 2. HttpURLConnection을 통해 web의 데이터를 가져온다.
        try {
            URL url = new URL(_url);
            urlConn = (HttpURLConnection) url.openConnection();

            // [2-1]. urlConn 설정
            urlConn.setRequestMethod("GET"); //URL 요청에 대한 메소드 설정 : GET.
            urlConn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정
            urlConn.setDoOutput(false); // POST 값을 보낼 땐 true

            // [2-2]. 연결 요청 확인.
            // 실패시 null을 리턴하고 메서드 종료.
            if(urlConn.getResponseCode() != HttpURLConnection.HTTP_OK){
                Log.d("HTTP_OK", "연결 요청 실패");
                return null;
            }

            // [2-3]. 읽어온 결과물 리턴.
            // 요청한 URL의 출력물을 BufferedReader로 받는다.
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

            // 출력물의 라인과 그 합에 대한 변수.
            String line;
            String page = "";

            // 라인을 받아와 합친다.
            while((line = reader.readLine()) != null){
                page += line;
            }

            return page;

        } catch (MalformedURLException e) {
            Log.d("MalformedURLException", String.valueOf(e));
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("IOException", String.valueOf(e));
            e.printStackTrace();
        } finally {
            if(urlConn != null)
                urlConn.disconnect();
        }

        return null;
    }
}
