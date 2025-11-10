package com.example.studytrackerbasictest.databases;

import androidx.annotation.Nullable;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
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
        db.collection("sessions")
                .whereEqualTo("user", username)
                .get()
                .addOnSuccessListener(snapshot -> {
                    int nextIndex = snapshot.size() + 1;
                    String sessionName = "Session " + nextIndex;

                    Map<String, Object> session = new HashMap<>();
                    session.put("name", sessionName);
                    session.put("date", date);
                    session.put("duration", duration);
                    session.put("user", username);

                    db.collection("sessions").add(session);
                });
    }

    public void upsertSessionById(String sessionId, String date, String duration, String username,
                                  @Nullable Double focusScore) {

        if (sessionId == null || sessionId.isEmpty()) {
            sessionId = "local_" + System.currentTimeMillis();
        }

        Map<String, Object> data = new HashMap<>();
        data.put("user", username);
        data.put("date", date);
        data.put("duration", duration);
        data.put("name", "Session " + sessionId.substring(0, Math.min(6, sessionId.length())));

        if (focusScore != null) {
            data.put("focusScore", focusScore);
        }

        db.collection("sessions")
                .document(sessionId)
                .set(data, SetOptions.merge());
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

    public interface OnSessionsLoadedListener {
        void onSessionsLoaded(List<Map<String, Object>> sessions);
    }
}
