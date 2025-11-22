package com.example.studytrackerbasictest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.studytrackerbasictest.databases.SessionDatabase;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class AnalyticsFragment extends Fragment {

    ListView sessionListView;
    ArrayAdapter<String> adapter;
    ArrayList<String> sessionList = new ArrayList<>();
    String username;

    private static final String PREFS_NAME = "AppPrefs";

    @Override
    public View onCreateView(LayoutInflater infl, ViewGroup parent, Bundle savedInstanceState) {
        View v = infl.inflate(R.layout.fragment_analytics, parent, false);

        sessionListView = v.findViewById(R.id.sessionListView);
        adapter = new ArrayAdapter<>(requireActivity(),
                R.layout.list_item_sessions,
                R.id.sessionText,
                sessionList);
        sessionListView.setAdapter(adapter);

        // Get username
        username = getActivity().getIntent().getStringExtra("username");
        if (username == null || username.isEmpty()) {
            SharedPreferences prefs = requireActivity()
                    .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            username = prefs.getString("logged_in_user", null);
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (username != null && !username.isEmpty()) {
            loadSessions();   // refresh when returning here
        }
    }

    private void loadSessions() {
        SessionDatabase db = new SessionDatabase();
        db.getSessionsForUser(username, sessions -> {
            sessionList.clear();
            for (Map<String, Object> s : sessions) {
                String name = (String) s.get("name");
                String date = (String) s.get("date");
                String duration = (String) s.get("duration");
                Object fs = s.get("focusScore");

                String fsStr = (fs == null)
                        ? "—"
                        : String.format(Locale.getDefault(), "%.1f",
                        ((Number) fs).doubleValue());

                sessionList.add(name + " — " + date + " — Focus " + fsStr+" — " + duration + " min");
            }

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
            }
        });
    }
}
