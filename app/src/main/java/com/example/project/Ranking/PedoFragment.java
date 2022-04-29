package com.example.project.Ranking;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.project.R;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public class PedoFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView member;
    String pedoMembersJsonString;
    private static final String TAG_JSON="pacerfit";
    private static final String TAG_MEMBERS="members";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ranking_pedo_fragment, container, false);


        tabLayout = v.findViewById(R.id.pedo_tabLayout);
        viewPager = v.findViewById(R.id.pedo_ranking_vp);
        member = v.findViewById(R.id.textView11);

        getPedoMembers task = new getPedoMembers();
        task.execute("http://pacerfit.dothome.co.kr/getPedoMembers.php");


        RankingVPAdapter rankingVPAdapter = new RankingVPAdapter(getActivity().getSupportFragmentManager());
        rankingVPAdapter.addFragment(new OneWeekFragment(), "주간랭킹");
        rankingVPAdapter.addFragment(new OneMonthFragment(), "월간랭킹");
        rankingVPAdapter.addFragment(new TopFragment(), "역대랭킹");

        viewPager.setAdapter(rankingVPAdapter);
        tabLayout.setupWithViewPager(viewPager);



        return v;
    }

    private class getPedoMembers extends AsyncTask<String, Void, String> {
        String errorString = null;

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            Log.d(TAG, "response  - " + result);
            if (result == null){
                Log.d(TAG, errorString);
            }
            else {
                pedoMembersJsonString = result;
                showResult();


            }
        }

        private void showResult(){
            try {
                JSONObject jsonObject = new JSONObject(pedoMembersJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                int members = 0 ;
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject item = jsonArray.getJSONObject(i);
                    members = item.getInt(TAG_MEMBERS);

                }
                DecimalFormat myFormatter = new DecimalFormat("###,###");
                member.setText(myFormatter.format(members));
            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }//연결상태 확인

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
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
