package com.example.project.Ranking;

import java.text.DecimalFormat;

public class RankingModel {
    String rankIndex;
    int rankProfile;
    String rankID;
    String rankStep;

    public RankingModel(String rankIndex, int rankProfile, String rankId, int rankStep) {
        DecimalFormat myFormatter = new DecimalFormat("###,###");
        this.rankIndex = rankIndex;
        this.rankProfile = rankProfile;
        this.rankID = rankId;
        this.rankStep = myFormatter.format(rankStep);
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

    public String getRankStep() {
        return rankStep;
    }

}
