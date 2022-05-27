package com.example.project.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.project.Login.LoginActivity;
import com.example.project.Login.RegisterActivity;
import com.example.project.Login.RegisterRequest;
import com.example.project.R;
import com.example.project.Ranking.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

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
        //입력을 막아놈
        editIdTxt.setClickable(false);
        editIdTxt.setFocusable(false);
        editPwTxt.setText(pwTxt.getText());

        Button okBtn = dialog1.findViewById(R.id.okBtn);
        Button cancelBtn = dialog1.findViewById(R.id.cancelBtn);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String changedName = String.valueOf(editNameTxt.getText());
                String changedPw = String.valueOf(editPwTxt.getText());

                // ****** 여기서 사용자가 입력한 값으로 변경 처리 ******
                Response.Listener<String> responseListener = response -> {
                    try {
                        System.out.println(response);
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) { // 회원등록에 성공한 경우
                            nameTxt.setText(changedName);
                            pwTxt.setText(changedPw);
                            UserInfo.getInstance().setUserName(changedName);
                            UserInfo.getInstance().setUserPass(changedPw);
                            dialog1.dismiss();
                            Toast.makeText(getApplication(), "정보가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                        } else { // 회원등록에 실패한 경우
                            String answer = jsonObject.getString("answer");
                            if(answer.equals("name")){ //이름 중복
                                Toast.makeText(getApplicationContext(), "이미 사용중인 닉네임입니다", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "수정 실패", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };
                // 서버로 Volley를 이용해서 요청을 함.
                //RegisterRequest.java 이동
                EditAccountRequest editAccountRequest = new EditAccountRequest(UserInfo.getInstance().getUserID(), changedPw, changedName, responseListener);
                RequestQueue queue = Volley.newRequestQueue(MyPageEditInfo.this);
                queue.add(editAccountRequest);

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
        RadioGroup Man_or_Woman = dialog2.findViewById(R.id.radioGroup);
        System.out.println("================gender"+UserInfo.getInstance().getUserGender());
        final String[] gender = new String[1];
        if(UserInfo.getInstance().getUserGender().equals("man"))
            Man_or_Woman.check(R.id.manRadioBtn);
        else
            Man_or_Woman.check(R.id.womanRadioBtn);

        if(UserInfo.getInstance().getUserGender().equals("man")) {
            genderTxt.setText("남");
        }else{
            genderTxt.setText("여");
        }

        Man_or_Woman.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { //라디오 버튼 남자 or 여자 선택
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.manRadioBtn:
//                        UserInfo.getInstance().setUserGender("man");
                        Man_or_Woman.check(R.id.manRadioBtn);
                        gender[0] = "man";
                        break;
                    case R.id.womanRadioBtn:
//                        UserInfo.getInstance().setUserGender("woman");
                        Man_or_Woman.check(R.id.womanRadioBtn);
                        gender[0] = "woman";
                        break;
                }
            }
        });
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

        int[] cage = new int[1];
        agePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String val = String.valueOf(thisYear - newVal + 2);
                calculateAgeTxt.setText(val);
                System.out.println("나이=============="+calculateAgeTxt.getText());
                cage[0] = Integer.parseInt(val);
                UserInfo.getInstance().setUserAge(val);
            }
        });

        // <---- 키 ---->
        int height = Integer.parseInt(UserInfo.getInstance().getUserHeight());
        //int height = Integer.parseInt((String) heightTxt.getText());

        heightPicker.setMaxValue(200);
        heightPicker.setMinValue(50);
        heightPicker.setValue(height);
        int[] ch = new int[1];
        int[] cw = new int[1];
        heightPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String changedHeight = String.valueOf(newVal-1);
                ch[0] = Integer.parseInt(changedHeight);
                UserInfo.getInstance().setUserHeight(changedHeight);
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
                String changedWeight = String.valueOf(newVal-1);
                cw[0] = Integer.parseInt(changedWeight);
                UserInfo.getInstance().setUserWeight(changedWeight);
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ****** 여기서 사용자가 입력한 값으로 변경 처리 ******
                Response.Listener<String> responseListener = response -> {
                    try {
                        System.out.println(response);
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) { // 회원등록에 성공한 경우
                            RadioButton a = dialog2.findViewById(R.id.manRadioBtn);
                            if(a.isChecked()){
                                UserInfo.getInstance().setUserGender("man");
                                genderTxt.setText("남");
                            }
                            else{
                                UserInfo.getInstance().setUserGender("woman");
                                genderTxt.setText("여");
                            }

//                            if(UserInfo.getInstance().getUserGender().equals("man"))
//                                genderTxt.setText("남");
//                            else
//                                genderTxt.setText("여");
//                            heightTxt.setText(String.valueOf(ch[0]));
//                            weightTxt.setText(String.valueOf(cw[0]));
                            heightTxt.setText(UserInfo.getInstance().getUserHeight());
                            weightTxt.setText(UserInfo.getInstance().getUserWeight());
                            ageTxt.setText(UserInfo.getInstance().getUserAge());

                            dialog2.dismiss();
                            Toast.makeText(getApplication(), "정보가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            String answer = jsonObject.getString("answer");
                            if(answer.equals("age")){
                                Toast.makeText(getApplicationContext(), "나이를 설정해주세요", Toast.LENGTH_SHORT).show();
                            }
                            else if(answer.equals("height")){
                                Toast.makeText(getApplicationContext(), "키를 설정해주세요", Toast.LENGTH_SHORT).show();
                            }
                            else if(answer.equals("weight")){
                                Toast.makeText(getApplicationContext(), "몸무게를 설정해주세요", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "수정 실패", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };
                // 서버로 Volley를 이용해서 요청을 함.
                //RegisterRequest.java 이동
                EditPhysicalRequest editPhysicalRequest = new EditPhysicalRequest(UserInfo.getInstance().getUserID(),
                        gender[0],
                        cage[0],
                        ch[0],
                        cw[0],
                        responseListener);
                RequestQueue queue = Volley.newRequestQueue(MyPageEditInfo.this);
                queue.add(editPhysicalRequest);
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