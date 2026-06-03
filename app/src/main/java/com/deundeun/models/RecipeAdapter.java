package com.deundeun.models;

public class RecipeAdapter {
    private Recipe baseRecipe;
    private int targetServings;

    public RecipeAdapter(Recipe baseRecipe) {
        this.baseRecipe = baseRecipe;
        this.targetServings = baseRecipe.getBaseServings();
    }

    public void adaptServings(int targetServings) {
        this.targetServings = targetServings;
        adaptIngredients();
        adaptTools();
    }

    public void adaptIngredients() {
    }

    public void adaptTools() {
    }

    public Recipe generateAdaptedRecipe() {
        Recipe adapted = new Recipe(
                baseRecipe.getId() + "_adapted", 
                baseRecipe.getTitle() + " (" + targetServings + "인분)", 
                targetServings, 
                baseRecipe.getViewCount()
        );
        return adapted;
    }

    public Recipe getBaseRecipe() { return baseRecipe; }
    public void setBaseRecipe(Recipe baseRecipe) { this.baseRecipe = baseRecipe; }

    public int getTargetServings() { return targetServings; }
    public void setTargetServings(int targetServings) { this.targetServings = targetServings; }
}
