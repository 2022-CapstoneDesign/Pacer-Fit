package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {
    private Button loginBtn;
    private Button joinBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        loginBtn = findViewById(R.id.introLoginBtn);
        joinBtn = findViewById(R.id.introJoinBtn);

        loginBtn.setOnClickListener(v -> {
            startActivity(new Intent(IntroActivity.this, LoginActivity.class));
            finish();
        });

        joinBtn.setOnClickListener(v -> {
            startActivity(new Intent(IntroActivity.this, JoinActivity.class));
            finish();
        });

    }
}