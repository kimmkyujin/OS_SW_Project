package com.deundeun.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.deundeun.R;
import com.deundeun.models.Recipe;
import com.deundeun.network.SupabaseApi;
import com.deundeun.network.SupabaseClient;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeDetailActivity extends AppCompatActivity {

    private ImageView ivRecipeImage, btnScrap;
    private TextView tvTitleTop, tvSteps;
    private boolean isScrapped = false;
    private TextView btnToolSelector, btnServingsSelector;
    private LinearLayout layoutToolOptions, layoutServingsOptions;
    private LinearLayout containerIngredients;
    
    private Recipe baseRecipe;
    private SupabaseApi api;

    private String selectedTool;
    private String selectedMainIngredient;
    private int selectedServings = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        api = SupabaseClient.getClient().create(SupabaseApi.class);
        initViews();
        
        String recipeId = getIntent().getStringExtra("RECIPE_ID");
        if (recipeId == null) recipeId = "doenjang-jjigae-001"; 

        checkScrapStatus(recipeId);
        fetchRecipeDetails(recipeId);
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        ivRecipeImage = findViewById(R.id.iv_recipe_image);
        tvTitleTop = findViewById(R.id.tv_recipe_title_top);
        btnScrap = findViewById(R.id.btn_scrap);
        tvSteps = findViewById(R.id.tv_steps);
        btnToolSelector = findViewById(R.id.btn_tool_selector);
        layoutToolOptions = findViewById(R.id.layout_tool_options);
        btnServingsSelector = findViewById(R.id.btn_servings_selector);
        layoutServingsOptions = findViewById(R.id.layout_servings_options);
        containerIngredients = findViewById(R.id.container_ingredients);

        findViewById(R.id.btn_storage_care).setOnClickListener(v -> {
            String recipeId = getIntent().getStringExtra("RECIPE_ID");
            if (recipeId == null) recipeId = "doenjang-jjigae-001";
            Intent intent = new Intent(RecipeDetailActivity.this, StorageCareActivity.class);
            intent.putExtra("RECIPE_ID", recipeId);
            startActivity(intent);
        });

        btnToolSelector.setOnClickListener(v -> layoutToolOptions.setVisibility(layoutToolOptions.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE));
        
        btnServingsSelector.setOnClickListener(v -> layoutServingsOptions.setVisibility(layoutServingsOptions.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE));
        findViewById(R.id.option_1serving).setOnClickListener(v -> selectServings(1, "1인분 / 15분"));
        findViewById(R.id.option_2servings).setOnClickListener(v -> selectServings(2, "2인분 / 20분"));

        btnScrap.setOnClickListener(v -> {
            toggleScrap();
        });
        
        btnScrap.bringToFront();
    }

    private String getUserId() {
        if (com.deundeun.models.User.currentUser != null) {
            return com.deundeun.models.User.currentUser.getEmail();
        }
        return "test_user_001";
    }

    private void checkScrapStatus(String recipeId) {
        String userId = getUserId();
        
        btnScrap.setAlpha(0.3f);
        isScrapped = false;

        api.checkScrap("eq." + userId, "eq." + recipeId).enqueue(new Callback<List<com.deundeun.models.Scrap>>() {
            @Override
            public void onResponse(Call<List<com.deundeun.models.Scrap>> call, Response<List<com.deundeun.models.Scrap>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    isScrapped = true;
                    btnScrap.setAlpha(1.0f);
                }
            }
            @Override
            public void onFailure(Call<List<com.deundeun.models.Scrap>> call, Throwable t) {
                Log.e("ScrapError", "Status check failed: " + t.getMessage());
            }
        });
    }

    private void toggleScrap() {
        if (baseRecipe == null) return;
        String userId = getUserId();
        String recipeId = baseRecipe.getId();

        if (isScrapped) {
            api.removeScrap("eq." + userId, "eq." + recipeId).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        isScrapped = false;
                        btnScrap.setAlpha(0.3f);
                        Toast.makeText(RecipeDetailActivity.this, "스크랩 취소", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("ScrapError", "Remove failed: " + t.getMessage());
                }
            });
        } else {
            com.deundeun.models.Scrap scrap = new com.deundeun.models.Scrap(userId, recipeId);
            api.addScrap(scrap).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        isScrapped = true;
                        btnScrap.setAlpha(1.0f);
                        Toast.makeText(RecipeDetailActivity.this, "보관소 저장", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("ScrapError", "Add failed with code: " + response.code());
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("ScrapError", "Add network failed: " + t.getMessage());
                }
            });
        }
    }

    private void setupToolOptions() {
        layoutToolOptions.removeAllViews();
        if (baseRecipe == null || baseRecipe.getCookingTools() == null) return;

        String[] tools = baseRecipe.getCookingTools().split(":");

        for (String tool : tools) {
            addToolOption(tool.trim());
        }
        
        if (tools.length > 0 && selectedTool == null) {
            selectedTool = tools[0].trim();
            btnToolSelector.setText(selectedTool);
        }
    }

    private void addToolOption(String tool) {
        TextView optionView = new TextView(this);
        optionView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        optionView.setPadding(30, 30, 30, 30);
        optionView.setText(tool);
        optionView.setGravity(android.view.Gravity.CENTER);
        optionView.setBackgroundResource(android.R.drawable.list_selector_background);
        optionView.setOnClickListener(v -> selectTool(tool));
        layoutToolOptions.addView(optionView);
    }

    private void selectTool(String tool) {
        selectedTool = tool;
        btnToolSelector.setText(tool);
        layoutToolOptions.setVisibility(View.GONE);
        updateUI();
    }

    private void selectServings(int servings, String text) {
        selectedServings = servings;
        btnServingsSelector.setText(text);
        layoutServingsOptions.setVisibility(View.GONE);
        updateUI();
    }

    private void fetchRecipeDetails(String id) {
        api.getRecipeById("eq." + id).enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    baseRecipe = response.body().get(0);
                    
                    setupToolOptions(); // 레시피에 맞는 도구 옵션 설정
                    updateUI();
                }
            }
            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {}
        });
    }

    private void updateUI() {
        if (baseRecipe == null) return;

        Glide.with(RecipeDetailActivity.this)
             .load(baseRecipe.getImageUrl())
             .centerCrop()
             .placeholder(R.drawable.ic_launcher_background)
             .error(R.drawable.ic_launcher_background)
             .into(ivRecipeImage);

        String calculatedIngredients = scaleIngredients(baseRecipe.getIngredients(), selectedServings);
        setupIngredientsUI(calculatedIngredients);

        String newSteps = baseRecipe.getSteps();
        if (newSteps == null) newSteps = "";

        if (baseRecipe.getReplaceable() != null) {
            String replaceData = baseRecipe.getReplaceable();
            newSteps = applyMapping(newSteps, replaceData, selectedTool);
            newSteps = applyMapping(newSteps, replaceData, selectedMainIngredient);
        }

        if (tvTitleTop != null) tvTitleTop.setText(baseRecipe.getTitle());
        tvSteps.setText(newSteps);

        String combinedTitle = baseRecipe.getTitle() + " (" + selectedServings + "인분, " + selectedTool + ", " + (selectedMainIngredient != null ? selectedMainIngredient : "") + ")";
        checkAndSyncToDB(combinedTitle, selectedTool, calculatedIngredients, newSteps);
    }

    private String applyMapping(String steps, String replaceData, String selectedOption) {
        if (selectedOption == null || replaceData == null) return steps;

        String patternString = Pattern.quote(selectedOption) + "\\{(.*?)\\}";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(replaceData);

        if (matcher.find()) {
            String mappings = matcher.group(1);
            String[] pairs = mappings.split(",");
            for (String pair : pairs) {
                String[] parts = pair.split(":");
                if (parts.length == 2) {
                    String tag = parts[0].trim();
                    String replacement = parts[1].trim();
                    steps = steps.replace(tag, replacement);
                }
            }
        }
        return steps;
    }

    private String scaleIngredients(String ingredients, int factor) {
        if (ingredients == null) return "";
        
        StringBuilder result = new StringBuilder();
        Pattern numPattern = Pattern.compile("(\\d+(\\.\\d+)?)");
        Matcher numMatcher = numPattern.matcher(ingredients);
        
        int lastEnd = 0;
        while (numMatcher.find()) {
            result.append(ingredients, lastEnd, numMatcher.start());
            double originalValue = Double.parseDouble(numMatcher.group(1));
            double newValue = originalValue * factor;
            
            if (newValue == (long) newValue) result.append((long) newValue);
            else result.append(String.format("%.2f", newValue).replace(".00", ""));
            
            lastEnd = numMatcher.end();
        }
        result.append(ingredients.substring(lastEnd));
        return result.toString();
    }

    private void setupIngredientsUI(String ingredientsRaw) {
        containerIngredients.removeAllViews();
        String[] parts = ingredientsRaw.split(",");
        LayoutInflater inflater = LayoutInflater.from(this);

        for (String part : parts) {
            String trimmed = part.trim();
            View itemView = inflater.inflate(R.layout.item_ingredient_replaceable, containerIngredients, false);
            TextView tvName = itemView.findViewById(R.id.tv_ingredient_name);
            TextView btnReplace = itemView.findViewById(R.id.btn_replace);
            LinearLayout layoutDropdown = itemView.findViewById(R.id.layout_dropdown);

            if (trimmed.contains(":")) {
                String[] options = trimmed.split(":");
                String currentDisplay = selectedMainIngredient != null ? 
                        findMatchingOption(options, selectedMainIngredient) : options[0];

                tvName.setVisibility(View.GONE);
                btnReplace.setVisibility(View.VISIBLE);
                btnReplace.setText(currentDisplay);
                btnReplace.setOnClickListener(v -> layoutDropdown.setVisibility(layoutDropdown.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE));

                for (String option : options) {
                    String optionTrimmed = option.trim();
                    String nameOnly = optionTrimmed.split(" ")[0];
                    addIngredientOption(layoutDropdown, optionTrimmed, btnReplace, nameOnly);
                    
                    if (selectedMainIngredient == null) selectedMainIngredient = nameOnly;
                }

                containerIngredients.addView(itemView);
            } else {
                btnReplace.setVisibility(View.GONE);
                tvName.setVisibility(View.VISIBLE);
                tvName.setText(trimmed);
                containerIngredients.addView(itemView);
            }
        }
    }

    private String findMatchingOption(String[] options, String name) {
        for (String opt : options) {
            if (opt.contains(name)) return opt.trim();
        }
        return options[0].trim();
    }

    private void addIngredientOption(LinearLayout container, String optionText, TextView button, String type) {
        TextView optionView = new TextView(this);
        optionView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        optionView.setPadding(30, 30, 30, 30);
        optionView.setText(optionText);
        optionView.setGravity(android.view.Gravity.CENTER);
        optionView.setBackgroundResource(android.R.drawable.list_selector_background);
        
        optionView.setOnClickListener(v -> {
            button.setText(optionText);
            container.setVisibility(View.GONE);
            selectedMainIngredient = type;
            updateUI();
        });
        
        container.addView(optionView);
        View divider = new View(this);
        divider.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
        divider.setBackgroundColor(0xFFEEEEEE);
        container.addView(divider);
    }

    private void checkAndSyncToDB(String title, String tool, String ingredients, String steps) {
        api.findRecipeByOptions(title, tool, ingredients).enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful() && (response.body() == null || response.body().isEmpty())) {
                    performUpsert(title, tool, ingredients, steps);
                }
            }
            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {}
        });
    }

    private void performUpsert(String title, String tool, String ingredients, String steps) {
        Recipe newRecipe = new Recipe();
        newRecipe.setId(UUID.randomUUID().toString());
        newRecipe.setTitle(title);
        newRecipe.setCookingTools(tool);
        newRecipe.setIngredients(ingredients);
        newRecipe.setSteps(steps);
        newRecipe.setImageUrl(baseRecipe.getImageUrl());
        newRecipe.setBaseServings(selectedServings);
        newRecipe.setViewCount(0);
        api.upsertRecipe(newRecipe).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {}
            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
