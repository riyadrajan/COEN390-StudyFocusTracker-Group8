package com.lockintwin.app.models;

import java.time.LocalDateTime;

/**
 * Represents attention monitoring data at a specific point in time
 */
public class AttentionData {
    
    private Long id;
    private Long sessionId;
    private LocalDateTime timestamp;
    private double attentionScore;
    private double headPoseYaw;
    private double headPosePitch;
    private double headPoseRoll;
    private double eyeAspectRatio;
    private boolean isDistracted;
    
    public AttentionData() {
        this.timestamp = LocalDateTime.now();
    }
    
    public AttentionData(Long sessionId, double attentionScore) {
        this();
        this.sessionId = sessionId;
        this.attentionScore = attentionScore;
    }
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public double getAttentionScore() {
        return attentionScore;
    }
    
    public void setAttentionScore(double attentionScore) {
        this.attentionScore = attentionScore;
    }
    
    public double getHeadPoseYaw() {
        return headPoseYaw;
    }
    
    public void setHeadPoseYaw(double headPoseYaw) {
        this.headPoseYaw = headPoseYaw;
    }
    
    public double getHeadPosePitch() {
        return headPosePitch;
    }
    
    public void setHeadPosePitch(double headPosePitch) {
        this.headPosePitch = headPosePitch;
    }
    
    public double getHeadPoseRoll() {
        return headPoseRoll;
    }
    
    public void setHeadPoseRoll(double headPoseRoll) {
        this.headPoseRoll = headPoseRoll;
    }
    
    public double getEyeAspectRatio() {
        return eyeAspectRatio;
    }
    
    public void setEyeAspectRatio(double eyeAspectRatio) {
        this.eyeAspectRatio = eyeAspectRatio;
    }
    
    public boolean isDistracted() {
        return isDistracted;
    }
    
    public void setDistracted(boolean distracted) {
        isDistracted = distracted;
    }
    
    @Override
    public String toString() {
        return "AttentionData{" +
                "sessionId=" + sessionId +
                ", timestamp=" + timestamp +
                ", attentionScore=" + attentionScore +
                ", isDistracted=" + isDistracted +
                '}';
    }
}
