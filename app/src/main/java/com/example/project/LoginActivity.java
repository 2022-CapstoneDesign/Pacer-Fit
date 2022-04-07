package com.example.project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private final int ACCESS_FINE_LOCATION = 1000;
    private TextInputEditText idTxt, passTxt;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        idTxt = findViewById(R.id.loginIdTxt);
        passTxt = findViewById(R.id.loginPwTxt);
        // 입력 시 처음 뜨는 키보드 언어 강제 설정(이라는데 효과가 없는 것 같습니다)
        idTxt.setPrivateImeOptions("defaultInputmode=english;");
        // 아이디는 한글, 특수문자, 이모지 등은 제한하고 영어 소문자, 숫자만 최대 20글자 입력 허용
        // (참고 : 비밀번호는 영어(소,대), 숫자, 특수문자만 최대 20글자 입력 가능)
        idTxt.setFilters(new InputFilter[] {filter, new InputFilter.LengthFilter(20)});



        loginBtn = findViewById(R.id.loginLoginBtn);
        loginBtn.setOnClickListener(v -> {
            String userID = idTxt.getText().toString();
            String userPass = passTxt.getText().toString();
            // DB추가
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                       // System.out.println("========================" + response);
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) { // 로그인에 성공한 경우
                            String userID = jsonObject.getString("userID");
                            String userPass = jsonObject.getString("userPassword");
                            String userName = jsonObject.getString("userName");
                            String bestSteps = jsonObject.getString("bestSteps");
                            String bestKm = jsonObject.getString("bestKm");
                            String bestTime_Km = jsonObject.getString("bestTime(km)");
                            String bestCalorie_Km = jsonObject.getString("bestCalorie(km)");
                            String bestTime_Steps = jsonObject.getString("bestTime(steps)");
                            String bestCalorie_Steps = jsonObject.getString("bestCalorie(steps)");
                            Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);// 메인 액티비티로 전환
                            intent.putExtra("userID", userID);
                            intent.putExtra("userPass", userPass);
                            intent.putExtra("userName",userName);
                            intent.putExtra("bestSteps", bestSteps);
                            intent.putExtra("bestKm", bestKm);
                            intent.putExtra("bestTime_Km", bestTime_Km);
                            intent.putExtra("bestCalorie_Km", bestCalorie_Km);
                            intent.putExtra("bestTime_Steps", bestTime_Steps);
                            intent.putExtra("bestCalorie_Steps", bestCalorie_Steps);
                            startActivity(intent);
                            finish();
                        } else { // 로그인에 실패한 경우
                            Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            LoginRequest loginRequest = new LoginRequest(userID, userPass, responseListener);
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            queue.add(loginRequest);

            //DB추가 주석 없앨때 아래 3줄 지우기

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