package com.example.fotnewsapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DevInfoActivity extends AppCompatActivity {

    private EditText editName, editStudentNumber, editPersonalStatement, editReleaseVersion;
    private Button btnClearInfo, btnSubmit, btnSignOut;
    private TextView tvUsername, tvEmail;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_info);

        // Initialize views
        editName = findViewById(R.id.EditName);
        editStudentNumber = findViewById(R.id.EditStudentNumber);
        editPersonalStatement = findViewById(R.id.EditPersonalStatement);
        editReleaseVersion = findViewById(R.id.EditReleaseVersion);
        btnClearInfo = findViewById(R.id.btnEditInfo); // Clear Info button
        btnSubmit = findViewById(R.id.btnSubmit); // Submit button
        btnSignOut = findViewById(R.id.btnSignOut);
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("UserProfile");

        // Retrieve data passed from LoginActivity
        String username = getIntent().getStringExtra("username");
        String email = getIntent().getStringExtra("email");

        // Display user info
        tvUsername.setText("Username: " + (username != null ? username : "N/A"));
        tvEmail.setText("Email: " + (email != null ? email : "N/A"));

        // Load user information
        loadUserInfo();

        // Clear Info button functionality
        btnClearInfo.setOnClickListener(v -> clearUserInfoFields());

        // Submit button functionality
        btnSubmit.setOnClickListener(v -> submitUserInfoToDatabase());

        btnSignOut.setOnClickListener(v -> showSignOutConfirmation());

        // Set up Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set bottom navigation listener
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(DevInfoActivity.this, MainActivity.class));
                return true;
            } else if (itemId == R.id.edit_info) {
                startActivity(new Intent(DevInfoActivity.this, DevInfoActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(DevInfoActivity.this, ProfileActivity.class));
                return false;
            } else if (itemId == R.id.nav_saved) {
                // Handle saved click
                return true;
            } else if (itemId == R.id.nav_settings) {
                startActivity(new Intent(DevInfoActivity.this, SettingsActivity.class));
                return true;
            }
            return true;
        });

    }

    // Function to load user information from Firebase
    private void loadUserInfo() {
        if (firebaseAuth.getCurrentUser() != null) {
            String username = firebaseAuth.getCurrentUser().getDisplayName();
            String userEmail = firebaseAuth.getCurrentUser().getEmail();

            tvEmail.setText("Email: " + (userEmail != null ? userEmail : "N/A"));

            if (username != null) {
                databaseReference.child(username).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        UserProfile userProfile = task.getResult().getValue(UserProfile.class);
                        if (userProfile != null) {
                            tvUsername.setText("Username: " + userProfile.getName());
                        } else {
                            tvUsername.setText("Username: Not found");
                        }
                    } else {
                        Toast.makeText(this, "Failed to load user information.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                tvUsername.setText("Username: Not found");
            }
        } else {
            tvEmail.setText("Email: Not logged in");
            tvUsername.setText("Username: Not logged in");
        }
    }

    // Function to clear all user information fields
    private void clearUserInfoFields() {
        editName.setText("");
        editStudentNumber.setText("");
        editPersonalStatement.setText("");
        editReleaseVersion.setText("");
        Toast.makeText(this, "Fields Cleared. Enter new data.", Toast.LENGTH_SHORT).show();
    }

    // Function to submit user information to the database
    private void submitUserInfoToDatabase() {
        String name = editName.getText().toString().trim();
        String studentNumber = editStudentNumber.getText().toString().trim();
        String personalStatement = editPersonalStatement.getText().toString().trim();
        String releaseVersion = editReleaseVersion.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(studentNumber) ||
                TextUtils.isEmpty(personalStatement) || TextUtils.isEmpty(releaseVersion)) {
            Toast.makeText(this, "Please fill in all fields before submitting.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (firebaseAuth.getCurrentUser() != null) {
            String username = firebaseAuth.getCurrentUser().getUid();

            users users = new users(name, studentNumber, personalStatement, releaseVersion);

            databaseReference.child(username).setValue(users)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "User Information Updated Successfully.", Toast.LENGTH_SHORT).show();
                        clearUserInfoFields();
                        loadUserInfo();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed to Update Information. Try Again.", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
        }
    }

    // Function to show sign-out confirmation dialog
    private void showSignOutConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Sign Out")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Yes", (dialog, which) -> signOutUser())
                .setNegativeButton("No", null)
                .show();
    }

    // Function to sign out the user
    private void signOutUser() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
        Toast.makeText(this, "Signed out successfully.", Toast.LENGTH_SHORT).show();
    }
}
