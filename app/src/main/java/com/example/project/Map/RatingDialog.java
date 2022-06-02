package com.example.project.Map;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.project.Main.EditAccountRequest;
import com.example.project.Main.MyPageEditInfoActivity;
import com.example.project.R;
import com.example.project.Ranking.UserInfo;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import org.json.JSONException;
import org.json.JSONObject;

public class RatingDialog extends Dialog {

    private Context context;
    private float rate;
    private String name;

    public RatingDialog(@NonNull Context context, String name) {
        super(context);
        this.context = context;
        this.name = name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.map_rating_bar);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        Button confirmButton = findViewById(R.id.confirm_btn);
        Button cancelButton = findViewById(R.id.cancel_btn);
        TextView ratingName = findViewById(R.id.rating_name);
        ratingName.setText(name);
        ScaleRatingBar scaleRatingBar = findViewById(R.id.scale_rating_bar);
        scaleRatingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating, boolean fromUser) {
                rate = rating;
            }
        });

        // 버튼 리스너 설정
        confirmButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 별점 db로 보내기

                if (rate == 0) {
                    Toast.makeText(getContext(), "별점을 매겨주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Response.Listener<String> responseListener = response -> {
                        try {
                            System.out.println(response);
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) { // 별점 등록 성공 and 실패
                                dismiss();
                            } else { // 회원등록에 실패한 경우
                                Toast.makeText(getContext().getApplicationContext(), "등록 실패", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    };
                    // 서버로 Volley를 이용해서 요청을 함.
                    //RegisterRequest.java 이동
                    RatingCourseRequest ratingCourseRequest = new RatingCourseRequest(UserInfo.getInstance().getUserID(), name, rate,responseListener);
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    queue.add(ratingCourseRequest);
                }
            }
        });
        cancelButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                rate = 0;
                dismiss();
            }
        });

    }


}
