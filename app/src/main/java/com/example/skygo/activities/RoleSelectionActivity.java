package com.example.skygo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.skygo.R;

public class RoleSelectionActivity extends AppCompatActivity {

    private Button buttonUserLogin, buttonAdminLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);

        buttonUserLogin = findViewById(R.id.buttonUserLogin);
        buttonAdminLogin = findViewById(R.id.buttonAdminLogin);

        buttonUserLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

        buttonAdminLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminLoginActivity.class));
        });
    }
}
