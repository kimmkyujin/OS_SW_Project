package com.deundeun.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.deundeun.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserInquiryDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_inquiry_detail);

        String category = getIntent().getStringExtra("CATEGORY");
        String title = getIntent().getStringExtra("TITLE");
        String content = getIntent().getStringExtra("CONTENT");
        String answer = getIntent().getStringExtra("ANSWER");
        String status = getIntent().getStringExtra("STATUS");

        TextView tvCategory = findViewById(R.id.tv_user_detail_category);
        TextView tvTitle = findViewById(R.id.tv_user_detail_title);
        TextView tvContent = findViewById(R.id.tv_user_detail_content);
        TextView tvAnswer = findViewById(R.id.tv_admin_answer);
        Button btnBack = findViewById(R.id.btn_back_to_list);

        tvCategory.setText("카테고리: " + category);
        tvTitle.setText(title);
        tvContent.setText(content);

        if ("Completed".equals(status) && answer != null && !answer.isEmpty()) {
            tvAnswer.setText(answer);
            tvAnswer.setTextColor(getResources().getColor(android.R.color.black));
        } else {
            tvAnswer.setText("관리자의 답변을 기다리고 있습니다.");
            tvAnswer.setTextColor(getResources().getColor(R.color.orange_primary));
        }

        btnBack.setOnClickListener(v -> finish());

        initBottomNavigation();
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
