package com.example.studytrackerbasictest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "AppPrefs";
    private static final String KEY_IP = "server_ip";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // --- Toolbar setup ---
        Toolbar toolbar = findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Settings");
        }

        // --- UI references ---
        EditText ipEditText = findViewById(R.id.ipAddressEditText);
        Button saveIpBtn = findViewById(R.id.saveIpBtn);
        Button logoutBtn = findViewById(R.id.logoutBtn);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedIp = prefs.getString(KEY_IP, "");
        if (!savedIp.isEmpty()) ipEditText.setText(savedIp);

        // --- Save IP ---
        saveIpBtn.setOnClickListener(v -> {
            String enteredIp = ipEditText.getText().toString().trim();
            if (enteredIp.isEmpty()) {
                Toast.makeText(this, "Please enter a valid IP", Toast.LENGTH_SHORT).show();
            } else {
                prefs.edit().putString(KEY_IP, enteredIp).apply();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("new_ip", enteredIp);
                setResult(RESULT_OK, resultIntent);
                Toast.makeText(this, "IP saved!", Toast.LENGTH_SHORT).show();
            }
        });

        // --- Logout ---
        logoutBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}


