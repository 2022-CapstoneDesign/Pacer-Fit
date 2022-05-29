package com.example.project.Main;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteAccountRequest extends StringRequest {
    // 서버 URL 설정 ( PHP 파일 연동 )
    //localhost 바꾸기
    final static private String URL = "http://pacerfit.dothome.co.kr/DeleteAccount.php";
    private Map<String, String> map;

    public DeleteAccountRequest(String userID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID", userID);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
