package com.lockintwin.app.models;

import java.time.LocalDateTime;

/**
 * Represents a study session
 */
public class Session {
    
    private Long id;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int duration; // in minutes
    private double averageAttention;
    private int breaksTaken;
    private String notes;
    private SessionStatus status;
    
    public enum SessionStatus {
        ACTIVE, PAUSED, COMPLETED, CANCELLED
    }
    
    public Session() {
        this.status = SessionStatus.ACTIVE;
        this.startTime = LocalDateTime.now();
    }
    
    public Session(String title, int duration) {
        this();
        this.title = title;
        this.duration = duration;
    }
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    public double getAverageAttention() {
        return averageAttention;
    }
    
    public void setAverageAttention(double averageAttention) {
        this.averageAttention = averageAttention;
    }
    
    public int getBreaksTaken() {
        return breaksTaken;
    }
    
    public void setBreaksTaken(int breaksTaken) {
        this.breaksTaken = breaksTaken;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public SessionStatus getStatus() {
        return status;
    }
    
    public void setStatus(SessionStatus status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", status=" + status +
                '}';
    }
}
