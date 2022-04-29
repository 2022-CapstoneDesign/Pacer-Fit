package com.example.project.Ranking;

import java.text.DecimalFormat;

public class RankOneModel {
    int rank1Profile;
    String rank1ID;
    String rank1Step;

    public RankOneModel(int rank1Profile, String rank1Id, int rank1Step) {
        DecimalFormat myFormatter = new DecimalFormat("###,###");
        this.rank1Profile = rank1Profile;
        this.rank1ID = rank1Id;
        this.rank1Step = myFormatter.format(rank1Step);
    }

    public int getRank1Profile() { return rank1Profile; }

    public String getRank1Id() {
        return rank1ID;
    }

    public String getRank1Step() {
        return rank1Step;
    }
}
