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

        editName = findViewById(R.id.EditName);
        editStudentNumber = findViewById(R.id.EditStudentNumber);
        editPersonalStatement = findViewById(R.id.EditPersonalStatement);
        editReleaseVersion = findViewById(R.id.EditReleaseVersion);
        btnClearInfo = findViewById(R.id.btnEditInfo); // Clear Info button
        btnSubmit = findViewById(R.id.btnSubmit); // Submit button
        btnSignOut = findViewById(R.id.btnSignOut);
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("UserProfile");

        String username = getIntent().getStringExtra("username");
        String email = getIntent().getStringExtra("email");

        tvUsername.setText("Username: " + (username != null ? username : "N/A"));
        tvEmail.setText("Email: " + (email != null ? email : "N/A"));

        loadUserInfo();

        btnClearInfo.setOnClickListener(v -> clearUserInfoFields());
        btnSubmit.setOnClickListener(v -> submitUserInfoToDatabase());
        btnSignOut.setOnClickListener(v -> showSignOutConfirmation());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

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
                return true;
            } else if (itemId == R.id.nav_settings) {
                startActivity(new Intent(DevInfoActivity.this, SettingsActivity.class));
                return true;
            }
            return false;
        });
    }

    private void loadUserInfo() {
        if (firebaseAuth.getCurrentUser() != null) {
            String userEmail = firebaseAuth.getCurrentUser().getEmail();
            tvEmail.setText("Email: " + (userEmail != null ? userEmail : "N/A"));

            String userId = firebaseAuth.getCurrentUser().getUid();

            databaseReference.child(userId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    UserProfile userProfile = task.getResult().getValue(UserProfile.class);
                    if (userProfile != null && !TextUtils.isEmpty(userProfile.getName())) {
                        tvUsername.setText("Username: " + userProfile.getName());
                    } else {
                        tvUsername.setText("Username: Not set");
                    }
                } else {
                    tvUsername.setText("Username: Not found");
                    Toast.makeText(this, "Failed to load user information.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            tvEmail.setText("Email: Not logged in");
            tvUsername.setText("Username: Not logged in");
        }
    }

    private void clearUserInfoFields() {
        editName.setText("");
        editStudentNumber.setText("");
        editPersonalStatement.setText("");
        editReleaseVersion.setText("");
        Toast.makeText(this, "Fields Cleared. Enter new data.", Toast.LENGTH_SHORT).show();
    }

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
            String userId = firebaseAuth.getCurrentUser().getUid();

            users users = new users(name, studentNumber, personalStatement, releaseVersion);

            databaseReference.child(userId).setValue(users)
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

    private void showSignOutConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Sign Out")
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Yes", (dialog, which) -> signOutUser())
                .setNegativeButton("No", null)
                .show();
    }

    private void signOutUser() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
        Toast.makeText(this, "Signed out successfully.", Toast.LENGTH_SHORT).show();
    }
}
