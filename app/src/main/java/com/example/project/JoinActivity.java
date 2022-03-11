package com.example.project;

import static java.sql.DriverManager.println;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class JoinActivity extends AppCompatActivity {
    private TextInputEditText idTxt,pwTxt,nameTxt,genderTxt;
    private EditText ageTxt,heightTxt,weightTxt;
    private RadioGroup Man_or_Woman;
    private Button joinBtn;
    private String gender = "man";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        idTxt = findViewById(R.id.joinIdTxt);
        pwTxt = findViewById(R.id.joinPwTxt);
        nameTxt = findViewById(R.id.joinNameTxt);
        Man_or_Woman = findViewById(R.id.radioGroup);
        ageTxt = findViewById(R.id.joinAgeTxt);
        heightTxt = findViewById(R.id.joinHeightTxt);
        weightTxt = findViewById(R.id.joinWeightTxt);

        // 입력 시 처음 뜨는 키보드 언어 강제 설정(이라는데 효과가 없는 것 같습니다)
        idTxt.setPrivateImeOptions("defaultInputmode=english;");
        // 아이디는 한글, 특수문자, 이모지 등은 제한하고 영어 소문자, 숫자만 최대 20글자 입력 허용
        // (참고 : 비밀번호는 영어(소,대), 숫자, 특수문자만 최대 20글자 입력 가능)
        idTxt.setFilters(new InputFilter[] {filter, new InputFilter.LengthFilter(20)});

        Man_or_Woman.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){ //라디오 버튼 남자 or 여자 선택
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId){
                switch (checkedId){
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
            int userAge = Integer.parseInt(ageTxt.getText().toString());
            int userheight = Integer.parseInt(heightTxt.getText().toString());
            int userweight = Integer.parseInt(weightTxt.getText().toString());
                    Response.Listener<String> responseListener = response -> {
                        try {
                            System.out.println(response);
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success) { // 회원등록에 성공한 경우
                                Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(JoinActivity.this, LoginActivity.class)); // 로그인 액티비티로 전환
                                finish();
                            } else { // 회원등록에 실패한 경우
                                Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    };
            // 서버로 Volley를 이용해서 요청을 함.
            //RegisterRequest.java 이동
            RegisterRequest registerRequest = new RegisterRequest(userID,userPass,userName,userGender,userAge,userheight,userweight,responseListener);
            RequestQueue queue = Volley.newRequestQueue(JoinActivity.this);
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