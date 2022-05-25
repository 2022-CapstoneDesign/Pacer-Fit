package com.example.project.Main;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.project.R;
import com.example.project.Ranking.UserInfo;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MyPageIdentifyFragment extends Fragment {
    TextInputLayout pwLayout;
    TextInputEditText pwEditTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.main_mypage_identify_fragment, container, false);

        Intent intent = getActivity().getIntent();
        int[] ProfileDrawable = {
                R.drawable.profile_default, R.drawable.profile_man, R.drawable.profile_man_beard, R.drawable.profile_man_cap,
                R.drawable.profile_man_hat, R.drawable.profile_man_hood, R.drawable.profile_man_horn, R.drawable.profile_man_round,
                R.drawable.profile_man_suit, R.drawable.profile_man_sunglass, R.drawable.profile_woman_glasses, R.drawable.profile_woman_neck,
                R.drawable.profile_woman_old, R.drawable.profile_woman_scarf
        };

        ImageView profile = v.findViewById(R.id.profile);
        profile.setImageResource(ProfileDrawable[UserInfo.getInstance().getUserProfileNum()]);
        TextView idTxt = v.findViewById(R.id.idTxt);
        idTxt.setText(intent.getStringExtra("userID"));

        LinearLayout ll = v.findViewById(R.id.linearLayout);
        // 애니메이션 효과
        YoYo.with(Techniques.SlideInLeft)
                .duration(700)
                .repeat(0)
                .playOn(ll);

        pwLayout = v.findViewById(R.id.pwLayout);
        pwEditTxt = v.findViewById(R.id.pwTxt);
        Button loginBtn = v.findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pw = String.valueOf(pwEditTxt.getText());
                if (pw.equals(intent.getStringExtra("userPass"))) {
                    // 개인정보 수정 액티비티로 이동
                    startActivity(new Intent(getActivity(), MyPageEditInfo.class)); //Fragment -> Activity로 이동
                }
                else { // 비밀번호 오류
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .repeat(0)
                            .playOn(pwLayout); // 좌우로 흔들리는 애니메이션
                    Toast.makeText(getContext(), "잘못된 비밀번호입니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });


        return v;
    }
}