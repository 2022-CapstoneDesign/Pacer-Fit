package com.example.project.Login;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    // 서버 URL 설정 ( PHP 파일 연동 )
    //localhost 바꾸기
    final static private String URL = "http://pacerfit.dothome.co.kr/Register.php";
    //final static private String URL = "http://localhost:8080/PacerFitDB/Register.php";
    private Map<String, String> map;

    public RegisterRequest(String userID, String userPassword, String userName, String userGender, int userAge, Float userHeight, Float userWeight, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID", userID);
        map.put("userPassword", userPassword);
        map.put("userName", userName);
        map.put("userGender", userGender);
        map.put("userAge", userAge + "");
        map.put("userHeight", userHeight + "");
        map.put("userWeight", userWeight + "");
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}