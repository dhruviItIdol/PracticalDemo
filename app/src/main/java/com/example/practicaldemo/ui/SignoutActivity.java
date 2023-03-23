package com.example.practicaldemo.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.practicaldemo.databinding.ActivitySignoutBinding;
import com.example.practicaldemo.databinding.ActivitySignupBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SignoutActivity extends AppCompatActivity {
    ActivitySignoutBinding binding;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignoutBinding.inflate(getLayoutInflater());
        auth= FirebaseAuth.getInstance();
        setContentView(binding.mainView);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}