package com.example.project.Pedo;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class PedoRecordSaveRequest extends StringRequest {
    // 서버 URL 설정 ( PHP 파일 연동 )
    final static private String URL = "http://pacerfit.dothome.co.kr/Pedo_Record_Save.php";
    private Map<String, String> map;
    String date_concat;

    public PedoRecordSaveRequest(String userID ,String pedoStep,String pedoTime, String pedoCalorie, Response.Listener<String> listener) {
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
        map.put("steps", pedoStep);
        map.put("pedoTime", pedoTime);
        map.put("pedoCal", pedoCalorie);
        map.put(date_concat+"",date_concat);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}