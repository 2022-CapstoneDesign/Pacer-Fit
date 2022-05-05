package com.example.project.Ranking;

import java.text.DecimalFormat;

public class DistRankOneModel {
    int rank1Profile;
    String rank1ID;
    String rank1Km;

    public DistRankOneModel(int rank1Profile, String rank1Id, double rank1Km) {
        DecimalFormat myFormatter = new DecimalFormat("###,##0.0");
        this.rank1Profile = rank1Profile;
        this.rank1ID = rank1Id;
        this.rank1Km = myFormatter.format(rank1Km);
    }

    public int getRank1Profile() { return rank1Profile; }

    public String getRank1Id() {
        return rank1ID;
    }

    public String getRank1Km() {
        return rank1Km;
    }
}
