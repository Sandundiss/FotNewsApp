package com.example.fotnewsapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        Button btnEditInfo = findViewById(R.id.btnEditInfo);
        Button btnExit = findViewById(R.id.btnExit);

        btnEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, DevInfoActivity.class);
                startActivity(intent);
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Set bottom navigation listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                return true;
            } else if (itemId == R.id.edit_info) {
                startActivity(new Intent(ProfileActivity.this, DevInfoActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
                return false;
            }else if (itemId == R.id.nav_settings) {
                startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
                return true;
            }
            return true;
        });

    }
}