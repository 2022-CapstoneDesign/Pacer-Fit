package com.example.project.Main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project.R;
import com.example.project.Ranking.DistRankingFragment;
import com.example.project.Ranking.PedoFragment;

public class RankingFragment extends Fragment {
    Button distBtn;
    Button pedoBtn;
    DistRankingFragment distRankingFragment;
    PedoFragment pedoRankingFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_ranking_fragment, container, false);

        distBtn = v.findViewById(R.id.ranking_dist_btn);
        pedoBtn = v.findViewById(R.id.ranking_pedo_btn);

        distBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distBtn.setBackground(getContext().getResources().getDrawable(R.drawable.btn_style3));
                pedoBtn.setBackground(getContext().getResources().getDrawable(R.drawable.btn_style));

                // fragment 위에 그린 fragment를 교체하기 위해서 childFragment를 사용
                getChildFragmentManager().beginTransaction().replace(R.id.ranking_fragment_container, new DistRankingFragment()).commit();
                //FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                //fm.replace(R.id.ranking_fragment_container,new DistRankingFragment()).commit();
            }
        });
        pedoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distBtn.setBackground(getContext().getResources().getDrawable(R.drawable.btn_style));
                pedoBtn.setBackground(getContext().getResources().getDrawable(R.drawable.btn_style3));

                // fragment 위에 그린 fragment를 교체하기 위해서 childFragment를 사용
                getChildFragmentManager().beginTransaction().replace(R.id.ranking_fragment_container, new PedoFragment()).commit();
                //FragmentTransaction fm = getActivity().getSupportFragmentManager().beginTransaction();
                //fm.replace(R.id.ranking_fragment_container,new PedoRankingFragment()).commit();
            }
        });
        return v;
    }
}