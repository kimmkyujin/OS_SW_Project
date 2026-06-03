package com.deundeun.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import com.deundeun.R;
import com.deundeun.models.Inquiry;
import com.deundeun.network.SupabaseApi;
import com.deundeun.network.SupabaseClient;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminInquiryDetailActivity extends AppCompatActivity {

    private String inquiryId;
    private EditText etAnswer;
    private SupabaseApi api;
    private boolean isDataChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_inquiry_detail);

        api = SupabaseClient.getClient().create(SupabaseApi.class);
        
        inquiryId = getIntent().getStringExtra("INQUIRY_ID");
        String title = getIntent().getStringExtra("INQUIRY_TITLE");
        String content = getIntent().getStringExtra("INQUIRY_CONTENT");
        String category = getIntent().getStringExtra("INQUIRY_CATEGORY");

        TextView tvCategory = findViewById(R.id.tv_detail_category);
        TextView tvTitle = findViewById(R.id.tv_detail_title);
        TextView tvContent = findViewById(R.id.tv_detail_content);
        etAnswer = findViewById(R.id.et_admin_answer);
        Button btnSend = findViewById(R.id.btn_send_answer);
        Button btnCancel = findViewById(R.id.btn_cancel_answer);

        tvCategory.setText("카테고리: " + category);
        tvTitle.setText(title);
        tvContent.setText(content);

        etAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { isDataChanged = true; }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnSend.setOnClickListener(v -> submitAnswer());
        btnCancel.setOnClickListener(v -> {
            if (isDataChanged) {
                new AlertDialog.Builder(this)
                    .setMessage("작성 중인 내용이 사라집니다. 괜찮으십니까?")
                    .setPositiveButton("확인", (dialog, which) -> clearAnswerField())
                    .setNegativeButton("취소", null)
                    .show();
            } else {
                clearAnswerField();
            }
        });

        setupBackPressedHandling();
    }

    private void setupBackPressedHandling() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isDataChanged) {
                    new AlertDialog.Builder(AdminInquiryDetailActivity.this)
                        .setMessage("작성 중인 내용이 사라집니다. 목록으로 돌아가시겠습니까?")
                        .setPositiveButton("확인", (dialog, which) -> finish())
                        .setNegativeButton("취소", null)
                        .show();
                } else {
                    finish();
                }
            }
        });
    }

    private void clearAnswerField() {
        if (etAnswer != null) etAnswer.setText("");
        isDataChanged = false;
    }

    private void submitAnswer() {
        String answer = etAnswer.getText().toString().trim();
        if (answer.isEmpty()) {
            Toast.makeText(this, "답변을 입력해 주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        Inquiry inquiry = new Inquiry();
        inquiry.addAnswer(answer);

        Map<String, Object> updates = new HashMap<>();
        updates.put("answer", inquiry.getAnswer());
        updates.put("status", inquiry.getStatus());
        updates.put("answered_at", inquiry.getAnsweredAt());

        api.updateInquiry("eq." + inquiryId, updates).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AdminInquiryDetailActivity.this, "답변 전송 완료", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "unknown";
                        Log.e("AdminInquiry", "Failed: " + response.code() + " - " + errorBody);
                    } catch (Exception e) {}
                    Toast.makeText(AdminInquiryDetailActivity.this, "전송 실패 (코드: " + response.code() + ")", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AdminInquiryDetailActivity.this, "오류!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
