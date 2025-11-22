package com.example.studytrackerbasictest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.studytrackerbasictest.databases.SessionDatabase;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    OkHttpClient client;
    String BASE_URL = "http://172.20.10.4:3000";

    TextView welcomeText, timerText;
    Button toggleBtn;

    String username;
    String currentSessionId = null;

    private Handler handler = new Handler();
    private boolean isRunning = false;
    private int seconds = 0;

    static final String PREFS_NAME = "AppPrefs";
    static final String KEY_IP = "server_ip";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        welcomeText = v.findViewById(R.id.welcomeText);
        timerText   = v.findViewById(R.id.timerText);
        toggleBtn   = v.findViewById(R.id.toggleBtn);

        username = getActivity().getIntent().getStringExtra("username");
        if (username != null)
            welcomeText.setText("Welcome, " + username + "!");

        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        String savedIp = prefs.getString(KEY_IP, "");
        if (!savedIp.isEmpty()) BASE_URL = "http://" + savedIp + ":3000";

        client = new OkHttpClient();

        toggleBtn.setOnClickListener(vw -> {
            if (!isRunning) {
                sendRequest("/start");
                startTimer();
                startFocusSession(username);

                isRunning = true;
                toggleBtn.setText("Stop");
                toggleBtn.setBackgroundResource(R.drawable.round_button_red);

            } else {
                sendRequest("/stop");
                stopTimer();

                String duration = String.format(Locale.getDefault(), "%02d:%02d", seconds / 60, seconds % 60);
                String date = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new java.util.Date());
                seconds = 0;

                stopFocusSessionAndSave(date, duration, username);

                isRunning = false;
                toggleBtn.setText("Start");
                toggleBtn.setBackgroundResource(R.drawable.round_button_green);
            }
        });

        return v;
    }

    // ---------- Timer ----------
    private void startTimer() {
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
        @Override public void run() {
            if (isRunning) {
                seconds++;
                timerText.setText(String.format("Running: %02d:%02d", seconds / 60, seconds % 60));
                handler.postDelayed(this, 1000);
            }
        }
    };

    // ---------- Flask start ----------
    private void sendRequest(String endpoint) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        String bodyText = "{}";
        if (endpoint.equals("/start")) {
            JSONObject obj = new JSONObject();
            try { obj.put("username", username); } catch (Exception ignored) {}
            bodyText = obj.toString();
        }

        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .post(RequestBody.create(bodyText, JSON))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {}
            @Override public void onResponse(Call call, Response response) {}
        });
    }

    private void startFocusSession(String username) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String body = "{\"username\":\"" + username + "\"}";

        Request request = new Request.Builder()
                .url(BASE_URL + "/session/start")
                .post(RequestBody.create(body, JSON))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {}

            @Override public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject json = new JSONObject(response.body().string());
                    currentSessionId = json.optString("sessionId", null);
                } catch (Exception ignored) {}
            }
        });
    }

    private void stopFocusSessionAndSave(String date, String duration, String username) {

        Request request = new Request.Builder()
                .url(BASE_URL + "/session/stop")
                .post(RequestBody.create("{}", MediaType.parse("application/json; charset=utf-8")))
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override public void onFailure(Call call, IOException e) {
                SessionDatabase db = new SessionDatabase();
                db.saveSession(currentSessionId, date, duration, username, null);
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                Double focusScore = null;

                try {
                    JSONObject json = new JSONObject(response.body().string());
                    if (json.has("focusScore"))
                        focusScore = json.getDouble("focusScore");
                } catch (Exception ignored) {}

                SessionDatabase db = new SessionDatabase();
                db.saveSession(currentSessionId, date, duration, username, focusScore);

                currentSessionId = null;
            }
        });
    }
}
