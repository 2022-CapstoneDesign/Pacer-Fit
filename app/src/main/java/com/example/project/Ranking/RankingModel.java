package com.example.project.Ranking;

public class RankingModel {
    String rankIndex;
    int rankProfile;
    String rankID;
    String rankStep;

    public RankingModel(String rankIndex, int rankProfile, String rankId, String rankStep) {
        this.rankIndex = rankIndex;
        this.rankProfile = rankProfile;
        this.rankID = rankId;
        this.rankStep = rankStep;
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