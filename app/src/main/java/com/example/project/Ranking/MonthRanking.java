package com.example.project.Ranking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

import java.util.ArrayList;

public class MonthRanking extends Fragment {

    RecyclerView recyclerView;
    RankingAdapter rankingAdapter;

    int Profiledrawable[] = {R.drawable.profile_man_horn, R.drawable.profile_man_beard, R.drawable.profile_woman_old,
            R.drawable.profile_woman_scarf, R.drawable.profile_woman_neck, R.drawable.profile_man_hood, R.drawable.profile_man_round};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ranking_month_pedo, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.month_pedo_recycler);
        setRecyclerView();

        setRank1();
        createList();

        return v;
    }

    private void createList(){
        ArrayList<RankingModel> rankingModels = new ArrayList<>();
        rankingModels.add(new RankingModel("2", Profiledrawable[1], "나이스러너", "3,566,851"));
        rankingModels.add(new RankingModel("3", Profiledrawable[2], "할머니아님", "2,748,110"));
        rankingModels.add(new RankingModel("4", Profiledrawable[3], "섹쉬한궁뒤", "1,556,228"));
        rankingModels.add(new RankingModel("5", Profiledrawable[4], "청담소녀", "1,223,253"));
        rankingModels.add(new RankingModel("6", Profiledrawable[5], "바디빌더후니", "905,775"));
        rankingModels.add(new RankingModel("7", Profiledrawable[2], "29살간지남", "665,108"));
        rankingModels.add(new RankingModel("8", Profiledrawable[3], "User1234", "662,551"));
        rankingModels.add(new RankingModel("9", Profiledrawable[4], "User1237", "644,992"));
        rankingModels.add(new RankingModel("10", Profiledrawable[1], "User1266", "612,709"));
        rankingModels.add(new RankingModel("11", Profiledrawable[5], "User123", "600,154"));
        rankingModels.add(new RankingModel("12", Profiledrawable[0], "User12", "455,162"));
        rankingModels.add(new RankingModel("13", Profiledrawable[2], "User34", "325,331"));
        rankingModels.add(new RankingModel("14", Profiledrawable[4], "User23", "165,227"));
        rankingModels.add(new RankingModel("15", Profiledrawable[3], "User14", "65,111"));
        rankingModels.add(new RankingModel("16", Profiledrawable[5], "User1534", "5,109"));
        rankingModels.add(new RankingModel("17", Profiledrawable[3], "User1613", "122"));
        rankingModels.add(new RankingModel("18", Profiledrawable[2], "User1226", "65"));
        rankingAdapter.setRankList(rankingModels);
    }



    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        rankingAdapter = new RankingAdapter();
        recyclerView.setAdapter(rankingAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setRank1() {
        ArrayList<Rank1Model> rank1Models = new ArrayList<>();
        rank1Models.add(new Rank1Model(Profiledrawable[0],"Hansung1998", "52,114,852"));
        rankingAdapter.setRank1List(rank1Models);
    }
}