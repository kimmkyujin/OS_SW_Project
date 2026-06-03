package com.deundeun.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.deundeun.R;
import com.deundeun.models.Inquiry;
import com.deundeun.network.SupabaseApi;
import com.deundeun.network.SupabaseClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminInquiryListActivity extends AppCompatActivity {

    private RecyclerView rvInquiries;
    private AdminInquiryListAdapter adapter;
    private List<Inquiry> inquiryList = new ArrayList<>();
    private SupabaseApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry_list);

        View bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) bottomNav.setVisibility(View.GONE);

        TextView tvHeaderTitle = findViewById(R.id.tv_header_title);
        if (tvHeaderTitle != null) tvHeaderTitle.setText("문의사항 관리");

        View userHeader = findViewById(R.id.tv_header_user);
        if (userHeader != null) userHeader.setVisibility(View.VISIBLE);

        api = SupabaseClient.getClient().create(SupabaseApi.class);
        rvInquiries = findViewById(R.id.rv_inquiries);
        rvInquiries.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new AdminInquiryListAdapter(inquiryList, inquiry -> {
            Intent intent = new Intent(AdminInquiryListActivity.this, AdminInquiryDetailActivity.class);
            intent.putExtra("INQUIRY_ID", inquiry.getId());
            intent.putExtra("INQUIRY_TITLE", inquiry.getTitle());
            intent.putExtra("INQUIRY_CONTENT", inquiry.getContent());
            intent.putExtra("INQUIRY_USER", inquiry.getUserId());
            intent.putExtra("INQUIRY_CATEGORY", inquiry.getCategory());
            startActivity(intent);
        });
        rvInquiries.setAdapter(adapter);

        fetchInquiries();
        setupBackPressedHandling();
    }

    private void setupBackPressedHandling() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(AdminInquiryListActivity.this, AdminActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchInquiries();
    }

    private void fetchInquiries() {
        api.getAllInquiries().enqueue(new Callback<List<Inquiry>>() {
            @Override
            public void onResponse(Call<List<Inquiry>> call, Response<List<Inquiry>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    inquiryList.clear();
                    inquiryList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<Inquiry>> call, Throwable t) {
                Toast.makeText(AdminInquiryListActivity.this, "목록 로드 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
