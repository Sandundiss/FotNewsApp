package com.example.fotnewsapp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG);

        Button btnEditInfo = findViewById(R.id.btnEditInfo);
        Button btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        Button btnSignOut = findViewById(R.id.btnSignOut);

        btnEditInfo.setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, DevInfoActivity.class));
        });


        btnDeleteAccount.setOnClickListener(v -> showDeleteAccountDialog());


        btnSignOut.setOnClickListener(v -> signOutUser());


    }


    private void showDeleteAccountDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
                .setPositiveButton("Yes", (dialog, which) -> deleteAccount())
                .setNegativeButton("No", null)
                .show();
    }


    private void deleteAccount() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();


            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("UserProfile").child(userId);


            userRef.removeValue()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Proceed to delete user from Firebase Auth
                            user.delete()
                                    .addOnCompleteListener(authTask -> {
                                        if (authTask.isSuccessful()) {
                                            Toast.makeText(SettingsActivity.this, "Account deleted successfully.", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(SettingsActivity.this, "Failed to delete user authentication. " + authTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(SettingsActivity.this, "Failed to remove user data from database. " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }


    private void signOutUser() {
        firebaseAuth.signOut();
        Toast.makeText(this, "Signed out successfully.", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
        finish();
    }
}