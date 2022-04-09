package com.example.project.Ranking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;

import java.util.ArrayList;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.ViewHolder> {

    ArrayList<RankingModel> rankList;

    @NonNull
    @Override
    public RankingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_item_recylerview, parent, false);
        return new RankingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingAdapter.ViewHolder holder, int position) {
        holder.onBind(rankList.get(position));
    }
    public void setRankList(ArrayList<RankingModel> list){
        this.rankList = list;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return rankList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView rankIndex;
        ImageView rankProfile;
        TextView rankID;
        TextView rankStep;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rankIndex = (TextView) itemView.findViewById(R.id.rank_index);
            rankProfile = (ImageView) itemView.findViewById(R.id.rank_profile);
            rankID = (TextView) itemView.findViewById(R.id.rank_id);
            rankStep = (TextView) itemView.findViewById(R.id.rank_step);
        }

        void onBind(RankingModel rankingModel) {
            rankIndex.setText(rankingModel.getRankIndex());
            rankProfile.setImageResource(rankingModel.getRankProfile());
            rankID.setText(rankingModel.getRankId());
            rankStep.setText(rankingModel.getRankStep());
        }
    }
}
