package com.example.project.ICF;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RecommendationResponse {

    private Long itemId;
    private double expectedPreference;

    public RecommendationResponse(Long primaryItemId, double expectedPreference) {
        this.itemId=primaryItemId;
        this.expectedPreference = expectedPreference;
    }
}
