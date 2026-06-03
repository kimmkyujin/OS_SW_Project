package com.deundeun.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.deundeun.R;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Button btnCheckInquiry = findViewById(R.id.btn_check_inquiry);
        btnCheckInquiry.setOnClickListener(v -> {
            startActivity(new Intent(AdminActivity.this, AdminInquiryListActivity.class));
        });

        setupBackPressedHandling();
    }

    private void setupBackPressedHandling() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitDialog();
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
}
