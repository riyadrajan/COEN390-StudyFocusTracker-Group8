package com.lockintwin.app.controllers;

import com.lockintwin.app.utils.NavigationManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

/**
 * Controller for the break screen
 */
public class BreakController {
    
    @FXML
    private Label titleLabel;
    
    @FXML
    private Label timerLabel;
    
    @FXML
    private ProgressBar breakProgressBar;
    
    @FXML
    private Button skipBreakButton;
    
    @FXML
    private Button endBreakButton;
    
    private Timeline timer;
    private int breakDuration = 5 * 60; // 5 minutes in seconds
    private int elapsedSeconds = 0;
    
    @FXML
    public void initialize() {
        skipBreakButton.setOnAction(event -> handleSkipBreak());
        endBreakButton.setOnAction(event -> handleEndBreak());
        
        startBreakTimer();
    }
    
    private void startBreakTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            elapsedSeconds++;
            updateTimer();
            
            if (elapsedSeconds >= breakDuration) {
                handleBreakComplete();
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }
    
    private void updateTimer() {
        int remainingSeconds = breakDuration - elapsedSeconds;
        int minutes = remainingSeconds / 60;
        int seconds = remainingSeconds % 60;
        
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
        breakProgressBar.setProgress((double) elapsedSeconds / breakDuration);
    }
    
    private void handleSkipBreak() {
        stopTimer();
        NavigationManager.navigateTo("main-monitoring");
    }
    
    private void handleEndBreak() {
        stopTimer();
        NavigationManager.navigateTo("new-session");
    }
    
    private void handleBreakComplete() {
        stopTimer();
        NavigationManager.navigateTo("main-monitoring");
    }
    
    private void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }
}
