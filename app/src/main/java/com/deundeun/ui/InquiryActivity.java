package com.deundeun.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import com.deundeun.R;
import com.deundeun.models.Inquiry;
import com.deundeun.network.SupabaseApi;
import com.deundeun.network.SupabaseClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.UUID;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquiryActivity extends AppCompatActivity {

    private Spinner spinnerCategory;
    private EditText etTitle, etContent;
    private boolean isDataChanged = false;
    private SupabaseApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);

        api = SupabaseClient.getClient().create(SupabaseApi.class);
        initViews();
        setupBackPressedHandling();
    }

    private void handleExit(Runnable onConfirm) {
        if (isDataChanged) {
            new AlertDialog.Builder(this)
                .setMessage("작성 중인 내용이 사라집니다. 괜찮으십니까?")
                .setPositiveButton("확인", (dialog, which) -> {
                    clearInquiryFields();
                    onConfirm.run();
                })
                .setNegativeButton("취소", null)
                .show();
        } else {
            onConfirm.run();
        }
    }

    private void initViews() {
        spinnerCategory = findViewById(R.id.spinner_category);
        etTitle = findViewById(R.id.et_inquiry_title);
        etContent = findViewById(R.id.et_inquiry_content);
        Button btnSend = findViewById(R.id.btn_send_inquiry);
        Button btnCancel = findViewById(R.id.btn_cancel_inquiry);
        TextView btnGoToList = findViewById(R.id.btn_go_to_list);

        String[] categories = {"문의 카테고리", "레시피 추천", "버그 제보", "기타 문의"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(adapter);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { isDataChanged = true; }
            @Override
            public void afterTextChanged(Editable s) {}
        };
        etTitle.addTextChangedListener(watcher);
        etContent.addTextChangedListener(watcher);

        btnSend.setOnClickListener(v -> validateAndSend());
        btnCancel.setOnClickListener(v -> {
            if (isDataChanged) {
                new AlertDialog.Builder(this)
                    .setMessage("작성 중인 내용이 사라집니다. 괜찮으십니까?")
                    .setPositiveButton("확인", (dialog, which) -> clearInquiryFields())
                    .setNegativeButton("취소", null)
                    .show();
            } else {
                clearInquiryFields();
            }
        });
        btnGoToList.setOnClickListener(v -> handleExit(() -> {
            startActivity(new Intent(InquiryActivity.this, InquiryListActivity.class));
            finish();
        }));

        initBottomNavigation();
    }

    private void clearInquiryFields() {
        if (etTitle != null) etTitle.setText("");
        if (etContent != null) etContent.setText("");
        if (spinnerCategory != null) spinnerCategory.setSelection(0);
        isDataChanged = false;
    }

    private void validateAndSend() {
        String category = spinnerCategory.getSelectedItem().toString();
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        if (spinnerCategory.getSelectedItemPosition() == 0 || title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "모든 항목을 입력해 주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        Inquiry inquiry = new Inquiry();
        inquiry.setId(UUID.randomUUID().toString());
        inquiry.setUserId(getUserId());
        inquiry.setCategory(category);
        inquiry.setTitle(title);
        inquiry.setContent(content);

        api.submitInquiry(inquiry).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(InquiryActivity.this, "전송 완료", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "unknown error";
                        Log.e("InquiryError", "Code: " + response.code() + ", Message: " + errorBody);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(InquiryActivity.this, "전송 실패 (코드: " + response.code() + ")", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(InquiryActivity.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getUserId() {
        if (com.deundeun.models.User.currentUser != null) {
            return com.deundeun.models.User.currentUser.getEmail();
        }
        return "test_user_001";
    }

    private void setupBackPressedHandling() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleExit(InquiryActivity.this::navigateToHome);
            }
        });
    }

    private void navigateToHome() {
        Intent intent = new Intent(InquiryActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void initBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_inquiry);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_inquiry) return true;

            handleExit(() -> {
                if (id == R.id.nav_home) {
                    navigateToHome();
                } else if (id == R.id.nav_storage) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("GO_TO_STORAGE", true);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.nav_recommend) {
                    startActivity(new Intent(this, RecommendActivity.class));
                    finish();
                }
            });
            return false;
        });
    }
}
