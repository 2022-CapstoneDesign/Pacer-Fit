package com.example.project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.project.Ranking.DistRankingFragment;
import com.example.project.Ranking.PedoRankingFragment;

public class Frag2 extends Fragment {

    Button distBtn;
    Button pedoBtn;
    DistRankingFragment distRankingFragment;
    PedoRankingFragment pedoRankingFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag2, container, false);

        distBtn = v.findViewById(R.id.ranking_dist_btn);
        pedoBtn = v.findViewById(R.id.ranking_pedo_btn);

        distBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distBtn.setBackground(getContext().getResources().getDrawable(R.drawable.btn_style3));
                pedoBtn.setBackground(getContext().getResources().getDrawable(R.drawable.btn_style));

                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.ranking_fragment_container,new DistRankingFragment()).commit();
            }
        });
        pedoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distBtn.setBackground(getContext().getResources().getDrawable(R.drawable.btn_style));
                pedoBtn.setBackground(getContext().getResources().getDrawable(R.drawable.btn_style3));

                FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.ranking_fragment_container,new PedoRankingFragment()).commit();
            }
        });

        return v;
    }
}