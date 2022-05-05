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

public class PedoOneMonthFragment extends Fragment {
    RecyclerView recyclerView;
    RankingAdapter rankingAdapter;
    String pedoMonthRankingJsonString;
    ArrayList<pedoMonthRankingData> pedoMonthRankingArrayList;
    String userName = UserInfo.getInstance().getUserName();
    int myIndexNumber;
    private static final String TAG_JSON="pacerfit";
    private static final String TAG_NAME = "userName";
    private static final String TAG_ID = "userID";
    private static final String TAG_MONTHSUM = "month_sum";


    TextView myIndex;
    ImageView myProfile;
    TextView myID;
    TextView myStep;

    int[] ProfileDrawable = {R.drawable.profile_man_horn, R.drawable.profile_man_beard, R.drawable.profile_woman_old,
            R.drawable.profile_woman_scarf, R.drawable.profile_woman_neck, R.drawable.profile_man_hood, R.drawable.profile_man_round};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ranking_pedo_month_fragment, container, false);


        myIndex = v.findViewById(R.id.myrank_index);
        myProfile = v.findViewById(R.id.myrank_profile);
        myID = v.findViewById(R.id.myrank_id);
        myStep = v.findViewById(R.id.myrank_step);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler);
        pedoMonthRankingArrayList = new ArrayList<>();


        getPedoMonthRanking task = new getPedoMonthRanking();
        task.execute("http://pacerfit.dothome.co.kr/oneMonthPedoRanking.php");

        return v;
    }

    private void createMyRank(int index, int profile, String id, int step) { //내 랭킹 출력
        DecimalFormat myFormatter = new DecimalFormat("###,###");
        myIndex.setText(String.valueOf(index+1));
        myProfile.setImageResource(profile);
        myID.setText(id);
        myStep.setText(myFormatter.format(step));
    }

    private void createList(){  //랭킹 리스트 출력
        ArrayList<RankingModel> rankingModels = new ArrayList<>();
        for(int i=1;i<pedoMonthRankingArrayList.size();i++){
            if(i!=myIndexNumber){
                int randomNum = (int) (Math.random() * 7);
                rankingModels.add(new RankingModel(String.valueOf(i+1),ProfileDrawable[randomNum],
                        pedoMonthRankingArrayList.get(i).userName,pedoMonthRankingArrayList.get(i).month_sum));
            }
        }
        rankingAdapter.setRankList(rankingModels);
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        rankingAdapter = new RankingAdapter();
        recyclerView.setAdapter(rankingAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void createRankOne() {  //1등 랭킹 출력
        int randomNum = (int) (Math.random() * 7);
        ArrayList<RankOneModel> rankOneModels = new ArrayList<>();
        rankOneModels.add(new RankOneModel(ProfileDrawable[randomNum],pedoMonthRankingArrayList.get(0).userName, pedoMonthRankingArrayList.get(0).month_sum));
        rankingAdapter.setRank1List(rankOneModels);
    }


    private class getPedoMonthRanking extends AsyncTask<String, Void, String> {  // DB에서 월간랭킹데이터 받아오는 부분
        String errorString = null;

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            Log.d(TAG, "response  - " + result);
            if (result == null){
                Log.d(TAG, errorString);
            }
            else {
                pedoMonthRankingJsonString = result;
                showResult();
                setRecyclerView();

                for(int i=0;i<pedoMonthRankingArrayList.size();i++){
                    if(pedoMonthRankingArrayList.get(i).userName.equals(userName))
                        myIndexNumber = i;
                }
                int randomNum = (int) (Math.random() * 7);

                createMyRank(myIndexNumber, ProfileDrawable[randomNum], userName, pedoMonthRankingArrayList.get(myIndexNumber).month_sum);
                createRankOne();
                createList();

            }
        }

        private void showResult(){
            try {
                JSONObject jsonObject = new JSONObject(pedoMonthRankingJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for(int i=0;i<jsonArray.length();i++){

                    JSONObject item = jsonArray.getJSONObject(i);

                    String userName = item.getString(TAG_NAME);
                    String userID = item.getString(TAG_ID);
                    int month_sum = item.getInt(TAG_MONTHSUM);

                    pedoMonthRankingData pedoMonthRankingData = new pedoMonthRankingData();
                    pedoMonthRankingData.setUserName(userName);
                    pedoMonthRankingData.setUserID(userID);
                    pedoMonthRankingData.setMonth_sum(month_sum);

                    pedoMonthRankingArrayList.add(pedoMonthRankingData);

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

    private class pedoMonthRankingData{  //DB에서 받은 데이터를 저장할 클래스
            private String userName;
            private String userID;
            private int month_sum;

            public String getUserID(){
                return userID;
            }
            public String getUserName(){
                return userName;
            }
            public int getMonth_sum(){
                return month_sum;
            }
            public void setUserName(String userName){
                this.userName = userName;
            }
            public void setUserID(String userID){
                this.userID = userID;
            }
            public void setMonth_sum(int month_sum){
                this.month_sum = month_sum;
            }
    }
}