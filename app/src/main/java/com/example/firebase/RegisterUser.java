package com.example.firebase;

import static java.util.Objects.requireNonNull;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener{

    private EditText fullName_input, age_input, email_input, password_input, password_input_rewrite;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        TextView banner = findViewById(R.id.banner);
        banner.setOnClickListener(this);

        Button registerUser = findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        fullName_input = findViewById(R.id.fullName_input);
        age_input = findViewById(R.id.age_input);
        email_input = findViewById(R.id.email_input);
        password_input = findViewById(R.id.password_input);
        password_input_rewrite= findViewById(R.id.password_input_rewrite);

        progressBar = findViewById(R.id.progressBar);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.banner:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.registerUser:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String fullNameValue = fullName_input.getText().toString().trim();
        String ageValue = age_input.getText().toString().trim();
        String emailValue = email_input.getText().toString().trim();
        String passwordValue = password_input.getText().toString().trim();
        String passwordRewrite = password_input_rewrite.getText().toString().trim();

        if (fullNameValue.isEmpty()) {
            fullName_input.setError("Full name is required");
            fullName_input.requestFocus();
            return;
        }


        if (ageValue.isEmpty()) {
            age_input.setError("Age is required");
            age_input.requestFocus();
            return;
        }


        if (emailValue.isEmpty()) {
            email_input.setError("Email is required");
            email_input.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailValue).matches()) {
            password_input.setError("Please provide valid email");
            password_input.requestFocus();
            return;
        }

        if (passwordValue.isEmpty()) {
            password_input.setError("Password is required");
            password_input.requestFocus();
            return;
        }

        if (passwordValue.length() < 6) {
            password_input.setError("Password must at least 6 characters");
            password_input.requestFocus();
            return;
        }

        if (!passwordRewrite.equals(passwordValue)) {
            password_input_rewrite.setError("The password must be the same");
            password_input_rewrite.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(emailValue, passwordValue)
                .addOnCompleteListener(this, task -> {

                    if (task.isSuccessful()) {
                        User user = new User(fullNameValue, ageValue, emailValue);

                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(requireNonNull(requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()))
                                .setValue(user)
                                .addOnCompleteListener(task1 -> {

                                    if (task1.isSuccessful()) {
                                        Toast.makeText(RegisterUser.this, "User has been register successfully", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.VISIBLE);
                                        startActivity(new Intent(RegisterUser.this, MainActivity.class));
                                    } else {
                                        Toast.makeText(RegisterUser.this, "Failed to register ! Try again !", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                    } else {
                        Toast.makeText(RegisterUser.this, "Failed to register", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}