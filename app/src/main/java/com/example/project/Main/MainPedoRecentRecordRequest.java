package com.example.project.Main;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class MainPedoRecentRecordRequest extends StringRequest {
    // 서버 URL 설정 ( PHP 파일 연동 )
    final static private String URL = "http://pacerfit.dothome.co.kr/Main_PedoRecentRecord.php";
    private Map<String, String> map;
    String date_concat;

    public MainPedoRecentRecordRequest(String userID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate now = null;
            now = LocalDate.now();

            int monthValue = now.getMonthValue();
            int dayOfMonth = now.getDayOfMonth();
            date_concat = monthValue + "m" + dayOfMonth + "d";
        }
        map = new HashMap<>();
        map.put("userID", userID);
        map.put(date_concat+"",date_concat);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}