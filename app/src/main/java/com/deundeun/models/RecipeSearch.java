package com.deundeun.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeSearch {
    private String searchQuery;
    private List<String> activeFilters;

    public RecipeSearch() {
        this.activeFilters = new ArrayList<>();
    }

    public List<Recipe> searchByKeyword(String keyword, List<Recipe> allRecipes) {
        this.searchQuery = keyword;
        if (keyword == null || keyword.trim().isEmpty()) {
            return allRecipes;
        }
        String lowerKeyword = keyword.toLowerCase();
        return allRecipes.stream()
                .filter(recipe -> 
                    (recipe.getTitle() != null && recipe.getTitle().toLowerCase().contains(lowerKeyword)) ||
                    (recipe.getIngredients() != null && recipe.getIngredients().toLowerCase().contains(lowerKeyword)) ||
                    (recipe.getCookingTools() != null && recipe.getCookingTools().toLowerCase().contains(lowerKeyword))
                )
                .collect(Collectors.toList());
    }

    public List<Recipe> filterByOptions(List<String> tools, List<String> ingredients, List<Recipe> recipesToFilter) {
        return recipesToFilter.stream()
                .filter(recipe -> {
                    boolean toolMatch = tools.isEmpty() || tools.stream().anyMatch(tool -> recipe.getCookingTools().contains(tool));
                    boolean ingredientMatch = ingredients.isEmpty() || ingredients.stream().anyMatch(ing -> recipe.getIngredients().contains(ing));
                    return toolMatch && ingredientMatch;
                })
                .collect(Collectors.toList());
    }

    public String getSearchQuery() { return searchQuery; }
    public void setSearchQuery(String searchQuery) { this.searchQuery = searchQuery; }

    public List<String> getActiveFilters() { return activeFilters; }
    public void setActiveFilters(List<String> activeFilters) { this.activeFilters = activeFilters; }
}
