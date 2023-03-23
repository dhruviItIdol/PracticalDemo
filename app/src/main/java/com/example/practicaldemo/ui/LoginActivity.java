package com.example.practicaldemo.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.practicaldemo.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
     ActivityLoginBinding binding;
    FirebaseAuth auth;  //FirebaseAuth Instance
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.mainLayout);
        auth= FirebaseAuth.getInstance();
        binding.idBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButtonClicked(v);
            }
        });
        binding.idBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignUpClicked(v);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        binding.loginProgress.setVisibility(View.GONE);
    }
    //Login button click
    public void loginButtonClicked(View view) {
        String Email = binding.idEdtEmail.getText().toString();
        final String pass = binding.idEdtPassword.getText().toString();

        //Validation section
        if (TextUtils.isEmpty(Email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }
        binding.loginProgress.setVisibility(View.VISIBLE);
        if (pass.length() < 6) {
            binding.idEdtPassword.setError("Should be greater than 6");
        }
        auth.signInWithEmailAndPassword(Email, pass)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_LONG).show();
                            Log.e("MyTag", task.getException().toString());

                        } else {
                            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
    public void onSignUpClicked(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
    }
}