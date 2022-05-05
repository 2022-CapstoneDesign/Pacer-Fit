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

public class DistRankingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<DistRankingModel> rankList;
    private ArrayList<DistRankOneModel> rank1;
    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        View view;
        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_dist_rankone_header, parent, false);
            holder = new HeaderViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_dist_item_recylerview, parent, false);
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

    public void setRankList(ArrayList<DistRankingModel> list) {
        this.rankList = list;
        notifyDataSetChanged();
    }

    public void setRank1List(ArrayList<DistRankOneModel> list) {
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
        TextView rank1Km;

        HeaderViewHolder(@NonNull View headerView) {
            super(headerView);
            rank1Profile = (ImageView) headerView.findViewById(R.id.rank1_profile);
            rank1ID = (TextView) headerView.findViewById(R.id.rank1_id);
            rank1Km = (TextView) headerView.findViewById(R.id.rank1_km);
        }

        void onBind(DistRankOneModel rank1Model) {
            rank1Profile.setImageResource(rank1Model.getRank1Profile());
            rank1ID.setText(rank1Model.getRank1Id());
            rank1Km.setText(rank1Model.getRank1Km());
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
        TextView rankKm;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            rankIndex = (TextView) itemView.findViewById(R.id.rank_index);
            rankProfile = (ImageView) itemView.findViewById(R.id.rank_profile);
            rankID = (TextView) itemView.findViewById(R.id.rank_id);
            rankKm = (TextView) itemView.findViewById(R.id.rank_km);
        }

        void onBind(DistRankingModel rankingModel) {
            rankIndex.setText(rankingModel.getRankIndex());
            rankProfile.setImageResource(rankingModel.getRankProfile());
            rankID.setText(rankingModel.getRankId());
            rankKm.setText(String.valueOf(rankingModel.getRankKm()));
        }
    }
}
