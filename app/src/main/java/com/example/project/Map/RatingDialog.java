package com.example.project.Map;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.project.R;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

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
        setContentView(R.layout.map_rating_bar);


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

                dismiss();
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
