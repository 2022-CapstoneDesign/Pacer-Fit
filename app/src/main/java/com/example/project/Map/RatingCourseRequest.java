package com.example.project.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RatingCourseRequest  extends StringRequest {
    // 서버 URL 설정 ( PHP 파일 연동 )
    //localhost 바꾸기
    final static private String URL = "http://pacerfit.dothome.co.kr/RatingCourse.php";
    private Map<String, String> map;

    public RatingCourseRequest(String userID, String CourseName, float rate, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID", userID);
        map.put("CourseName", CourseName);
        map.put("rate", rate+"");
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}