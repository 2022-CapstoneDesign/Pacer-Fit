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
    String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.main_mypage_identify_fragment, container, false);

        Intent intent = getActivity().getIntent();
        userID = intent.getStringExtra("userID");
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
        // ??????????????? ??????
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
                if (pw.equals(UserInfo.getInstance().getUserPass())) {
                    // ???????????? ?????? ??????????????? ??????
                    Intent intent = new Intent(getActivity(), MyPageEditInfoActivity.class); //Fragment -> Activity??? ?????? (StepCounterActivity.java)
                    intent.putExtra("userID", userID);
                    startActivity(intent);
                    getActivity().getSupportFragmentManager().popBackStack(); // ???????????? ?????? ??? ???????????? ????????? ??? ??? ?????????
                }
                else { // ???????????? ??????
                    YoYo.with(Techniques.Shake)
                            .duration(700)
                            .repeat(0)
                            .playOn(pwLayout); // ????????? ???????????? ???????????????
                    Toast.makeText(getContext(), "????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                }

            }
        });


        return v;
    }
}