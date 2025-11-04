package com.lockintwin.app.controllers;

import com.lockintwin.app.utils.NavigationManager;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

/**
 * Controller for the splash screen
 */
public class SplashController {
    
    @FXML
    private Label titleLabel;
    
    @FXML
    private Label subtitleLabel;
    
    @FXML
    private ProgressBar loadingBar;
    
    @FXML
    public void initialize() {
        // Simulate loading and navigate to onboarding after 2 seconds
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> NavigationManager.navigateTo("onboarding"));
        delay.play();
    }
}
