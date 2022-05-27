package com.example.project.Main;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class EditAccountRequest  extends StringRequest {
    // 서버 URL 설정 ( PHP 파일 연동 )
    //localhost 바꾸기
    final static private String URL = "http://pacerfit.dothome.co.kr/EditAccount.php";
    private Map<String, String> map;

    public EditAccountRequest(String userID, String userPassword, String userName, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID", userID);
        map.put("userPassword", userPassword);
        map.put("userName", userName);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}