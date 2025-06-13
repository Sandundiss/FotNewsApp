package com.example.fotnewsapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etConfirmPassword, etEmail;
    private TextView usernameError, passwordError, confirmPasswordError, emailError;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etEmail = findViewById(R.id.etEmail);
        usernameError = findViewById(R.id.usernameError);
        passwordError = findViewById(R.id.passwordError);
        confirmPasswordError = findViewById(R.id.confirmPasswordError);
        emailError = findViewById(R.id.emailError);

        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        boolean isValid = true;

        if (username.isEmpty()) {
            usernameError.setText("Username cannot be empty");
            usernameError.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            usernameError.setVisibility(View.GONE);
        }

        if (password.isEmpty()) {
            passwordError.setText("Password cannot be empty");
            passwordError.setVisibility(View.VISIBLE);
            isValid = false;
        } else if (password.length() < 6) {
            passwordError.setText("Password must be at least 6 characters");
            passwordError.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            passwordError.setVisibility(View.GONE);
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordError.setText("Confirm your password");
            confirmPasswordError.setVisibility(View.VISIBLE);
            isValid = false;
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordError.setText("Passwords do not match");
            confirmPasswordError.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            confirmPasswordError.setVisibility(View.GONE);
        }

        if (email.isEmpty()) {
            emailError.setText("Email cannot be empty");
            emailError.setVisibility(View.VISIBLE);
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError.setText("Invalid email address");
            emailError.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            emailError.setVisibility(View.GONE);
        }

        if (!isValid) return;

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();
                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("username", username);
                            userMap.put("email", email);

                            databaseReference.child(userId).setValue(userMap)
                                    .addOnCompleteListener(dbTask -> {
                                        if (dbTask.isSuccessful()) {
                                            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(this, "Failed to save user data: " + dbTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(this, "Email already registered. Try logging in.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
