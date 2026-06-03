package com.deundeun.models;

import com.google.gson.annotations.SerializedName;

public class Recipe {
    private String id;
    private String title;
    
    @SerializedName("baseServings")
    private int baseServings;
    
    @SerializedName("viewCount")
    private int viewCount;
    
    @SerializedName("CookingTools")
    private String cookingTools;
    
    @SerializedName("ingredients")
    private String ingredients;
    
    @SerializedName("Steps")
    private String steps;
    
    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("replaceable")
    private String replaceable;

    public Recipe() {}

    public Recipe(String id, String title, int baseServings, int viewCount) {
        this.id = id;
        this.title = title;
        this.baseServings = baseServings;
        this.viewCount = viewCount;
    }

    public Recipe(String id, String title, String cookingTools, String ingredients, String steps, String imageUrl) {
        this.id = id;
        this.title = title;
        this.cookingTools = cookingTools;
        this.ingredients = ingredients;
        this.steps = steps;
        this.imageUrl = imageUrl;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getBaseServings() { return baseServings; }
    public void setBaseServings(int baseServings) { this.baseServings = baseServings; }

    public int getViewCount() { return viewCount; }
    public void setViewCount(int viewCount) { this.viewCount = viewCount; }

    public String getCookingTools() { return cookingTools; }
    public void setCookingTools(String cookingTools) { this.cookingTools = cookingTools; }

    public String getIngredients() { return ingredients; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }

    public String getSteps() { return steps; }
    public void setSteps(String steps) { this.steps = steps; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getReplaceable() { return replaceable; }
    public void setReplaceable(String replaceable) { this.replaceable = replaceable; }
}
