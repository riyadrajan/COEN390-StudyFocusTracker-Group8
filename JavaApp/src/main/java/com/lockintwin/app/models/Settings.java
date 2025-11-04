package com.lockintwin.app.models;

/**
 * Application settings
 */
public class Settings {
    
    private int sessionDuration;
    private int breakDuration;
    private int longBreakDuration;
    private int sessionsBeforeLongBreak;
    private boolean soundEnabled;
    private boolean notificationsEnabled;
    private int cameraIndex;
    private double attentionThreshold;
    private String backendUrl;
    
    public Settings() {
        // Default values
        this.sessionDuration = 25;
        this.breakDuration = 5;
        this.longBreakDuration = 15;
        this.sessionsBeforeLongBreak = 4;
        this.soundEnabled = true;
        this.notificationsEnabled = true;
        this.cameraIndex = 0;
        this.attentionThreshold = 0.6;
        this.backendUrl = "http://localhost:5000";
    }
    
    // Getters and Setters
    
    public int getSessionDuration() {
        return sessionDuration;
    }
    
    public void setSessionDuration(int sessionDuration) {
        this.sessionDuration = sessionDuration;
    }
    
    public int getBreakDuration() {
        return breakDuration;
    }
    
    public void setBreakDuration(int breakDuration) {
        this.breakDuration = breakDuration;
    }
    
    public int getLongBreakDuration() {
        return longBreakDuration;
    }
    
    public void setLongBreakDuration(int longBreakDuration) {
        this.longBreakDuration = longBreakDuration;
    }
    
    public int getSessionsBeforeLongBreak() {
        return sessionsBeforeLongBreak;
    }
    
    public void setSessionsBeforeLongBreak(int sessionsBeforeLongBreak) {
        this.sessionsBeforeLongBreak = sessionsBeforeLongBreak;
    }
    
    public boolean isSoundEnabled() {
        return soundEnabled;
    }
    
    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
    }
    
    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }
    
    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }
    
    public int getCameraIndex() {
        return cameraIndex;
    }
    
    public void setCameraIndex(int cameraIndex) {
        this.cameraIndex = cameraIndex;
    }
    
    public double getAttentionThreshold() {
        return attentionThreshold;
    }
    
    public void setAttentionThreshold(double attentionThreshold) {
        this.attentionThreshold = attentionThreshold;
    }
    
    public String getBackendUrl() {
        return backendUrl;
    }
    
    public void setBackendUrl(String backendUrl) {
        this.backendUrl = backendUrl;
    }
}
