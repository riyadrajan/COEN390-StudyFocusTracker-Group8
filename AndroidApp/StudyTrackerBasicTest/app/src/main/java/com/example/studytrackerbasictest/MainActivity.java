package com.example.studytrackerbasictest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.Locale;

import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;

public class MainActivity extends AppCompatActivity {

    OkHttpClient client;
    private static String BASE_URL = "http://172.20.10.4:3000";

    private static final String PREFS_NAME = "AppPrefs";
    private static final String KEY_IP = "server_ip";

    TextView welcomeText, timerText;
    Button toggleBtn;

    private Handler handler = new Handler();
    private boolean isRunning = false;
    private int seconds = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // --- Toolbar setup ---
        Toolbar toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        // --- UI setup ---
        welcomeText = findViewById(R.id.welcomeText);
        timerText = findViewById(R.id.timerText);
        toggleBtn = findViewById(R.id.toggleBtn);

        // --- Display username ---
        String username = getIntent().getStringExtra("username");
        if (username != null)
            welcomeText.setText("Welcome, " + username + "!");

        // --- Load saved IP ---
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedIp = prefs.getString(KEY_IP, "");
        if (!savedIp.isEmpty()) {
            BASE_URL = "http://" + savedIp + ":3000";
        }

        // --- HTTP client setup ---
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        client = new OkHttpClient.Builder().addInterceptor(logging).build();

        // --- Insets ---
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // --- start/stop Button listener ---
        toggleBtn.setOnClickListener(v -> {
            if (!isRunning) {
                sendRequest("/start");
                startTimer();
                isRunning = true;
                toggleBtn.setText("Stop");
                toggleBtn.setTextColor(getResources().getColor(R.color.red_primary));
                toggleBtn.setBackgroundResource(R.drawable.round_button_red);
            } else {
                sendRequest("/stop");
                stopTimer();
                isRunning = false;
                toggleBtn.setText("Start");
                toggleBtn.setTextColor(getResources().getColor(R.color.green_primary));
                toggleBtn.setBackgroundResource(R.drawable.round_button_green);
            }
        });
    }

    // Pause timer if user leaves app
    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(timerRunnable);
        isRunning = false;
    }

    // --- Toolbar menu setup ---
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    // --- Handle Settings menu ---
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivityForResult(intent, 100); // get result back
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // --- Handle returned IP from Settings ---
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            String newIp = data.getStringExtra("new_ip");
            if (newIp != null) {
                BASE_URL = "http://" + newIp + ":3000";
                Toast.makeText(this, "Server updated to: " + newIp, Toast.LENGTH_SHORT).show();
            }
        }
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
        timerText.setText("");
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
