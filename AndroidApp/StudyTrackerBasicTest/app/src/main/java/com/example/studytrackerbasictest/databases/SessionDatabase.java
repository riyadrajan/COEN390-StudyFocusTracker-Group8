package com.example.studytrackerbasictest.databases;

import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionDatabase {
    private final FirebaseFirestore db;

    public SessionDatabase() {
        db = FirebaseFirestore.getInstance();
    }

    public void addSession(String date, String duration, String username) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("sessions")
                .whereEqualTo("user", username)
                .get()
                .addOnSuccessListener(snapshot -> {
                    int nextIndex = snapshot.size() + 1; // total sessions for this user
                    String sessionName = "Session " + nextIndex;

                    Map<String, Object> session = new HashMap<>();
                    session.put("name", sessionName);
                    session.put("date", date);
                    session.put("duration", duration);
                    session.put("user", username);

                    db.collection("sessions").add(session);

                });
    }

    public void getSessionsForUser(String username, OnSessionsLoadedListener listener) {
        db.collection("sessions")
                .whereEqualTo("user", username)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Map<String, Object>> sessions = new ArrayList<>();
                    for (var doc : querySnapshot.getDocuments()) {
                        sessions.add(doc.getData());
                    }
                    listener.onSessionsLoaded(sessions);
                });
    }

    // Callback interface
    public interface OnSessionsLoadedListener {
        void onSessionsLoaded(List<Map<String, Object>> sessions);
    }

}