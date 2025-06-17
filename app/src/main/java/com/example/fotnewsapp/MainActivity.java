package com.example.fotnewsapp;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSports = findViewById(R.id.btnSports);
        Button btnAcademic = findViewById(R.id.btnAcademic);
        Button btnEvents = findViewById(R.id.btnEvents);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);


        loadFragment(new SportsFragment());

        btnSports.setOnClickListener(v -> loadFragment(new SportsFragment()));
        btnAcademic.setOnClickListener(v -> loadFragment(new AcademicFragment()));
        btnEvents.setOnClickListener(v -> loadFragment(new EventsFragment()));


        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                return true;
            } else if (itemId == R.id.edit_info) {
                startActivity(new Intent(MainActivity.this, DevInfoActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                return true;
            } else if (itemId == R.id.nav_settings) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.newsContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}