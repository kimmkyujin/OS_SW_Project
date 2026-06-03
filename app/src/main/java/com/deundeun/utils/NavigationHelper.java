package com.deundeun.utils;

import android.app.Activity;
import android.content.Intent;
import com.deundeun.R;
import com.deundeun.ui.MainActivity;
import com.deundeun.ui.RecommendActivity;
import com.deundeun.ui.InquiryActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationHelper {
    public static void setupBottomNavigation(Activity activity, int selectedItemId) {
        BottomNavigationView bottomNav = activity.findViewById(R.id.bottom_navigation);
        if (bottomNav == null) return;

        bottomNav.setSelectedItemId(selectedItemId);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == selectedItemId) return true;

            Intent intent = null;
            if (id == R.id.nav_home) {
                intent = new Intent(activity, MainActivity.class);
            } else if (id == R.id.nav_storage) {
                intent = new Intent(activity, MainActivity.class);
                intent.putExtra("GO_TO_STORAGE", true);
            } else if (id == R.id.nav_recommend) {
                intent = new Intent(activity, RecommendActivity.class);
            } else if (id == R.id.nav_inquiry) {
                intent = new Intent(activity, InquiryActivity.class);
            }

            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                activity.startActivity(intent);
                if (!(activity instanceof MainActivity)) activity.finish();
                return true;
            }
            return false;
        });
    }
}
