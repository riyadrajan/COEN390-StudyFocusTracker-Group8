package com.lockintwin.app.controllers;

import com.lockintwin.app.services.CameraService;
import com.lockintwin.app.utils.NavigationManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/**
 * Controller for the camera permission screen
 */
public class PermissionController {
    
    @FXML
    private Label titleLabel;
    
    @FXML
    private Label descriptionLabel;
    
    @FXML
    private ImageView cameraIcon;
    
    @FXML
    private Button allowButton;
    
    @FXML
    private Button skipButton;
    
    @FXML
    public void initialize() {
        allowButton.setOnAction(event -> handleAllow());
        skipButton.setOnAction(event -> handleSkip());
    }
    
    private void handleAllow() {
        // Test camera access
        CameraService cameraService = CameraService.getInstance();
        boolean cameraStarted = cameraService.startCamera(0);
        
        if (cameraStarted) {
            cameraService.stopCamera();
            NavigationManager.navigateTo("calibration");
        } else {
            // Show error
            descriptionLabel.setText("Failed to access camera. Please check your permissions and try again.");
            descriptionLabel.setStyle("-fx-text-fill: #ef4444;");
        }
    }
    
    private void handleSkip() {
        NavigationManager.navigateTo("new-session");
    }
}
