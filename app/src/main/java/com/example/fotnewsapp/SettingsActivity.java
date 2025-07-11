package com.example.fotnewsapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize buttons
        Button btnEditInfo = findViewById(R.id.btnEditInfo);
        Button btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        Button btnSignOut = findViewById(R.id.btnSignOut);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set default selected item
        bottomNavigationView.setSelectedItemId(R.id.nav_settings);

        // Set up button listeners
        btnEditInfo.setOnClickListener(v -> navigateTo(DevInfoActivity.class));
        btnDeleteAccount.setOnClickListener(v -> showDeleteAccountDialog());
        btnSignOut.setOnClickListener(v -> signOutUser());

        // Handle BottomNavigationView item selection
        bottomNavigationView.setOnItemSelectedListener(item -> handleBottomNavigation(item.getItemId()));
    }

    /**
     * Navigates to a specified activity.
     *
     * @param targetActivity The activity to navigate to.
     */
    private void navigateTo(Class<?> targetActivity) {
        Intent intent = new Intent(SettingsActivity.this, targetActivity);
        startActivity(intent);
    }

    /**
     * Shows a confirmation dialog for account deletion.
     */
    private void showDeleteAccountDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
                .setPositiveButton("Yes", (dialog, which) -> deleteAccount())
                .setNegativeButton("No", null)
                .show();
    }

    /**
     * Deletes the user account from Firebase Authentication and the database.
     */
    private void deleteAccount() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user == null) {
            Toast.makeText(this, "No user is logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("UserProfile").child(userId);

        // Remove user data from Firebase Realtime Database
        userRef.removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        user.delete()
                                .addOnCompleteListener(authTask -> {
                                    if (authTask.isSuccessful()) {
                                        onAccountDeletionSuccess();
                                    } else {
                                        onAccountDeletionFailure(authTask.getException());
                                    }
                                });
                    } else {
                        onDatabaseRemovalFailure(task.getException());
                    }
                });
    }

    /**
     * Signs out the current user and navigates to the login screen.
     */
    private void signOutUser() {
        firebaseAuth.signOut();
        Toast.makeText(this, "Signed out successfully.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Handles BottomNavigationView item clicks.
     *
     * @param itemId The selected item ID.
     * @return True if the event was handled; otherwise, false.
     */
    private boolean handleBottomNavigation(int itemId) {
        if (itemId == R.id.nav_home) {
            navigateTo(MainActivity.class);
            return true;
        } else if (itemId == R.id.edit_info) {
            navigateTo(DevInfoActivity.class);
            return true;
        } else if (itemId == R.id.nav_profile) {
            navigateTo(ProfileActivity.class);
            return true;
        }else if (itemId == R.id.nav_settings) {
            // Current activity, no action needed
            return true;
        }
        return false;
    }


    /**
     * Handles successful account deletion.
     */
    private void onAccountDeletionSuccess() {
        Toast.makeText(SettingsActivity.this, "Account deleted successfully.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Handles failures during the database removal process.
     *
     * @param exception The exception thrown during the database operation.
     */
    private void onDatabaseRemovalFailure(Exception exception) {
        String errorMessage = exception != null ? exception.getMessage() : "Unknown error occurred.";
        Toast.makeText(SettingsActivity.this, "Failed to remove user data. " + errorMessage, Toast.LENGTH_LONG).show();
    }

    /**
     * Handles failures during user authentication deletion.
     *
     * @param exception The exception thrown during the authentication operation.
     */
    private void onAccountDeletionFailure(Exception exception) {
        String errorMessage = exception != null ? exception.getMessage() : "Unknown error occurred.";
        Toast.makeText(SettingsActivity.this, "Failed to delete user authentication. " + errorMessage, Toast.LENGTH_LONG).show();
    }
}
