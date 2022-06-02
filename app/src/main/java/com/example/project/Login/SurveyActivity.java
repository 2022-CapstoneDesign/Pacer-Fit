package com.example.project.Login;

import static com.example.project.Map.LocationService.TAG;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;

import com.example.project.Main.MyPageFragment;
import com.example.project.R;
import com.example.project.Ranking.UserInfo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SurveyActivity extends AppCompatActivity {

    Button okBtn;
    NumberPicker difficultyPicker;

    private final String[] pickerVals = new String[] {"쉬움", "보통", "어려움"};
    private int selectedDifficulty;

    CheckBox check0;
    CheckBox check1;
    CheckBox check2;
    CheckBox check3;
    CheckBox check4;
    CheckBox check5;
    CheckBox check6;
    CheckBox check7;
    CheckBox check8;
    CheckBox check9;
    CheckBox check10;

    HashtagInfo hashTags[] = {
            new HashtagInfo(0, 0),
            new HashtagInfo(0, 1),
            new HashtagInfo(0, 2),
            new HashtagInfo( 0, 3),
            new HashtagInfo( 0, 4),
            new HashtagInfo( 0, 5),
            new HashtagInfo( 0, 6),
            new HashtagInfo( 0, 7),
            new HashtagInfo( 0, 8),
            new HashtagInfo( 0,9),
            new HashtagInfo( 0, 10)
    };

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_survey_activity);

        okBtn = findViewById(R.id.okBtn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSelectedHashtags();

                if (selectedDifficulty == 0) {
                    Log.d("testingTAG", "쉬움 선택");
                }
                else if (selectedDifficulty == 1) {
                    Log.d("testingTAG", "보통 선택");
                }
                else {
                    Log.d("testingTAG", "어려움 선택");
                }
                uploadServeyData task = new uploadServeyData();
                task.execute("http://pacerfit.dothome.co.kr/uploadServeyData.php");
            }
        });

        difficultyPicker = findViewById(R.id.difficultyPicker);
        difficultyPicker.setDisplayedValues(pickerVals);
        difficultyPicker.setMinValue(0);
        difficultyPicker.setMaxValue(2);

        difficultyPicker.setValue(0);
        selectedDifficulty = 0;

        difficultyPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedDifficulty = newVal;
            }
        });

        check0 = findViewById(R.id.check0);
        check0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingSelectedInfo(check0, 0);
            }
        });
        check1 = findViewById(R.id.check1);
        check1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingSelectedInfo(check1, 1);
            }
        });
        check2 = findViewById(R.id.check2);
        check2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingSelectedInfo(check2, 2);
            }
        });
        check3 = findViewById(R.id.check3);
        check3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingSelectedInfo(check3, 3);
            }
        });
        check4 = findViewById(R.id.check4);
        check4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingSelectedInfo(check4, 4);
            }
        });
        check5 = findViewById(R.id.check5);
        check5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingSelectedInfo(check5, 5);
            }
        });
        check6 = findViewById(R.id.check6);
        check6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingSelectedInfo(check6, 6);
            }
        });
        check7 = findViewById(R.id.check7);
        check7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingSelectedInfo(check7, 7);
            }
        });
        check8 = findViewById(R.id.check8);
        check8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingSelectedInfo(check8, 8);
            }
        });
        check9 = findViewById(R.id.check9);
        check9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingSelectedInfo(check9, 9);
            }
        });
        check10 = findViewById(R.id.check10);
        check10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingSelectedInfo(check10, 10);
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

    private void getSelectedHashtags() {
        // ------------------- 해시태그 정보를 불러오는 메소드들입니다 -------------------
        // getTitle() : 해시태그의 이름을 불러옴(공원, 산, 숲, ...)
        // getSelected() : 유저가 해시태그를 선택했는지 알려줌(선택X->0, 선택O->1)
        // getIndex() : 해당 해시태그가 몇 번째 해쉬태그인지 알려줌(공원->0, 산->1, 숲->2, ...)
        // ------------------------------------------------------------------------
        for (int i = 0; i < 11; i++) {
            if (hashTags[i].getSelected() == 1) {
                //Log.d("testingTAG", hashTags[i].getTitle());
                Log.d("testingTAG", hashTags[i].getIndex() + "");
            }
        }
    }


    private class uploadServeyData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPostExecute(String result) { //doInBackground에서 return한 값을 받음
            super.onPostExecute(result);
            //progressDialog.dismiss();
            Log.d(TAG, "response  - " + result);
            startActivity(new Intent(SurveyActivity.this, LoginActivity.class)); // 로그인 액티비티로 전환
            finish();
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];

            try {
                String userID = "userID=" + UserInfo.getInstance().getUserID();
                String level_like = "selectedDiffculty=" + selectedDifficulty;
                String park= "park=" + hashTags[0].getSelected();
                String mountain= "mountain=" + hashTags[1].getSelected();
                String forest= "forest=" + hashTags[2].getSelected();
                String sea= "sea=" + hashTags[3].getSelected();
                String beach= "beach=" + hashTags[4].getSelected();
                String trekking= "trekking=" + hashTags[5].getSelected();
                String nature= "nature=" + hashTags[6].getSelected();
                String sights= "sights=" + hashTags[7].getSelected();
                String town= "town=" + hashTags[8].getSelected();
                String scenery= "scenery=" + hashTags[9].getSelected();
                String history= "history=" + hashTags[10].getSelected();

                StringBuffer stParams = new StringBuffer();
                stParams.append(userID);
                stParams.append("&");
                stParams.append(level_like);
                stParams.append("&");
                stParams.append(park);
                stParams.append("&");
                stParams.append(mountain);
                stParams.append("&");
                stParams.append(forest);
                stParams.append("&");
                stParams.append(sea);
                stParams.append("&");
                stParams.append(beach);
                stParams.append("&");
                stParams.append(trekking);
                stParams.append("&");
                stParams.append(nature);
                stParams.append("&");
                stParams.append(sights);
                stParams.append("&");
                stParams.append(town);
                stParams.append("&");
                stParams.append(scenery);
                stParams.append("&");
                stParams.append(history);

                String strParams = stParams.toString();
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(strParams.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
                //어플에서 데이터 전송

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }//연결상태 확인

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString().trim();
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();
                return null;
            }
        }
    }
}