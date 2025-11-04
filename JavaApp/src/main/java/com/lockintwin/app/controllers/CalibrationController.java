package com.lockintwin.app.controllers;

import com.lockintwin.app.services.CameraService;
import com.lockintwin.app.utils.NavigationManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/**
 * Controller for the camera calibration screen
 */
public class CalibrationController {
    
    @FXML
    private Label titleLabel;
    
    @FXML
    private Label instructionLabel;
    
    @FXML
    private ImageView cameraPreview;
    
    @FXML
    private Button calibrateButton;
    
    @FXML
    private Button continueButton;
    
    private boolean isCalibrated = false;
    
    @FXML
    public void initialize() {
        calibrateButton.setOnAction(event -> handleCalibrate());
        continueButton.setOnAction(event -> handleContinue());
        continueButton.setDisable(true);
        
        startCameraPreview();
    }
    
    private void startCameraPreview() {
        CameraService cameraService = CameraService.getInstance();
        cameraService.startCamera(0);
        cameraService.startFrameCapture(image -> {
            cameraPreview.setImage(image);
        });
    }
    
    private void handleCalibrate() {
        // Simulate calibration
        instructionLabel.setText("Calibration successful! You can now continue.");
        instructionLabel.setStyle("-fx-text-fill: #22c55e;");
        isCalibrated = true;
        continueButton.setDisable(false);
        calibrateButton.setDisable(true);
    }
    
    private void handleContinue() {
        CameraService.getInstance().stopCamera();
        NavigationManager.navigateTo("new-session");
    }
}
