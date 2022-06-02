package com.example.project.Main;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class HashTagRequest extends StringRequest {
    // 서버 URL 설정 ( PHP 파일 연동 )
    final static private String URL = "http://pacerfit.dothome.co.kr/MyPage_HashTag.php";
    private Map<String, String> map;

    public HashTagRequest(String userID, int dif,int a1, int a2, int a3, int a4, int a5, int a6, int a7 , int a8, int a9, int a10,int a11, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("userID", userID);
        map.put("dif", dif+"");
        map.put("park", a1+"");
        map.put("mountain", a2+"");
        map.put("forest", a3+"");
        map.put("sea", a4+"");
        map.put("beach", a5+"");
        map.put("trekking", a6+"");
        map.put("nature", a7+"");
        map.put("sights", a8+"");
        map.put("town", a9+"");
        map.put("scenery", a10+"");;
        map.put("history", a11+"");
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}