package com.deundeun.ui;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.deundeun.R;
import com.deundeun.models.StorageCare;
import com.deundeun.network.SupabaseApi;
import com.deundeun.network.SupabaseClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StorageCareActivity extends AppCompatActivity {

    private RecyclerView rvStorageCare;
    private StorageCareAdapter adapter;
    private List<StorageCare> careList = new ArrayList<>();
    private SupabaseApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_care);

        String recipeId = getIntent().getStringExtra("RECIPE_ID");
        if (recipeId == null) {
            finish();
            return;
        }

        api = SupabaseClient.getClient().create(SupabaseApi.class);
        rvStorageCare = findViewById(R.id.rv_storage_care);
        rvStorageCare.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StorageCareAdapter(careList);
        rvStorageCare.setAdapter(adapter);

        android.util.Log.d("StorageCare", "Fetching care data for recipeId: " + recipeId);
        fetchStorageCareData(recipeId);
    }

    private void fetchStorageCareData(String recipeId) {
        api.getStorageCareByRecipeId("eq." + recipeId).enqueue(new Callback<List<StorageCare>>() {
            @Override
            public void onResponse(Call<List<StorageCare>> call, Response<List<StorageCare>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isEmpty()) {
                        Toast.makeText(StorageCareActivity.this, "준비 중", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        careList.clear();
                        careList.addAll(response.body());
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<StorageCare>> call, Throwable t) {
                Toast.makeText(StorageCareActivity.this, "데이터를 불러오는데 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
