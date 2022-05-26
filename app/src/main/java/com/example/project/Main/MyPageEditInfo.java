package com.example.project.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
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

        RadioGroup ManOrWoman = dialog2.findViewById(R.id.radioGroup);

        TextView calculateAgeTxt = dialog2.findViewById(R.id.calculateAgeTxt);
        calculateAgeTxt.setText(UserInfo.getInstance().getUserAge());

        TextView calculateHeightIntTxt = dialog2.findViewById(R.id.calculateHeightIntTxt);
        calculateHeightIntTxt.setText(getHeightIntVal());

        TextView calculateHeightPointTxt = dialog2.findViewById(R.id.calculateHeightPointTxt);
        calculateHeightPointTxt.setText(getHeightPointVal());

        TextView calculateWeightIntTxt = dialog2.findViewById(R.id.calculateWeightIntTxt);
        calculateWeightIntTxt.setText(getWeightIntVal());

        TextView calculateWeightPointTxt = dialog2.findViewById(R.id.calculateWeightPointTxt);
        calculateWeightPointTxt.setText(getWeightPointVal());

        NumberPicker agePicker = dialog2.findViewById(R.id.agePicker);
        NumberPicker heightPicker = dialog2.findViewById(R.id.heightPicker);
        NumberPicker heightPointPicker = dialog2.findViewById(R.id.heightPointPicker);
        NumberPicker weightPicker = dialog2.findViewById(R.id.weightPicker);
        NumberPicker weightPointPicker = dialog2.findViewById(R.id.weightPointPicker);

        Button okBtn = dialog2.findViewById(R.id.okBtn);
        Button cancelBtn = dialog2.findViewById(R.id.cancelBtn);

        // <---- 나이 ---->
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("y");
        int thisYear = Integer.parseInt(df.format(cal.getTime()));
        int userAge = Integer.parseInt(UserInfo.getInstance().getUserAge());
        int userYear = thisYear - userAge + 1;

        agePicker.setMinValue(1901);
        agePicker.setMaxValue(thisYear);
        agePicker.setValue(userYear);
        agePicker.setWrapSelectorWheel(false);

        agePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String val = String.valueOf(thisYear - newVal + 1);
                calculateAgeTxt.setText(val);
            }
        });

        // <---- 키 ---->
        int height = Integer.parseInt(UserInfo.getInstance().getUserHeight());
        //int height = Integer.parseInt((String) heightTxt.getText());

        heightPicker.setMaxValue(200);
        heightPicker.setMinValue(50);
        heightPicker.setValue(Integer.parseInt(getHeightIntVal()));
        heightPicker.setWrapSelectorWheel(false);

        heightPointPicker.setMinValue(0);
        heightPointPicker.setMaxValue(9);
        heightPointPicker.setValue(Integer.parseInt(getHeightPointVal()));
        heightPointPicker.setWrapSelectorWheel(true);

        // 정수자리
        heightPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String val = String.valueOf(newVal);
                calculateHeightIntTxt.setText(val);
            }
        });
        // 소수점
        heightPointPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String val = String.valueOf(newVal);
                calculateHeightPointTxt.setText(val);
            }
        });

        // <---- 몸무게 ---->
        int weight = Integer.parseInt(UserInfo.getInstance().getUserWeight());

        weightPicker.setMinValue(30);
        weightPicker.setMaxValue(200);
        weightPicker.setValue(Integer.parseInt(getWeightIntVal()));
        weightPicker.setWrapSelectorWheel(false);

        weightPointPicker.setMinValue(0);
        weightPointPicker.setMaxValue(9);
        weightPointPicker.setValue(Integer.parseInt(getWeightPointVal()));
        weightPointPicker.setWrapSelectorWheel(true);

        // 정수자리
        weightPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String val = String.valueOf(newVal);
                calculateWeightIntTxt.setText(val);
            }
        });
        // 소수점
        weightPointPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String val = String.valueOf(newVal);
                calculateWeightPointTxt.setText(val);
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 성별 값 읽어오기
                String gender;
                if (ManOrWoman.getCheckedRadioButtonId() == R.id.manBtn) {
                    gender = "man";
                }
                else {
                    gender = "woman";
                }

                // 나이 값 읽어오기
                int age = Integer.parseInt(calculateAgeTxt.getText().toString());

                // 키 값 읽어오기
                Float height = Float.parseFloat(calculateHeightIntTxt.getText().toString()) + Float.parseFloat(calculateHeightPointTxt.getText().toString()) * 0.1f;

                // 몸무게 값 읽어오기
                Float weight = Float.parseFloat(calculateWeightIntTxt.getText().toString()) + Float.parseFloat(calculateWeightPointTxt.getText().toString()) * 0.1f;

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

    public String getHeightIntVal() {
        Float height = Float.valueOf(UserInfo.getInstance().getUserHeight());
        int intVal = (int) (height * 10) / 10;

        return String.valueOf(intVal);
    }
    public String getHeightPointVal() {
        Float height = Float.valueOf(UserInfo.getInstance().getUserHeight());
        int  point = (int) ((height * 10) % 10);

        return String.valueOf(point);
    }
    public String getWeightIntVal() {
        Float weight = Float.valueOf(UserInfo.getInstance().getUserWeight());
        int intVal = (int) (weight * 10) / 10;

        return String.valueOf(intVal);
    }
    public String getWeightPointVal() {
        Float weight = Float.valueOf(UserInfo.getInstance().getUserWeight());
        int  point = (int) ((weight * 10) % 10);

        return String.valueOf(point);
    }
}