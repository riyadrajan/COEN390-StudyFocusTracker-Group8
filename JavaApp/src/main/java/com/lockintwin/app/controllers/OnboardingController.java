package com.lockintwin.app.controllers;

import com.lockintwin.app.utils.NavigationManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Controller for the onboarding screen
 */
public class OnboardingController {
    
    @FXML
    private Label titleLabel;
    
    @FXML
    private Label descriptionLabel;
    
    @FXML
    private Button getStartedButton;
    
    @FXML
    public void initialize() {
        getStartedButton.setOnAction(event -> handleGetStarted());
    }
    
    private void handleGetStarted() {
        NavigationManager.navigateTo("permission");
    }
}
