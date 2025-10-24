package com.example.studytrackerbasictest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.Locale;

import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;

public class MainActivity extends AppCompatActivity {

    OkHttpClient client;
    private static final String BASE_URL = "http://10.0.2.2:3000";

    TextView welcomeText, timerText;
    Button startBtn, stopBtn,logoutBtn;

    private Handler handler = new Handler();
    private boolean isRunning = false;
    private int seconds = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // UI setup
        welcomeText = findViewById(R.id.welcomeText);
        timerText = findViewById(R.id.timerText);
        startBtn = findViewById(R.id.startBtn);
        stopBtn = findViewById(R.id.stopBtn);
        logoutBtn = findViewById(R.id.logoutBtn);


        // Get username from Intent
        String username = getIntent().getStringExtra("username");
        if (username != null) {
            welcomeText.setText("Welcome, " + username + "!");
        }

        // Initialize HTTP client
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        client = new OkHttpClient.Builder().addInterceptor(logging).build();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Button listeners
        startBtn.setOnClickListener(v -> {
            sendRequest("/start");
            startTimer();
        });

        stopBtn.setOnClickListener(v -> {
            sendRequest("/stop");
            stopTimer();
        });

        logoutBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // closes MainActivity so user can’t go back
        });

    }

    // --- Timer functions ---
    private void startTimer() {
        if (isRunning) return;
        isRunning = true;
        seconds = 0;
        timerText.setText("Running: 00:00");
        handler.postDelayed(timerRunnable, 1000);
    }

    private void stopTimer() {
        isRunning = false;
        handler.removeCallbacks(timerRunnable);
        timerText.setText(""); // clears the timer
    }

    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (isRunning) {
                seconds++;
                int mins = seconds / 60;
                int secs = seconds % 60;
                String time = String.format(Locale.getDefault(), "Running: %02d:%02d", mins, secs);
                timerText.setText(time);
                handler.postDelayed(this, 1000);
            }
        }
    };

    // --- Networking ---
    private void sendRequest(String endpoint) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create("{}", JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Request failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Response: " + response.code(), Toast.LENGTH_SHORT).show());
            }
        });
    }
}
