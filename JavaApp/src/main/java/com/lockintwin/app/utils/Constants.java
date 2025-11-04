package com.lockintwin.app.utils;

/**
 * Application-wide constants
 */
public class Constants {
    
    // Backend API
    public static final String BACKEND_URL = "http://localhost:5000";
    public static final String ATTENTION_ENDPOINT = "/api/attention";
    
    // Camera settings
    public static final int DEFAULT_CAMERA_INDEX = 0;
    public static final int CAMERA_WIDTH = 640;
    public static final int CAMERA_HEIGHT = 480;
    public static final int CAMERA_FPS = 30;
    
    // Session settings
    public static final int DEFAULT_SESSION_DURATION = 25; // minutes
    public static final int DEFAULT_BREAK_DURATION = 5; // minutes
    public static final int DEFAULT_LONG_BREAK_DURATION = 15; // minutes
    public static final int SESSIONS_BEFORE_LONG_BREAK = 4;
    
    // Attention thresholds
    public static final double ATTENTION_THRESHOLD_LOW = 0.3;
    public static final double ATTENTION_THRESHOLD_MEDIUM = 0.6;
    public static final double ATTENTION_THRESHOLD_HIGH = 0.8;
    
    // Database
    public static final String DB_NAME = "study_tracker.db";
    public static final String DB_PATH = System.getProperty("user.home") + "/.lockintwin/";
    
    // UI Colors
    public static final String COLOR_PRIMARY = "#6366f1";
    public static final String COLOR_BACKGROUND = "#0a0a0a";
    public static final String COLOR_SURFACE = "#1a1a1a";
    public static final String COLOR_TEXT_PRIMARY = "#ffffff";
    public static final String COLOR_TEXT_SECONDARY = "#a0a0a0";
    public static final String COLOR_SUCCESS = "#22c55e";
    public static final String COLOR_WARNING = "#f59e0b";
    public static final String COLOR_ERROR = "#ef4444";
    
    // Notification settings
    public static final int NOTIFICATION_DURATION = 5000; // milliseconds
    
    private Constants() {
        // Prevent instantiation
    }
}
