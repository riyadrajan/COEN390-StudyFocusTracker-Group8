package com.lockintwin.app.controllers;

import com.lockintwin.app.models.Settings;
import com.lockintwin.app.services.DatabaseService;
import com.lockintwin.app.services.NotificationService;
import com.lockintwin.app.utils.NavigationManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controller for the settings screen
 */
public class SettingsController {
    
    @FXML
    private Spinner<Integer> sessionDurationSpinner;
    
    @FXML
    private Spinner<Integer> breakDurationSpinner;
    
    @FXML
    private Spinner<Integer> longBreakDurationSpinner;
    
    @FXML
    private Spinner<Integer> sessionsBeforeLongBreakSpinner;
    
    @FXML
    private CheckBox soundEnabledCheckbox;
    
    @FXML
    private CheckBox notificationsEnabledCheckbox;
    
    @FXML
    private Spinner<Integer> cameraIndexSpinner;
    
    @FXML
    private Slider attentionThresholdSlider;
    
    @FXML
    private Label attentionThresholdLabel;
    
    @FXML
    private TextField backendUrlField;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Button testConnectionButton;
    
    private Settings currentSettings;
    
    @FXML
    public void initialize() {
        setupSpinners();
        loadSettings();
        
        saveButton.setOnAction(event -> handleSave());
        cancelButton.setOnAction(event -> handleCancel());
        testConnectionButton.setOnAction(event -> handleTestConnection());
        
        attentionThresholdSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            attentionThresholdLabel.setText(String.format("%.2f", newVal.doubleValue()));
        });
    }
    
    private void setupSpinners() {
        sessionDurationSpinner.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 120, 25, 5)
        );
        
        breakDurationSpinner.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 30, 5, 1)
        );
        
        longBreakDurationSpinner.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 60, 15, 5)
        );
        
        sessionsBeforeLongBreakSpinner.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 4, 1)
        );
        
        cameraIndexSpinner.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, 0, 1)
        );
    }
    
    private void loadSettings() {
        currentSettings = DatabaseService.getInstance().getSettings();
        
        sessionDurationSpinner.getValueFactory().setValue(currentSettings.getSessionDuration());
        breakDurationSpinner.getValueFactory().setValue(currentSettings.getBreakDuration());
        longBreakDurationSpinner.getValueFactory().setValue(currentSettings.getLongBreakDuration());
        sessionsBeforeLongBreakSpinner.getValueFactory().setValue(currentSettings.getSessionsBeforeLongBreak());
        soundEnabledCheckbox.setSelected(currentSettings.isSoundEnabled());
        notificationsEnabledCheckbox.setSelected(currentSettings.isNotificationsEnabled());
        cameraIndexSpinner.getValueFactory().setValue(currentSettings.getCameraIndex());
        attentionThresholdSlider.setValue(currentSettings.getAttentionThreshold());
        backendUrlField.setText(currentSettings.getBackendUrl());
    }
    
    private void handleSave() {
        currentSettings.setSessionDuration(sessionDurationSpinner.getValue());
        currentSettings.setBreakDuration(breakDurationSpinner.getValue());
        currentSettings.setLongBreakDuration(longBreakDurationSpinner.getValue());
        currentSettings.setSessionsBeforeLongBreak(sessionsBeforeLongBreakSpinner.getValue());
        currentSettings.setSoundEnabled(soundEnabledCheckbox.isSelected());
        currentSettings.setNotificationsEnabled(notificationsEnabledCheckbox.isSelected());
        currentSettings.setCameraIndex(cameraIndexSpinner.getValue());
        currentSettings.setAttentionThreshold(attentionThresholdSlider.getValue());
        currentSettings.setBackendUrl(backendUrlField.getText());
        
        DatabaseService.getInstance().saveSettings(currentSettings);
        
        NotificationService.getInstance().showNotification(
            "Settings Saved",
            "Your preferences have been updated successfully.",
            NotificationService.NotificationType.SUCCESS
        );
        
        NavigationManager.navigateTo("new-session");
    }
    
    private void handleCancel() {
        NavigationManager.navigateTo("new-session");
    }
    
    private void handleTestConnection() {
        // Test backend connection
        testConnectionButton.setDisable(true);
        testConnectionButton.setText("Testing...");
        
        com.lockintwin.app.services.AttentionMonitorService.getInstance()
            .checkBackendConnection()
            .thenAccept(success -> {
                javafx.application.Platform.runLater(() -> {
                    if (success) {
                        NotificationService.getInstance().showNotification(
                            "Connection Successful",
                            "Backend server is reachable.",
                            NotificationService.NotificationType.SUCCESS
                        );
                    } else {
                        NotificationService.getInstance().showNotification(
                            "Connection Failed",
                            "Could not reach backend server. Please check if it's running.",
                            NotificationService.NotificationType.ERROR
                        );
                    }
                    
                    testConnectionButton.setDisable(false);
                    testConnectionButton.setText("Test Connection");
                });
            });
    }
}
