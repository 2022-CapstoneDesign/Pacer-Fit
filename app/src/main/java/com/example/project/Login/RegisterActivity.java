package com.example.project.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.project.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText idTxt, pwTxt, nameTxt;
    //private EditText ageTxt, heightTxt, weightTxt;
    private RadioGroup Man_or_Woman;
    private Button joinBtn;

    private String gender = "man";

    TextView calculateAgeTxt;
    TextView calculateHeightIntTxt;
    TextView calculateHeightPointTxt;
    TextView calculateWeightIntTxt;
    TextView calculateWeightPointTxt;

    NumberPicker agePicker;
    NumberPicker heightPicker;
    NumberPicker heightPointPicker;
    NumberPicker weightPicker;
    NumberPicker weightPointPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register_activity);

        idTxt = findViewById(R.id.joinIdTxt);
        pwTxt = findViewById(R.id.joinPwTxt);
        nameTxt = findViewById(R.id.joinNameTxt);
        Man_or_Woman = findViewById(R.id.radioGroup);

        //ageTxt = findViewById(R.id.joinAgeTxt);
//        heightTxt = findViewById(R.id.joinHeightTxt);
//        weightTxt = findViewById(R.id.joinWeightTxt);

        calculateAgeTxt = findViewById(R.id.calculateAgeTxt);
        calculateAgeTxt.setText("20");
        calculateHeightIntTxt = findViewById(R.id.calculateHeightIntTxt);
        calculateHeightIntTxt.setText("170");
        calculateHeightPointTxt = findViewById(R.id.calculateHeightPointTxt);
        calculateHeightPointTxt.setText("0");
        calculateWeightIntTxt = findViewById(R.id.calculateWeightIntTxt);
        calculateWeightIntTxt.setText("60");
        calculateWeightPointTxt = findViewById(R.id.calculateWeightPointTxt);
        calculateWeightPointTxt.setText("0");

        agePicker = findViewById(R.id.agePicker);
        heightPicker = findViewById(R.id.heightPicker);
        heightPointPicker = findViewById(R.id.heightPointPicker);
        weightPicker = findViewById(R.id.weightPicker);
        weightPointPicker = findViewById(R.id.weightPointPicker);

        // 입력 시 처음 뜨는 키보드 언어 강제 설정(이라는데 효과가 없는 것 같습니다)
        idTxt.setPrivateImeOptions("defaultInputmode=english;");
        // 아이디는 한글, 특수문자, 이모지 등은 제한하고 영어 소문자, 숫자만 최대 20글자 입력 허용
        // (참고 : 비밀번호는 영어(소,대), 숫자, 특수문자만 최대 20글자 입력 가능)
        idTxt.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(20)});


        // <---- 나이 ---->
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("y");
        int thisYear = Integer.parseInt(df.format(cal.getTime()));

        agePicker.setMinValue(1901);
        agePicker.setMaxValue(thisYear);
        agePicker.setValue(2003);
        agePicker.setWrapSelectorWheel(false);

        agePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String val = String.valueOf(thisYear - newVal + 1);
                calculateAgeTxt.setText(val);
            }
        });

        // <---- 키 ---->
        heightPicker.setMaxValue(200);
        heightPicker.setMinValue(50);
        heightPicker.setValue(170);
        heightPicker.setWrapSelectorWheel(false);

        heightPointPicker.setMinValue(0);
        heightPointPicker.setMaxValue(9);
        heightPointPicker.setValue(0);
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
        weightPicker.setMinValue(30);
        weightPicker.setMaxValue(200);
        weightPicker.setValue(60);
        weightPicker.setWrapSelectorWheel(false);

        weightPointPicker.setMinValue(0);
        weightPointPicker.setMaxValue(9);
        weightPointPicker.setValue(0);
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

        Man_or_Woman.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { //라디오 버튼 남자 or 여자 선택
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.manRadioBtn:
                        gender = "man";
                        break;
                    case R.id.womanRadioBtn:
                        gender = "woman";
                        break;
                }
            }
        });

        joinBtn = findViewById(R.id.joinJoinBtn);
        joinBtn.setOnClickListener(v -> {
            String userID = idTxt.getText().toString();
            String userPass = pwTxt.getText().toString();
            String userName = nameTxt.getText().toString();
            String userGender = gender;

            // 나이 값 읽어오기
            int userAge = Integer.parseInt(calculateAgeTxt.getText().toString());
            // 키 값 읽어오기
            Float userHeight = Float.parseFloat(calculateHeightIntTxt.getText().toString()) + Float.parseFloat(calculateHeightPointTxt.getText().toString()) * 0.1f;
            //int userHeight = Integer.parseInt(height.toString());

            // 몸무게 값 읽어오기
            Float userWeight = Float.parseFloat(calculateWeightIntTxt.getText().toString()) + Float.parseFloat(calculateWeightPointTxt.getText().toString()) * 0.1f;
            //int userWeight = Integer.parseInt(weight.toString());

            //DB 추가
            Response.Listener<String> responseListener = response -> {
                try {
                    System.out.println(response);
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) { // 회원등록에 성공한 경우
                        Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class)); // 로그인 액티비티로 전환
                        finish();
                    } else { // 회원등록에 실패한 경우
                        String answer = jsonObject.getString("answer");
                        if(answer.equals("id")){ //id중복
                            Toast.makeText(getApplicationContext(), "존재하는 아이디입니다", Toast.LENGTH_SHORT).show();
                        }
                        else if(answer.equals("name")){ //이름 중복
                            Toast.makeText(getApplicationContext(), "이미 사용중인 이름입니다", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            };
            // 서버로 Volley를 이용해서 요청을 함.
            //RegisterRequest.java 이동
            RegisterRequest registerRequest = new RegisterRequest(userID, userPass, userName, userGender, userAge, userHeight, userWeight, responseListener);
            RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
            queue.add(registerRequest);
        });
    }

    // 영어(소문자), 숫자만 입력을 위한 필터
    protected InputFilter filter = (source, start, end, dest, dstart, dend) -> {
        Pattern ps = Pattern.compile("^[a-z0-9]+$");
        if (!ps.matcher(source).matches()) {
            return "";
        }
        return null;
    };
}