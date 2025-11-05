package com.example.studytrackerbasictest.databases;

import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class SessionDatabase {
    private final FirebaseFirestore db;

    public SessionDatabase() {
        db = FirebaseFirestore.getInstance();
    }

    public void addSession(String name, String date, String duration, String username) {
        Map<String, Object> session = new HashMap<>();
        session.put("name", name);
        session.put("date", date);
        session.put("duration", duration);
        session.put("user", username);


        db.collection("sessions")
                .add(session)
                .addOnSuccessListener(documentReference ->
                        Log.d("SessionDatabase", "✅ Session added with ID: " + documentReference.getId()))
                .addOnFailureListener(e ->
                        Log.e("SessionDatabase", "❌ Error adding session", e));
    }
}