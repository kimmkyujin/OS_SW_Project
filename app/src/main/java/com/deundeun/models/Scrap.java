package com.deundeun.models;

import com.google.gson.annotations.SerializedName;

public class Scrap {
    @SerializedName("user_id")
    private String userId;
    
    @SerializedName("recipe_id")
    private String recipeId;

    public Scrap() {}

    public Scrap(String userId, String recipeId) {
        this.userId = userId;
        this.recipeId = recipeId;
    }

    public void add() {
    }

    public void remove() {
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getRecipeId() { return recipeId; }
    public void setRecipeId(String recipeId) { this.recipeId = recipeId; }
}
