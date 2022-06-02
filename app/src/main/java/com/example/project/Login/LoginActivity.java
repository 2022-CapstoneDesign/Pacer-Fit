package com.example.project.Login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.project.Main.BottomNavigation;
import com.example.project.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private final int ACCESS_FINE_LOCATION = 1000;
    private TextInputEditText idTxt, passTxt;
    private Button loginBtn;
    String date_concat;

    TextInputLayout idTextInputLayout;
    TextInputLayout pwTextInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        idTextInputLayout = findViewById(R.id.idTextInputLayout);
        pwTextInputLayout = findViewById(R.id.pwTextInputLayout);

        idTxt = findViewById(R.id.loginIdTxt);
        passTxt = findViewById(R.id.loginPwTxt);
        loginBtn = findViewById(R.id.loginLoginBtn);
        // 입력 시 처음 뜨는 키보드 언어 강제 설정(이라는데 효과가 없는 것 같습니다)
        idTxt.setPrivateImeOptions("defaultInputmode=english;");
        // 아이디는 한글, 특수문자, 이모지 등은 제한하고 영어 소문자, 숫자만 최대 20글자 입력 허용
        // (참고 : 비밀번호는 영어(소,대), 숫자, 특수문자만 최대 20글자 입력 가능)
        idTxt.setFilters(new InputFilter[] {filter, new InputFilter.LengthFilter(20)});
        //오늘 날짜
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate now = null;
            now = LocalDate.now();

            int monthValue = now.getMonthValue();
            int dayOfMonth = now.getDayOfMonth();
            date_concat = monthValue+"m"+dayOfMonth+"d";
        }

        loginBtn.setOnClickListener(v -> {
            String userID = idTxt.getText().toString();
            String userPass = passTxt.getText().toString();
            // DB추가
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        System.out.println("========================" + response);
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) { // 로그인에 성공한 경우
                            String userID = jsonObject.getString("userID");
                            String userPass = jsonObject.getString("userPassword");
                            String userName = jsonObject.getString("userName");
                            String userHeight = jsonObject.getString("userHeight"); // 키
                            String userWeight = jsonObject.getString("userWeight");
                            String userGender = jsonObject.getString("userGender"); // 성별
                            String userAge =jsonObject.getString("userAge"); // 나이
                            String pedo_max = jsonObject.getString("pedo_max");
                            String km_max = jsonObject.getString("km_max");
                            int userProfileNum = jsonObject.getInt("userProfileNum");

                            String userHashtag = jsonObject.getString("myHashtag"); // #
                            String level_like = jsonObject.getString("level_like");

                            Log.d("testingHash", userHashtag);
                            Log.d("testingHash", level_like);

                            Intent intent = new Intent(LoginActivity.this, BottomNavigation.class);// 메인 액티비티로 전환
                            intent.putExtra("userID", userID);
                            intent.putExtra("userPass", userPass);
                            intent.putExtra("userName",userName);
                            intent.putExtra("userHeight", userHeight); // 키
                            intent.putExtra("userWeight", userWeight);
                            intent.putExtra("userGender", userGender); // 성별
                            intent.putExtra("userAge", userAge); // 나이
                            intent.putExtra("pedo_max",pedo_max);
                            intent.putExtra("km_max",km_max);
                            intent.putExtra("userProfileNum",userProfileNum);
                            intent.putExtra("userHashtag", userHashtag); // #
                            intent.putExtra("userLevelLike", level_like); // 선호 난이도

                            SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", Activity.MODE_PRIVATE);

                            //Editor를 preferences에 쓰겠다고 연결
                            SharedPreferences.Editor autoLogin = sharedPreferences.edit();
                            //putString(KEY,VALUE)
                            autoLogin.putString("userid",idTxt.getText().toString());
                            autoLogin.putString("userpwd", passTxt.getText().toString());
                            autoLogin.putString("userName", userName);
                            autoLogin.putString("userHeight", userHeight);
                            autoLogin.putString("userWeight", userWeight);
                            autoLogin.putString("pedo_max", pedo_max);
                            autoLogin.putString("km_max", km_max);
                            autoLogin.putString("userProfileNum", userProfileNum+"");
                            //항상 commit & apply 를 해주어야 저장이 된다.
                            autoLogin.commit();

                            startActivity(intent);
                            finish();
                        } else { // 로그인에 실패한 경우
                            YoYo.with(Techniques.Shake)
                                    .duration(700)
                                    .repeat(0)
                                    .playOn(idTextInputLayout); // 좌우로 흔들리는 애니메이션
                            YoYo.with(Techniques.Shake)
                                    .duration(700)
                                    .repeat(0)
                                    .playOn(pwTextInputLayout); // 좌우로 흔들리는 애니메이션
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
