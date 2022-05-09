package com.example.project.Ranking;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class DistOneWeekFragment extends Fragment {
    RecyclerView recyclerView;
    DistRankingAdapter rankingAdapter;
    String distWeekRankingJsonString;
    ArrayList<distWeekRankingData> distWeekRankingArrayList;
    String userName = UserInfo.getInstance().getUserName();
    int userProfileNum = UserInfo.getInstance().getUserProfileNum();
    int myIndexNumber;
    private static final String TAG_JSON="pacerfit";
    private static final String TAG_NAME = "userName";
    private static final String TAG_ID = "userID";
    private static final String TAG_WEEKSUM = "week_sum";
    private static final String TAG_PROFILE = "profile_num";


    TextView myIndex;
    ImageView myProfile;
    TextView myID;
    TextView myKm;

    int[] ProfileDrawable = {
            R.drawable.profile_default, R.drawable.profile_man, R.drawable.profile_man_beard, R.drawable.profile_man_cap,
            R.drawable.profile_man_hat, R.drawable.profile_man_hood, R.drawable.profile_man_horn, R.drawable.profile_man_round,
            R.drawable.profile_man_suit, R.drawable.profile_man_sunglass, R.drawable.profile_woman_glasses, R.drawable.profile_woman_neck,
            R.drawable.profile_woman_old, R.drawable.profile_woman_scarf
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ranking_dist_week_fragment, container, false);


        myIndex = v.findViewById(R.id.myrank_index);
        myProfile = v.findViewById(R.id.myrank_profile);
        myID = v.findViewById(R.id.myrank_id);
        myKm = v.findViewById(R.id.myrank_km);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler);
        distWeekRankingArrayList = new ArrayList<>();


        getDistWeekRanking task = new getDistWeekRanking();
        task.execute("http://pacerfit.dothome.co.kr/oneWeekDistRanking.php");

        return v;
    }

    private void createMyRank(int index, int profile, String id, double km) {
        DecimalFormat myFormatter = new DecimalFormat("###,##0.0");
        myIndex.setText(String.valueOf(index+1));
        myProfile.setImageResource(profile);
        myID.setText(id);
        myKm.setText(myFormatter.format(km));
    }

    private void createList(){
        ArrayList<DistRankingModel> rankingModels = new ArrayList<>();
        for(int i=1;i<distWeekRankingArrayList.size();i++){
            if(i!=myIndexNumber){
                rankingModels.add(new DistRankingModel(String.valueOf(i+1),ProfileDrawable[distWeekRankingArrayList.get(i).profile_num],
                        distWeekRankingArrayList.get(i).userName,distWeekRankingArrayList.get(i).week_sum));
            }
        }
        rankingAdapter.setRankList(rankingModels);
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        rankingAdapter = new DistRankingAdapter();
        recyclerView.setAdapter(rankingAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void createRankOne() {
        ArrayList<DistRankOneModel> rankOneModels = new ArrayList<>();
        rankOneModels.add(new DistRankOneModel(ProfileDrawable[distWeekRankingArrayList.get(0).profile_num],
                distWeekRankingArrayList.get(0).userName, distWeekRankingArrayList.get(0).week_sum));
        rankingAdapter.setRank1List(rankOneModels);
    }


    private class getDistWeekRanking extends AsyncTask<String, Void, String> {  // DB에서 월간랭킹데이터 받아오는 부분
        String errorString = null;

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            Log.d(TAG, "response  - " + result);
            if (result == null){
                Log.d(TAG, errorString);
            }
            else {
                distWeekRankingJsonString = result;
                showResult();
                setRecyclerView();

                for(int i=0;i<distWeekRankingArrayList.size();i++){
                    if(distWeekRankingArrayList.get(i).userName.equals(userName))
                        myIndexNumber = i;
                }

                createMyRank(myIndexNumber, ProfileDrawable[userProfileNum], userName, distWeekRankingArrayList.get(myIndexNumber).week_sum);
                createRankOne();
                createList();

            }
        }

        private void showResult(){
            try {
                JSONObject jsonObject = new JSONObject(distWeekRankingJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for(int i=0;i<jsonArray.length();i++){

                    JSONObject item = jsonArray.getJSONObject(i);

                    String userName = item.getString(TAG_NAME);
                    String userID = item.getString(TAG_ID);
                    double week_sum = item.getDouble(TAG_WEEKSUM);
                    int profile_num = item.getInt(TAG_PROFILE);

                    distWeekRankingData distWeekRankingData = new distWeekRankingData();
                    distWeekRankingData.setUserName(userName);
                    distWeekRankingData.setUserID(userID);
                    distWeekRankingData.setMonth_sum(week_sum);
                    distWeekRankingData.setProfile_num(profile_num);

                    distWeekRankingArrayList.add(distWeekRankingData);

                }
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

    private class distWeekRankingData{  //DB에서 받은 데이터를 저장할 클래스
        private String userName;
        private String userID;
        private double week_sum;
        private int profile_num;

        public String getUserID(){
            return userID;
        }
        public String getUserName(){
            return userName;
        }
        public double getWeek_sum(){
            return week_sum;
        }
        public int getProfile_num() {return profile_num; }
        public void setUserName(String userName){
            this.userName = userName;
        }
        public void setUserID(String userID){
            this.userID = userID;
        }
        public void setMonth_sum(double week_sum){
            this.week_sum = week_sum;
        }
        public void setProfile_num(int profile_num) {this.profile_num = profile_num; }
    }
}