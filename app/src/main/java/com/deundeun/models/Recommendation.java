package com.deundeun.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Recommendation {
    private String period;

    public Recommendation() {
        this.period = "주간";
    }

    public List<Recipe> getTopRecipes(String period, int limit, List<Recipe> allRecipes) {
        this.period = period;
        if (allRecipes == null || allRecipes.isEmpty()) {
            return new ArrayList<>();
        }

        return allRecipes.stream()
                .sorted((r1, r2) -> Integer.compare(calculateScore(r2), calculateScore(r1)))
                .limit(limit)
                .collect(Collectors.toList());
    }

    private int calculateScore(Recipe recipe) {
        return recipe.getViewCount();
    }

    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
}
