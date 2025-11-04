package com.lockintwin.app.controllers;

import com.lockintwin.app.models.AttentionData;
import com.lockintwin.app.models.Session;
import com.lockintwin.app.services.AttentionMonitorService;
import com.lockintwin.app.services.CameraService;
import com.lockintwin.app.services.DatabaseService;
import com.lockintwin.app.services.NotificationService;
import com.lockintwin.app.utils.NavigationManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the main monitoring screen during an active session
 */
public class MainMonitoringController {
    
    @FXML
    private Label sessionTitleLabel;
    
    @FXML
    private Label timerLabel;
    
    @FXML
    private Label attentionScoreLabel;
    
    @FXML
    private ProgressBar attentionProgressBar;
    
    @FXML
    private ImageView cameraPreview;
    
    @FXML
    private LineChart<String, Number> attentionChart;
    
    @FXML
    private Button pauseButton;
    
    @FXML
    private Button endSessionButton;
    
    private Session currentSession;
    private Timeline timer;
    private int elapsedSeconds = 0;
    private List<Double> attentionScores = new ArrayList<>();
    private XYChart.Series<String, Number> chartSeries;
    
    @FXML
    public void initialize() {
        pauseButton.setOnAction(event -> handlePause());
        endSessionButton.setOnAction(event -> handleEndSession());
        
        // Set up chart
        chartSeries = new XYChart.Series<>();
        chartSeries.setName("Attention Score");
        attentionChart.getData().add(chartSeries);
    }
    
    public void setData(Object data) {
        if (data instanceof Session) {
            this.currentSession = (Session) data;
            startSession();
        }
    }
    
    private void startSession() {
        sessionTitleLabel.setText(currentSession.getTitle());
        
        // Start camera
        CameraService cameraService = CameraService.getInstance();
        cameraService.startCamera(0);
        cameraService.startFrameCapture(image -> {
            Platform.runLater(() -> cameraPreview.setImage(image));
        });
        
        // Start attention monitoring
        AttentionMonitorService.getInstance().startMonitoring(
            currentSession.getId(),
            this::handleAttentionData
        );
        
        // Start timer
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            elapsedSeconds++;
            updateTimer();
            
            // Check if session duration is reached
            if (elapsedSeconds >= currentSession.getDuration() * 60) {
                handleSessionComplete();
            }
            
            // Check for break reminder (every 25 minutes)
            if (elapsedSeconds % (25 * 60) == 0 && elapsedSeconds > 0) {
                NotificationService.getInstance().showBreakReminder(5);
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }
    
    private void handleAttentionData(AttentionData data) {
        Platform.runLater(() -> {
            double score = data.getAttentionScore();
            attentionScores.add(score);
            
            // Update UI
            attentionScoreLabel.setText(String.format("%.1f%%", score * 100));
            attentionProgressBar.setProgress(score);
            
            // Update chart (keep last 60 data points)
            chartSeries.getData().add(new XYChart.Data<>(
                String.valueOf(elapsedSeconds), score * 100
            ));
            
            if (chartSeries.getData().size() > 60) {
                chartSeries.getData().remove(0);
            }
            
            // Check for low attention
            if (data.isDistracted()) {
                NotificationService.getInstance().showLowAttentionAlert();
            }
        });
    }
    
    private void updateTimer() {
        int hours = elapsedSeconds / 3600;
        int minutes = (elapsedSeconds % 3600) / 60;
        int seconds = elapsedSeconds % 60;
        
        timerLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }
    
    private void handlePause() {
        if (timer.getStatus() == Timeline.Status.RUNNING) {
            timer.pause();
            pauseButton.setText("Resume");
            currentSession.setStatus(Session.SessionStatus.PAUSED);
        } else {
            timer.play();
            pauseButton.setText("Pause");
            currentSession.setStatus(Session.SessionStatus.ACTIVE);
        }
    }
    
    private void handleEndSession() {
        stopSession();
        currentSession.setStatus(Session.SessionStatus.CANCELLED);
        updateSessionInDatabase();
        NavigationManager.navigateTo("new-session");
    }
    
    private void handleSessionComplete() {
        stopSession();
        currentSession.setStatus(Session.SessionStatus.COMPLETED);
        updateSessionInDatabase();
        
        NotificationService.getInstance().showSessionComplete(currentSession.getTitle());
        NavigationManager.navigateTo("stats");
    }
    
    private void stopSession() {
        if (timer != null) {
            timer.stop();
        }
        
        AttentionMonitorService.getInstance().stopMonitoring();
        CameraService.getInstance().stopCamera();
        
        currentSession.setEndTime(LocalDateTime.now());
    }
    
    private void updateSessionInDatabase() {
        // Calculate average attention
        if (!attentionScores.isEmpty()) {
            double avgAttention = attentionScores.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
            currentSession.setAverageAttention(avgAttention);
        }
        
        DatabaseService.getInstance().updateSession(currentSession);
    }
}
