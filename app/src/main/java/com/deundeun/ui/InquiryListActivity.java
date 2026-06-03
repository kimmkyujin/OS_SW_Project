package com.deundeun.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.deundeun.R;
import com.deundeun.models.Inquiry;
import com.deundeun.network.SupabaseApi;
import com.deundeun.network.SupabaseClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquiryListActivity extends AppCompatActivity {

    private RecyclerView rvInquiries;
    private InquiryListAdapter adapter;
    private List<Inquiry> inquiryList = new ArrayList<>();
    private SupabaseApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry_list);

        api = SupabaseClient.getClient().create(SupabaseApi.class);
        rvInquiries = findViewById(R.id.rv_inquiries);
        rvInquiries.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new InquiryListAdapter(inquiryList, inquiry -> {
            Intent intent = new Intent(InquiryListActivity.this, UserInquiryDetailActivity.class);
            intent.putExtra("CATEGORY", inquiry.getCategory());
            intent.putExtra("TITLE", inquiry.getTitle());
            intent.putExtra("CONTENT", inquiry.getContent());
            intent.putExtra("ANSWER", inquiry.getAnswer());
            intent.putExtra("STATUS", inquiry.getStatus());
            startActivity(intent);
        });
        rvInquiries.setAdapter(adapter);

        initBottomNavigation();
        fetchUserInquiries();
        setupBackPressedHandling();
    }

    private void setupBackPressedHandling() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(InquiryListActivity.this, InquiryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void fetchUserInquiries() {
        String userId = "test_user_001";
        if (com.deundeun.models.User.currentUser != null) {
            userId = com.deundeun.models.User.currentUser.getEmail();
        }

        api.getInquiriesByUserId("eq." + userId).enqueue(new Callback<List<Inquiry>>() {
            @Override
            public void onResponse(Call<List<Inquiry>> call, Response<List<Inquiry>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    inquiryList.clear();
                    inquiryList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Inquiry>> call, Throwable t) {}
        });
    }

    private void initBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_inquiry);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                return true;
            } else if (id == R.id.nav_storage) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("GO_TO_STORAGE", true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                return true;
            } else if (id == R.id.nav_recommend) {
                startActivity(new Intent(this, RecommendActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_inquiry) {
                startActivity(new Intent(this, InquiryActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }
}
