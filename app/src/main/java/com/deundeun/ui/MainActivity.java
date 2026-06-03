package com.deundeun.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.deundeun.R;
import com.deundeun.models.Recipe;
import com.deundeun.models.RecipeSearch;
import com.deundeun.network.SupabaseApi;
import com.deundeun.network.SupabaseClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    
    private RecyclerView rvRecipes;
    private RecipeListAdapter adapter;
    private List<Recipe> allRecipes = new ArrayList<>();
    private List<Recipe> filteredRecipes = new ArrayList<>();
    private RecipeSearch recipeSearch = new RecipeSearch();
    private SupabaseApi api;
    private boolean isStorageMode = false;
    private TextView tvListTitle, tvEmptyMessage;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (com.deundeun.models.User.currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);
        
        api = SupabaseClient.getClient().create(SupabaseApi.class);
        
        initViews();
        handleIntent(getIntent());
        setupBackPressedHandling();
    }

    private void setupBackPressedHandling() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isStorageMode) {
                    com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
                    if (bottomNav != null) {
                        bottomNav.setSelectedItemId(R.id.nav_home);
                    }
                    switchToHome();
                } else {
                    showExitDialog();
                }
            }
        });
    }

    private void showExitDialog() {
        new AlertDialog.Builder(this)
            .setMessage("종료하시겠습니까?")
            .setPositiveButton("예", (dialog, which) -> {
                com.deundeun.models.User.currentUser = null;
                finishAffinity();
                System.exit(0);
            })
            .setNegativeButton("아니오", null)
            .show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent == null) return;
        
        com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            if (intent.getBooleanExtra("GO_TO_STORAGE", false)) {
                bottomNav.setSelectedItemId(R.id.nav_storage);
            } else {
                bottomNav.setSelectedItemId(R.id.nav_home);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isStorageMode) {
            fetchScrappedRecipes();
        } else {
            fetchAllRecipes();
        }
    }

    private String getUserId() {
        if (com.deundeun.models.User.currentUser != null) {
            return com.deundeun.models.User.currentUser.getEmail();
        }
        return "test_user_001";
    }

    private void initViews() {
        etSearch = findViewById(R.id.et_search);
        rvRecipes = findViewById(R.id.rv_recipes);
        tvListTitle = findViewById(R.id.tv_list_title);
        tvEmptyMessage = findViewById(R.id.tv_empty_message);
        com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        updateLayoutManager();
        
        adapter = new RecipeListAdapter(filteredRecipes, new RecipeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Recipe recipe) {
                Intent intent = new Intent(MainActivity.this, RecipeDetailActivity.class);
                intent.putExtra("RECIPE_ID", recipe.getId());
                startActivity(intent);
            }

            @Override
            public void onHeartClick(Recipe recipe) {
                if (isStorageMode) {
                    removeScrapFromList(recipe);
                }
            }
        });
        rvRecipes.setAdapter(adapter);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            android.util.Log.d("MainNav", "Menu clicked: " + id);
            if (id == R.id.nav_home) {
                switchToHome();
                return true;
            } else if (id == R.id.nav_storage) {
                switchToStorage();
                return true;
            } else if (id == R.id.nav_recommend) {
                startActivity(new Intent(MainActivity.this, RecommendActivity.class));
                return true;
            } else if (id == R.id.nav_inquiry) {
                startActivity(new Intent(MainActivity.this, InquiryActivity.class));
                return true;
            }
            return false;
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void updateLayoutManager() {
        if (isStorageMode) {
            rvRecipes.setLayoutManager(new androidx.recyclerview.widget.GridLayoutManager(this, 2));
        } else {
            rvRecipes.setLayoutManager(new LinearLayoutManager(this));
        }
        
        if (adapter != null) {
            adapter.setGridView(isStorageMode);
            rvRecipes.setAdapter(adapter);
        }
    }

    private void switchToHome() {
        isStorageMode = false;
        tvListTitle.setText("레시피 목록");
        etSearch.setHint("검색");
        updateLayoutManager();
        fetchAllRecipes();
    }

    private void switchToStorage() {
        isStorageMode = true;
        tvListTitle.setText("보관된 레시피");
        etSearch.setHint("보관소 검색");
        updateLayoutManager();
        fetchScrappedRecipes();
    }

    private void fetchAllRecipes() {
        api.getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allRecipes = response.body();
                    updateDisplayList(allRecipes);
                }
            }
            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {}
        });
    }

    private void fetchScrappedRecipes() {
        String userId = getUserId();
        api.getScrappedRecipes("eq." + userId).enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allRecipes = response.body();
                    updateDisplayList(allRecipes);
                } else {
                    updateDisplayList(new ArrayList<>());
                }
            }
            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                updateDisplayList(new ArrayList<>());
            }
        });
    }

    private void performSearch(String keyword) {
        List<Recipe> results = recipeSearch.searchByKeyword(keyword, allRecipes);
        updateDisplayList(results);
    }

    private void removeScrapFromList(Recipe recipe) {
        String userId = getUserId();
        
        api.removeScrap("eq." + userId, "eq." + recipe.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    allRecipes.remove(recipe);
                    updateDisplayList(allRecipes);
                    Toast.makeText(MainActivity.this, "스크랩이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDisplayList(List<Recipe> newList) {
        filteredRecipes.clear();
        filteredRecipes.addAll(newList);
        adapter.notifyDataSetChanged();
        
        if (filteredRecipes.isEmpty()) {
            tvEmptyMessage.setVisibility(View.VISIBLE);
            if (isStorageMode) tvEmptyMessage.setText("보관된 레시피가 없습니다.");
            else tvEmptyMessage.setText("검색 결과가 없습니다.");
        } else {
            tvEmptyMessage.setVisibility(View.GONE);
        }
    }
}
