package com.deundeun.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.deundeun.R;
import com.deundeun.models.Recipe;
import com.deundeun.models.Recommendation;
import com.deundeun.network.SupabaseApi;
import com.deundeun.network.SupabaseClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendActivity extends AppCompatActivity {

    private RecyclerView rvTopRecipes;
    private RecipeListAdapter adapter;
    private List<Recipe> allRecipes = new ArrayList<>();
    private List<Recipe> topRecipes = new ArrayList<>();
    private Recommendation recommendation = new Recommendation();
    private SupabaseApi api;
    private TextView tvPeriodSelector, tvEmptyMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        api = SupabaseClient.getClient().create(SupabaseApi.class);
        initViews();
        fetchAllRecipes();
        setupBackPressedHandling();
    }

    private void setupBackPressedHandling() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(RecommendActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initViews() {
        rvTopRecipes = findViewById(R.id.rv_top_recipes);
        tvPeriodSelector = findViewById(R.id.tv_period_selector);
        tvEmptyMessage = findViewById(R.id.tv_empty_message);
        
        rvTopRecipes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeListAdapter(topRecipes, new RecipeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Recipe recipe) {
                Intent intent = new Intent(RecommendActivity.this, RecipeDetailActivity.class);
                intent.putExtra("RECIPE_ID", recipe.getId());
                startActivity(intent);
            }

            @Override
            public void onHeartClick(Recipe recipe) {
            }
        });
        rvTopRecipes.setAdapter(adapter);

        tvPeriodSelector.setOnClickListener(v -> showPeriodMenu());

        initBottomNavigation();
    }

    private void showPeriodMenu() {
        String[] periods = {"이번주", "이번달", "올해"};
        new AlertDialog.Builder(this)
                .setTitle("기간 선택")
                .setItems(periods, (dialog, which) -> {
                    String selected = periods[which];
                    tvPeriodSelector.setText("[" + selected + "]");
                    updateRecommendation(selected);
                })
                .show();
    }

    private void fetchAllRecipes() {
        api.getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allRecipes = response.body();
                    updateRecommendation("이번주");
                }
            }
            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {}
        });
    }

    private void updateRecommendation(String period) {
        List<Recipe> results = recommendation.getTopRecipes(period, 5, allRecipes);
        topRecipes.clear();
        topRecipes.addAll(results);
        adapter.notifyDataSetChanged();

        if (topRecipes.isEmpty()) {
            tvEmptyMessage.setVisibility(View.VISIBLE);
        } else {
            tvEmptyMessage.setVisibility(View.GONE);
        }
    }

    private void initBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_recommend);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_storage) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("GO_TO_STORAGE", true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            } else if (id == R.id.nav_inquiry) {
                Intent intent = new Intent(this, InquiryActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}
