package com.example.project.Ranking;

import java.text.DecimalFormat;

public class DistRankingModel {
    String rankIndex;
    int rankProfile;
    String rankID;
    String rankKm;

    public DistRankingModel(String rankIndex, int rankProfile, String rankId, double rankKm) {
        DecimalFormat myFormatter = new DecimalFormat("###,##0.0");
        this.rankIndex = rankIndex;
        this.rankProfile = rankProfile;
        this.rankID = rankId;
        this.rankKm = myFormatter.format(rankKm);
    }

    public String getRankIndex() {
        return rankIndex;
    }

    public int getRankProfile() {
        return rankProfile;
    }

    public String getRankId() {
        return rankID;
    }

    public String getRankKm() {
        return rankKm;
    }

}
