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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ranking_month_pedo, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.month_pedo_recycler);
        setRecyclerView();

        ArrayList<RankingModel> rankingModels = new ArrayList<>();
        for(int i= 2; i< 7; i++){
            rankingModels.add(new RankingModel(Integer.toString(i),R.drawable.profile_default,"이름","123,456,789"));
        }

        rankingAdapter.setRankList(rankingModels);


        return v;
    }


    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        rankingAdapter = new RankingAdapter();
        recyclerView.setAdapter(rankingAdapter);
    }
}