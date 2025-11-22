package com.example.studytrackerbasictest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    private static final String PREFS_NAME = "AppPrefs";
    private static final String KEY_IP = "server_ip";

    EditText ipInput;
    Button saveBtn, logoutBtn;

    public SettingsFragment() {}

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ipInput = view.findViewById(R.id.ipInput);
        saveBtn = view.findViewById(R.id.saveBtn);
        logoutBtn = view.findViewById(R.id.logoutBtn);

        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        ipInput.setText(prefs.getString(KEY_IP, ""));

        saveBtn.setOnClickListener(v -> {
            String ip = ipInput.getText().toString().trim();
            if (TextUtils.isEmpty(ip)) {
                Toast.makeText(requireContext(), "Enter a valid IP", Toast.LENGTH_SHORT).show();
                return;
            }
            prefs.edit().putString(KEY_IP, ip).apply();
            Toast.makeText(requireContext(), "IP saved", Toast.LENGTH_SHORT).show();
        });

        logoutBtn.setOnClickListener(v -> {
            // Clear stored login state if needed
            prefs.edit().remove("logged_in_user").apply();

            Intent i = new Intent(requireContext(), LoginActivity.class);
            startActivity(i);
            requireActivity().finish();
        });
    }
}
