package com.example.project.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.Ranking.UserInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyPageEditInfo extends AppCompatActivity {

    TextView nameTxt;
    TextView idTxt;
    TextView pwTxt;
    TextView genderTxt;
    TextView heightTxt;
    TextView weightTxt;
    TextView ageTxt;

    Button editInfoBtn1;
    Button editInfoBtn2;

    Dialog dialog1;
    Dialog dialog2;

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

        dialog1 = new Dialog(MyPageEditInfo.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.popup_edit_account_inform);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); // 뒤에 하얀 배경 안 나오게
        dialog1.setCanceledOnTouchOutside(false); // 외부 터치 시 꺼지는 현상 막기

        dialog2 = new Dialog(MyPageEditInfo.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.popup_edit_physical_inform);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); // 뒤에 하얀 배경 안 나오게
        dialog2.setCanceledOnTouchOutside(false); // 외부 터치 시 꺼지는 현상 막기


        editInfoBtn1 = findViewById(R.id.editInfoBtn1);
        editInfoBtn2 = findViewById(R.id.editInfoBtn2);

        editInfoBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditAccountDialog();
            }
        });

        editInfoBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditPhysicalDialog();
            }
        });

    }


    private void setUserData() {
        String gender = UserInfo.getInstance().getUserGender();
        if (gender.equals("man")) { gender = "남"; }
        else { gender = "여"; }

        nameTxt.setText(UserInfo.getInstance().getUserName());
        idTxt.setText(UserInfo.getInstance().getUserID());
        pwTxt.setText(UserInfo.getInstance().getUserPass());
        genderTxt.setText(gender);
        heightTxt.setText(UserInfo.getInstance().getUserHeight());
        weightTxt.setText(UserInfo.getInstance().getUserWeight());
        ageTxt.setText(UserInfo.getInstance().getUserAge());
    }

    private void showEditAccountDialog() {
        dialog1.show();

        ClearEditText editNameTxt = dialog1.findViewById(R.id.editNameTxt);
        ClearEditText editIdTxt = dialog1.findViewById(R.id.editIdTxt);
        ClearEditText editPwTxt = dialog1.findViewById(R.id.editPwTxt);

        editNameTxt.setText(nameTxt.getText());
        editIdTxt.setText(idTxt.getText());
        editPwTxt.setText(pwTxt.getText());

        Button okBtn = dialog1.findViewById(R.id.okBtn);
        Button cancelBtn = dialog1.findViewById(R.id.cancelBtn);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String changedName = String.valueOf(editNameTxt.getText());
                String changedId = String.valueOf(editIdTxt.getText());
                String changedPw = String.valueOf(editPwTxt.getText());

                // ****** 여기서 사용자가 입력한 값으로 변경 처리 ******

                dialog1.dismiss();
                Toast.makeText(getApplication(), "정보가 변경되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
    }

    private void showEditPhysicalDialog() {
        dialog2.show();

        TextView calculateAgeTxt = dialog2.findViewById(R.id.calculateAgeTxt);

        NumberPicker agePicker = dialog2.findViewById(R.id.agePicker);
        NumberPicker heightPicker = dialog2.findViewById(R.id.heightPicker);
        NumberPicker weightPicker = dialog2.findViewById(R.id.weightPicker);

        Button okBtn = dialog2.findViewById(R.id.okBtn);
        Button cancelBtn = dialog2.findViewById(R.id.cancelBtn);

        // <---- 나이 ---->
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("y");
        int thisYear = Integer.parseInt(df.format(cal.getTime()));
        int userAge = Integer.parseInt(UserInfo.getInstance().getUserAge());
        int userYear = thisYear - userAge + 1;

        agePicker.setMaxValue(2022);
        agePicker.setMinValue(1900);
        agePicker.setValue(userYear);

        agePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String val = String.valueOf(thisYear - newVal + 2);
                calculateAgeTxt.setText(val);
            }
        });

        // <---- 키 ---->
        int height = Integer.parseInt(UserInfo.getInstance().getUserHeight());
        //int height = Integer.parseInt((String) heightTxt.getText());

        heightPicker.setMaxValue(200);
        heightPicker.setMinValue(50);
        heightPicker.setValue(height);

        heightPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String changedHeight = String.valueOf(newVal);
            }
        });

        // <---- 몸무게 ---->
        int weight = Integer.parseInt(UserInfo.getInstance().getUserWeight());

        weightPicker.setMinValue(30);
        weightPicker.setMaxValue(200);
        weightPicker.setValue(weight);

        weightPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String changedWeight = String.valueOf(newVal);
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
                Toast.makeText(getApplication(), "정보가 변경되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
    }
}