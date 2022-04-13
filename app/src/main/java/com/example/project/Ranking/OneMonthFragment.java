package com.example.project.Ranking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

import java.util.ArrayList;

public class OneMonthFragment extends Fragment {
    RecyclerView recyclerView;
    RankingAdapter rankingAdapter;

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
        recyclerView = (RecyclerView) v.findViewById(R.id.month_pedo_recycler);

        setRecyclerView();
        createMyRank("10", ProfileDrawable[1], "User1266", "612,709");
        createRankOne();
        createList();
        return v;
    }

    private void createMyRank(String index, int profile, String id, String step) {
        myIndex.setText(index);
        myProfile.setImageResource(profile);
        myID.setText(id);
        myStep.setText(step);
    }

    private void createList(){
        ArrayList<RankingModel> rankingModels = new ArrayList<>();
        rankingModels.add(new RankingModel("2", ProfileDrawable[1], "나이스러너", "3,566,851"));
        rankingModels.add(new RankingModel("3", ProfileDrawable[2], "할머니아님", "2,748,110"));
        rankingModels.add(new RankingModel("4", ProfileDrawable[3], "섹쉬한궁뒤", "1,556,228"));
        rankingModels.add(new RankingModel("5", ProfileDrawable[4], "청담소녀", "1,223,253"));
        rankingModels.add(new RankingModel("6", ProfileDrawable[5], "바디빌더후니", "905,775"));
        rankingModels.add(new RankingModel("7", ProfileDrawable[2], "29살간지남", "665,108"));
        rankingModels.add(new RankingModel("8", ProfileDrawable[3], "User1234", "662,551"));
        rankingModels.add(new RankingModel("9", ProfileDrawable[4], "User1237", "644,992"));
        rankingModels.add(new RankingModel("10", ProfileDrawable[1], "User1266", "612,709"));
        rankingModels.add(new RankingModel("11", ProfileDrawable[5], "User123", "600,154"));
        rankingModels.add(new RankingModel("12", ProfileDrawable[0], "User12", "455,162"));
        rankingModels.add(new RankingModel("13", ProfileDrawable[2], "User34", "325,331"));
        rankingModels.add(new RankingModel("14", ProfileDrawable[4], "User23", "165,227"));
        rankingModels.add(new RankingModel("15", ProfileDrawable[3], "User14", "65,111"));
        rankingModels.add(new RankingModel("16", ProfileDrawable[5], "User1534", "5,109"));
        rankingModels.add(new RankingModel("17", ProfileDrawable[3], "User1613", "122"));
        rankingModels.add(new RankingModel("18", ProfileDrawable[2], "User1226", "65"));
        rankingAdapter.setRankList(rankingModels);
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        rankingAdapter = new RankingAdapter();
        recyclerView.setAdapter(rankingAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void createRankOne() {
        ArrayList<RankOneModel> rankOneModels = new ArrayList<>();
        rankOneModels.add(new RankOneModel(ProfileDrawable[0],"Hansung1998", "52,114,852"));
        rankingAdapter.setRank1List(rankOneModels);
    }
}