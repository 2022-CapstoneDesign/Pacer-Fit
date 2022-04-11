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

public class RankingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<RankingModel> rankList;
    ArrayList<RankOneModel> rank1;
    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        View view;
        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_rankone_header, parent, false);
            holder = new HeaderViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_item_recylerview, parent, false);
            holder = new ItemViewHolder(view);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.onBind(rank1.get(0));
        } else {
            // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.onBind(rankList.get(position - 1));
        }
    }

    public void setRankList(ArrayList<RankingModel> list) {
        this.rankList = list;
        notifyDataSetChanged();
    }
    public void setRank1List(ArrayList<RankOneModel> list){
        this.rank1 = list;
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        else
            return TYPE_ITEM;
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        ImageView rank1Profile;
        TextView rank1ID;
        TextView rank1Step;
        HeaderViewHolder(@NonNull View headerView) {
            super(headerView);

            rank1Profile = (ImageView) headerView.findViewById(R.id.rank1_profile);
            rank1ID = (TextView) headerView.findViewById(R.id.rank1_id);
            rank1Step = (TextView) headerView.findViewById(R.id.rank1_step);
        }

        void onBind(RankOneModel rank1Model){
            rank1Profile.setImageResource(rank1Model.getRank1Profile());
            rank1ID.setText(rank1Model.getRank1Id());
            rank1Step.setText(rank1Model.getRank1Step());

        }
    }

    @Override
    public int getItemCount() {
        // header가 추가되므로 + 1
        return rankList.size() + 1;
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView rankIndex;
        ImageView rankProfile;
        TextView rankID;
        TextView rankStep;

        public ItemViewHolder(@NonNull View itemView) {
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
