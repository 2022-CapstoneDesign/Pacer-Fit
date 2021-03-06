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
import com.example.project.Ranking.UserInfo;
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

        // ?????? ??? ?????? ?????? ????????? ?????? ?????? ??????(???????????? ????????? ?????? ??? ????????????)
        idTxt.setPrivateImeOptions("defaultInputmode=english;");
        // ???????????? ??????, ????????????, ????????? ?????? ???????????? ?????? ?????????, ????????? ?????? 20?????? ?????? ??????
        // (?????? : ??????????????? ??????(???,???), ??????, ??????????????? ?????? 20?????? ?????? ??????)
        idTxt.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(20)});


        // <---- ?????? ---->
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

        // <---- ??? ---->
        heightPicker.setMaxValue(200);
        heightPicker.setMinValue(50);
        heightPicker.setValue(170);
        heightPicker.setWrapSelectorWheel(false);

        heightPointPicker.setMinValue(0);
        heightPointPicker.setMaxValue(9);
        heightPointPicker.setValue(0);
        heightPointPicker.setWrapSelectorWheel(true);

        // ????????????
        heightPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String val = String.valueOf(newVal);
                calculateHeightIntTxt.setText(val);
            }
        });
        // ?????????
        heightPointPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String val = String.valueOf(newVal);
                calculateHeightPointTxt.setText(val);
            }
        });

        // <---- ????????? ---->
        weightPicker.setMinValue(30);
        weightPicker.setMaxValue(200);
        weightPicker.setValue(60);
        weightPicker.setWrapSelectorWheel(false);

        weightPointPicker.setMinValue(0);
        weightPointPicker.setMaxValue(9);
        weightPointPicker.setValue(0);
        weightPointPicker.setWrapSelectorWheel(true);

        // ????????????
        weightPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String val = String.valueOf(newVal);
                calculateWeightIntTxt.setText(val);
            }
        });
        // ?????????
        weightPointPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String val = String.valueOf(newVal);
                calculateWeightPointTxt.setText(val);
            }
        });

        Man_or_Woman.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { //????????? ?????? ?????? or ?????? ??????
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

            // ?????? ??? ????????????
            int userAge = Integer.parseInt(calculateAgeTxt.getText().toString());
            // ??? ??? ????????????
            Float userHeight = Float.parseFloat(calculateHeightIntTxt.getText().toString()) + Float.parseFloat(calculateHeightPointTxt.getText().toString()) * 0.1f;
            //int userHeight = Integer.parseInt(height.toString());

            // ????????? ??? ????????????
            Float userWeight = Float.parseFloat(calculateWeightIntTxt.getText().toString()) + Float.parseFloat(calculateWeightPointTxt.getText().toString()) * 0.1f;
            //int userWeight = Integer.parseInt(weight.toString());

            //DB ??????
            Response.Listener<String> responseListener = response -> {
                try {
                    System.out.println(response);
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) { // ??????????????? ????????? ??????
                        UserInfo.getInstance().setUserID(userID);
                        Toast.makeText(getApplicationContext(), "???????????? ??????", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, SurveyActivity.class)); // ???????????? ??????????????? ??????
                        finish();
                    } else { // ??????????????? ????????? ??????
                        String answer = jsonObject.getString("answer");
                        if(answer.equals("id")){ //id??????
                            Toast.makeText(getApplicationContext(), "???????????? ??????????????????", Toast.LENGTH_SHORT).show();
                        }
                        else if(answer.equals("name")){ //?????? ??????
                            Toast.makeText(getApplicationContext(), "?????? ???????????? ???????????????", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "???????????? ??????", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            };
            // ????????? Volley??? ???????????? ????????? ???.
            //RegisterRequest.java ??????
            RegisterRequest registerRequest = new RegisterRequest(userID, userPass, userName, userGender, userAge, userHeight, userWeight, responseListener);
            RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
            queue.add(registerRequest);
        });
    }

    // ??????(?????????), ????????? ????????? ?????? ??????
    protected InputFilter filter = (source, start, end, dest, dstart, dend) -> {
        Pattern ps = Pattern.compile("^[a-z0-9]+$");
        if (!ps.matcher(source).matches()) {
            return "";
        }
        return null;
    };
}