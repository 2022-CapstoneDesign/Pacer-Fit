package com.example.project.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.Ranking.UserInfo;

public class MyPageEditInfo extends AppCompatActivity {

    TextView nameTxt;
    TextView idTxt;
    TextView pwTxt;
    TextView genderTxt;
    TextView heightTxt;
    TextView weightTxt;
    TextView ageTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_mypage_edit_info_activity);

        nameTxt = findViewById(R.id.nameTxt);
        idTxt = findViewById(R.id.idTxt);
        pwTxt = findViewById(R.id.pwTxt);
        genderTxt = findViewById(R.id.genderTxt);
        heightTxt = findViewById(R.id.heightTxt);
        weightTxt = findViewById(R.id.weightTxt);
        ageTxt = findViewById(R.id.ageTxt);

        setUserData();

    }

    private void setUserData() {
        String gender = UserInfo.getInstance().getUserGender();
        if (gender.equals("man")) { gender = "남"; }
        else { gender = "여"; }

        nameTxt.setText(UserInfo.getInstance().getUserName());
        idTxt.setText(UserInfo.getInstance().getUserID());
        pwTxt.setText(UserInfo.getInstance().getUserPass());
        genderTxt.setText(gender);
        heightTxt.setText(UserInfo.getInstance().getUserHeight() + "cm");
        weightTxt.setText(UserInfo.getInstance().getUserWeight() + "kg");
        ageTxt.setText(UserInfo.getInstance().getUserAge() + "세");
    }
}