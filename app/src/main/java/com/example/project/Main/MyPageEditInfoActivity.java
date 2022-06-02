package com.example.project.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.project.Login.HashtagInfo;
import com.example.project.Login.IntroActivity;
import com.example.project.R;
import com.example.project.Ranking.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MyPageEditInfoActivity extends AppCompatActivity {

    TextView nameTxt;
    TextView idTxt;
    TextView pwTxt;
    TextView genderTxt;
    TextView heightTxt;
    TextView weightTxt;
    TextView ageTxt;
    TextView levelTxt;

    Button editInfoBtn1;
    Button editInfoBtn2;
    Button editInfoBtn3;
    Button deleteBtn;
    Button gotoHome;
    Dialog dialog_account; // 계정 정보 수정 다이얼로그
    Dialog dialog_physical; // 신체 정보 수정 다이얼로그
    Dialog dialog_delete; // 탈퇴 다이얼로그
    Dialog dialog_prefer; // 선호 정보 수정 다이얼로그

    CheckBox checkBox0;
    CheckBox checkBox1;
    CheckBox checkBox2;
    CheckBox checkBox3;
    CheckBox checkBox4;
    CheckBox checkBox5;
    CheckBox checkBox6;
    CheckBox checkBox7;
    CheckBox checkBox8;
    CheckBox checkBox9;
    CheckBox checkBox10;

    HashtagInfo hashTags[] = {
            new HashtagInfo("공원", UserInfo.getInstance().getTag_park(), 0),
            new HashtagInfo("산", UserInfo.getInstance().getTag_mountain(), 1),
            new HashtagInfo("숲", UserInfo.getInstance().getTag_forest(), 2),
            new HashtagInfo("바다", UserInfo.getInstance().getTag_sea(), 3),
            new HashtagInfo("해변", UserInfo.getInstance().getTag_beach(), 4),
            new HashtagInfo("트레킹", UserInfo.getInstance().getTag_trekking(), 5),
            new HashtagInfo("자연", UserInfo.getInstance().getTag_nature(), 6),
            new HashtagInfo("명소", UserInfo.getInstance().getTag_sights(), 7),
            new HashtagInfo("동네", UserInfo.getInstance().getTag_town(), 8),
            new HashtagInfo("풍경", UserInfo.getInstance().getTag_scenery(),9),
            new HashtagInfo("역사", UserInfo.getInstance().getTag_history(), 10)
    };


    private final String[] pickerVals = new String[] {"쉬움", "보통", "어려움"};

    @Override
    protected void onPause() {
        super.onPause();
    }

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
        levelTxt = findViewById(R.id.levelTxt);

        checkBox0 = findViewById(R.id.check0);
        checkBox1 = findViewById(R.id.check1);
        checkBox2 = findViewById(R.id.check2);
        checkBox3 = findViewById(R.id.check3);
        checkBox4 = findViewById(R.id.check4);
        checkBox5 = findViewById(R.id.check5);
        checkBox6 = findViewById(R.id.check6);
        checkBox7 = findViewById(R.id.check7);
        checkBox8 = findViewById(R.id.check8);
        checkBox9 = findViewById(R.id.check9);
        checkBox10 = findViewById(R.id.check10);

        setUserData();
        setHashtagChecked(); // 유저가 회원가입시 선택했던 해시태그들만 불 들어오게 하는... 메소드

        dialog_account = new Dialog(MyPageEditInfoActivity.this);
        dialog_account.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_account.setContentView(R.layout.popup_edit_account_inform);
        dialog_account.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); // 뒤에 하얀 배경 안 나오게
        dialog_account.setCanceledOnTouchOutside(false); // 외부 터치 시 꺼지는 현상 막기

        dialog_physical = new Dialog(MyPageEditInfoActivity.this);
        dialog_physical.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_physical.setContentView(R.layout.popup_edit_physical_inform);
        dialog_physical.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); // 뒤에 하얀 배경 안 나오게
        dialog_physical.setCanceledOnTouchOutside(false); // 외부 터치 시 꺼지는 현상 막기

        dialog_delete = new Dialog(MyPageEditInfoActivity.this);
        dialog_delete.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_delete.setContentView(R.layout.popup_delete_my_account);
        dialog_delete.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); // 뒤에 하얀 배경 안 나오게
        dialog_delete.setCanceledOnTouchOutside(false); // 외부 터치 시 꺼지는 현상 막기

        dialog_prefer = new Dialog(MyPageEditInfoActivity.this);
        dialog_prefer.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_prefer.setContentView(R.layout.popup_edit_prefer_inform);
        dialog_prefer.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); // 뒤에 하얀 배경 안 나오게
        dialog_prefer.setCanceledOnTouchOutside(false); // 외부 터치 시 꺼지는 현상 막기

        editInfoBtn1 = findViewById(R.id.editInfoBtn1);
        editInfoBtn2 = findViewById(R.id.editInfoBtn2);
        editInfoBtn3 = findViewById(R.id.editInfoBtn3);
        deleteBtn = findViewById(R.id.deleteAccountBtn);
        gotoHome = findViewById(R.id.gotoHome);

        gotoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteDialog();
            }
        });

        editInfoBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditPreferDialog();
            }
        });
    }

    private void setHashtagChecked() {
        // 사용자가 회원가입 시 선택했던 해시태그만 선택된 모양으로 보여줌(터치는 불가능)
        if (hashTags[0].getSelected() == 1) {
            checkBox0.setChecked(true);
        } else {
            checkBox0.setChecked(false);
        }
        if (hashTags[1].getSelected() == 1) {
            checkBox1.setChecked(true);
        }
        else {
            checkBox1.setChecked(false);
        }
        if (hashTags[2].getSelected() == 1) {
            checkBox2.setChecked(true);
        }
        else {
            checkBox2.setChecked(false);
        }
        if (hashTags[3].getSelected() == 1) {
            checkBox3.setChecked(true);
        }
        else {
            checkBox3.setChecked(false);
        }
        if (hashTags[4].getSelected() == 1) {
            checkBox4.setChecked(true);
        }
        else {
            checkBox4.setChecked(false);
        }
        if (hashTags[5].getSelected() == 1) {
            checkBox5.setChecked(true);
        }
        else {
            checkBox5.setChecked(false);
        }
        if (hashTags[6].getSelected() == 1) {
            checkBox6.setChecked(true);
        }
        else {
            checkBox6.setChecked(false);
        }
        if (hashTags[7].getSelected() == 1) {
            checkBox7.setChecked(true);
        }
        else {
            checkBox7.setChecked(false);
        }
        if (hashTags[8].getSelected() == 1) {
            checkBox8.setChecked(true);
        }
        else {
            checkBox8.setChecked(false);
        }
        if (hashTags[9].getSelected() == 1) {
            checkBox9.setChecked(true);
        }
        else {
            checkBox9.setChecked(false);
        }
        if (hashTags[10].getSelected() == 1) {
            checkBox10.setChecked(true);
        }else {
            checkBox10.setChecked(false);
        }
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

        if (UserInfo.getInstance().getUserLevelLike() == 0) {
            levelTxt.setText("쉬움");
        }
        else if (UserInfo.getInstance().getUserLevelLike() == 1) {
            levelTxt.setText("보통");
        }
        else {
            levelTxt.setText("어려움");
        }
    }
    private void showDeleteDialog(){
        dialog_delete.show();
        Button okBtn = dialog_delete.findViewById(R.id.okBtn);
        Button cancelBtn = dialog_delete.findViewById(R.id.cancelBtn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = response -> {
                    try {
                        System.out.println(response);
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) { // 회원탈퇴에 성공한 경우
                            clearBackStack();
                            dialog_delete.dismiss();
                            Intent intent = new Intent(MyPageEditInfoActivity.this, IntroActivity.class);// 메인 액티비티로 전환
                            startActivity(intent);
                            SharedPreferences sharedPreferences = getSharedPreferences("sharedPreferences", Activity.MODE_PRIVATE);
                            //Editor를 preferences에 쓰겠다고 연결
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.commit();
                            finish();
                            Toast.makeText(getApplicationContext(), "탈퇴 처리되었습니다.", Toast.LENGTH_SHORT).show();
                        } else { // 회원탈퇴에 실패한 경우
                            Toast.makeText(getApplicationContext(), "탈퇴 실패", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };
                DeleteAccountRequest deleteAccountRequest = new DeleteAccountRequest(UserInfo.getInstance().getUserID()+"", responseListener);
                RequestQueue queue = Volley.newRequestQueue(MyPageEditInfoActivity.this);
                queue.add(deleteAccountRequest);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_delete.dismiss();
            }
        });
    }
    private void clearBackStack() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        while (fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStackImmediate();
        }
    }
    private void exitProgram() {
        // 태스크를 백그라운드로 이동
         moveTaskToBack(true);

        if (Build.VERSION.SDK_INT >= 21) {
            // 액티비티 종료 + 태스크 리스트에서 지우기
            finishAndRemoveTask();
        } else {
            // 액티비티 종료
            finish();
        }
        System.exit(0);
    }

    private void showEditPreferDialog() {
        dialog_prefer.show();

        final int[] selectedDifficulty = new int[1];

        CheckBox check0 = dialog_prefer.findViewById(R.id.check0);
        CheckBox check1 = dialog_prefer.findViewById(R.id.check1);
        CheckBox check2 = dialog_prefer.findViewById(R.id.check2);
        CheckBox check3 = dialog_prefer.findViewById(R.id.check3);
        CheckBox check4 = dialog_prefer.findViewById(R.id.check4);
        CheckBox check5 = dialog_prefer.findViewById(R.id.check5);
        CheckBox check6 = dialog_prefer.findViewById(R.id.check6);
        CheckBox check7 = dialog_prefer.findViewById(R.id.check7);
        CheckBox check8 = dialog_prefer.findViewById(R.id.check8);
        CheckBox check9 = dialog_prefer.findViewById(R.id.check9);
        CheckBox check10 = dialog_prefer.findViewById(R.id.check10);

        if (hashTags[0].getSelected() == 1) {
            check0.setChecked(true);
        }
        if (hashTags[1].getSelected() == 1) {
            check1.setChecked(true);
        }
        if (hashTags[2].getSelected() == 1) {
            check2.setChecked(true);
        }
        if (hashTags[3].getSelected() == 1) {
            check3.setChecked(true);
        }
        if (hashTags[4].getSelected() == 1) {
            check4.setChecked(true);
        }
        if (hashTags[5].getSelected() == 1) {
            check5.setChecked(true);
        }
        if (hashTags[6].getSelected() == 1) {
            check6.setChecked(true);
        }
        if (hashTags[7].getSelected() == 1) {
            check7.setChecked(true);
        }
        if (hashTags[8].getSelected() == 1) {
            check8.setChecked(true);
        }
        if (hashTags[9].getSelected() == 1) {
            check9.setChecked(true);
        }
        if (hashTags[10].getSelected() == 1) {
            check10.setChecked(true);
        }

        NumberPicker difficultyPicker = dialog_prefer.findViewById(R.id.difficultyPicker);
        Button okBtn = dialog_prefer.findViewById(R.id.okBtn);
        Button cancelBtn = dialog_prefer.findViewById(R.id.cancelBtn);

        check0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingSelectedInfo(check0, 0);
            }
        });
        check1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingSelectedInfo(check1, 1);
            }
        });
        check2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingSelectedInfo(check2, 2);
            }
        });
        check3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingSelectedInfo(check3, 3);
            }
        });
        check4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingSelectedInfo(check4, 4);
            }
        });
        check5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingSelectedInfo(check5, 5);
            }
        });
        check6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingSelectedInfo(check6, 6);
            }
        });
        check7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingSelectedInfo(check7, 7);
            }
        });
        check8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingSelectedInfo(check8, 8);
            }
        });
        check9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingSelectedInfo(check9, 9);
            }
        });
        check10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingSelectedInfo(check10, 10);
            }
        });

        difficultyPicker.setDisplayedValues(pickerVals);
        difficultyPicker.setMinValue(0);
        difficultyPicker.setMaxValue(2);

        difficultyPicker.setValue(UserInfo.getInstance().getUserLevelLike()); // 설문조사 때 유저가 선택했던 값으로 설정해야함
        selectedDifficulty[0] = UserInfo.getInstance().getUserLevelLike();

        difficultyPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedDifficulty[0] = newVal;
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 해시태그 선택 유무 정보((int) 0 or 1) -> hasgTags[0~10].getSelected
                // 새로 선택한 코스 난이도((int) 0, 1, 2) -> selectedDifficulty[0]

                UserInfo.getInstance().setUserLevelLike(selectedDifficulty[0]);
                UserInfo.getInstance().setTag_park(hashTags[0].getSelected());
                UserInfo.getInstance().setTag_mountain(hashTags[1].getSelected());
                UserInfo.getInstance().setTag_forest(hashTags[2].getSelected());
                UserInfo.getInstance().setTag_sea(hashTags[3].getSelected());
                UserInfo.getInstance().setTag_beach(hashTags[4].getSelected());
                UserInfo.getInstance().setTag_trekking(hashTags[5].getSelected());
                UserInfo.getInstance().setTag_nature(hashTags[6].getSelected());
                UserInfo.getInstance().setTag_sights(hashTags[7].getSelected());
                UserInfo.getInstance().setTag_town(hashTags[8].getSelected());
                UserInfo.getInstance().setTag_scenery(hashTags[9].getSelected());
                UserInfo.getInstance().setTag_history(hashTags[10].getSelected());

                dialog_prefer.dismiss();
                Toast.makeText(getApplication(), "정보가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                setHashtagChecked();
                setUserData();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_prefer.dismiss();
            }
        });
    }

    private void settingSelectedInfo(CheckBox checkBox, int index) {
        if (checkBox.isChecked()) {
            hashTags[index].setSelected(1);
        }
        else {
            hashTags[index].setSelected(0);
        }
    }

    private void showEditAccountDialog() {
        dialog_account.show();

        ClearEditText editNameTxt = dialog_account.findViewById(R.id.editNameTxt);
        TextView editIdTxt = dialog_account.findViewById(R.id.editIdTxt);
        ClearEditText editPwTxt = dialog_account.findViewById(R.id.editPwTxt);

        editNameTxt.setText(nameTxt.getText());
        editIdTxt.setText(idTxt.getText());
        editPwTxt.setText(pwTxt.getText());

        Button okBtn = dialog_account.findViewById(R.id.okBtn);
        Button cancelBtn = dialog_account.findViewById(R.id.cancelBtn);

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
                            dialog_account.dismiss();
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
                RequestQueue queue = Volley.newRequestQueue(MyPageEditInfoActivity.this);
                queue.add(editAccountRequest);

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_account.dismiss();
            }
        });
    }

    private void showEditPhysicalDialog() {
        dialog_physical.show();

        TextView calculateAgeTxt = dialog_physical.findViewById(R.id.calculateAgeTxt);
        calculateAgeTxt.setText(UserInfo.getInstance().getUserAge());

        TextView calculateHeightIntTxt = dialog_physical.findViewById(R.id.calculateHeightIntTxt);
        calculateHeightIntTxt.setText(getHeightIntVal());

        TextView calculateHeightPointTxt = dialog_physical.findViewById(R.id.calculateHeightPointTxt);
        calculateHeightPointTxt.setText(getHeightPointVal());

        TextView calculateWeightIntTxt = dialog_physical.findViewById(R.id.calculateWeightIntTxt);
        calculateWeightIntTxt.setText(getWeightIntVal());

        TextView calculateWeightPointTxt = dialog_physical.findViewById(R.id.calculateWeightPointTxt);
        calculateWeightPointTxt.setText(getWeightPointVal());

        NumberPicker agePicker = dialog_physical.findViewById(R.id.agePicker);
        NumberPicker heightPicker = dialog_physical.findViewById(R.id.heightPicker);
        NumberPicker heightPointPicker = dialog_physical.findViewById(R.id.heightPointPicker);
        NumberPicker weightPicker = dialog_physical.findViewById(R.id.weightPicker);
        NumberPicker weightPointPicker = dialog_physical.findViewById(R.id.weightPointPicker);
        RadioGroup Man_or_Woman = dialog_physical.findViewById(R.id.radioGroup);
        System.out.println("================gender"+UserInfo.getInstance().getUserGender());
        final String[] gender = new String[1];

        if(UserInfo.getInstance().getUserGender().equals("man")) {
            gender[0] = "man";
            Man_or_Woman.check(R.id.manRadioBtn);
            genderTxt.setText("남");
        }else{
            gender[0] = "woman";
            Man_or_Woman.check(R.id.womanRadioBtn);
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
        Button okBtn = dialog_physical.findViewById(R.id.okBtn);
        Button cancelBtn = dialog_physical.findViewById(R.id.cancelBtn);

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

        int[] cAge = new int[1];
        cAge[0] = userAge;
        agePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                String val = String.valueOf(thisYear - newVal + 1);
                calculateAgeTxt.setText(val);
                System.out.println("나이=============="+calculateAgeTxt.getText());
                cAge[0] = Integer.parseInt(val);
                //UserInfo.getInstance().setUserAge(val);
            }
        });

        // <---- 키 ---->
        //int height = Integer.parseInt(UserInfo.getInstance().getUserHeight());
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
        //int weight = Integer.parseInt(UserInfo.getInstance().getUserWeight());

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
                UserInfo.getInstance().setUserAge(String.valueOf(cAge[0])); // 새로운 나이 설정

                // 키 값 읽어오기
                Float height = Float.parseFloat(calculateHeightIntTxt.getText().toString()) + Float.parseFloat(calculateHeightPointTxt.getText().toString()) * 0.1f;
                //int height_i = Integer.parseInt(height.toString());
                UserInfo.getInstance().setUserHeight(height.toString()); // 새로운 키 설정

                // 몸무게 값 읽어오기
                Float weight = Float.parseFloat(calculateWeightIntTxt.getText().toString()) + Float.parseFloat(calculateWeightPointTxt.getText().toString()) * 0.1f;
                //int weight_i = Integer.parseInt(weight.toString());
                UserInfo.getInstance().setUserWeight(weight.toString()); // 새로운 몸무게 설정


                // ****** 여기서 사용자가 입력한 값으로 변경 처리 ******
                Response.Listener<String> responseListener = response -> {
                    try {
                        System.out.println(response);
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) { // 회원등록에 성공한 경우
                            RadioButton a = dialog_physical.findViewById(R.id.manRadioBtn);
                            // 새로운 성별 설정
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

                            dialog_physical.dismiss();
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
                        cAge[0],
                        height,
                        weight,
                        responseListener);
                RequestQueue queue = Volley.newRequestQueue(MyPageEditInfoActivity.this);
                queue.add(editPhysicalRequest);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_physical.dismiss();
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