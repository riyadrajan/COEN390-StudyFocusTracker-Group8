package com.lockintwin.app.controllers;

import com.lockintwin.app.models.Session;
import com.lockintwin.app.services.DatabaseService;
import com.lockintwin.app.utils.NavigationManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

/**
 * Controller for creating a new study session
 */
public class NewSessionController {
    
    @FXML
    private TextField sessionTitleField;
    
    @FXML
    private Spinner<Integer> durationSpinner;
    
    @FXML
    private Button startButton;
    
    @FXML
    private Button viewStatsButton;
    
    @FXML
    private Button settingsButton;
    
    @FXML
    public void initialize() {
        // Set up duration spinner (5 to 120 minutes)
        SpinnerValueFactory<Integer> valueFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 120, 25, 5);
        durationSpinner.setValueFactory(valueFactory);
        
        startButton.setOnAction(event -> handleStartSession());
        viewStatsButton.setOnAction(event -> NavigationManager.navigateTo("stats"));
        settingsButton.setOnAction(event -> NavigationManager.navigateTo("settings"));
    }
    
    private void handleStartSession() {
        String title = sessionTitleField.getText();
        if (title == null || title.trim().isEmpty()) {
            title = "Study Session";
        }
        
        int duration = durationSpinner.getValue();
        
        Session session = new Session(title, duration);
        Long sessionId = DatabaseService.getInstance().saveSession(session);
        session.setId(sessionId);
        
        NavigationManager.navigateTo("main-monitoring", session);
    }
}
