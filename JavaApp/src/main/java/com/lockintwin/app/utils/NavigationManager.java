package com.lockintwin.app.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages navigation between different screens in the application
 */
public class NavigationManager {
    
    private static final Logger logger = LoggerFactory.getLogger(NavigationManager.class);
    private static Stage stage;
    private static final Map<String, String> SCREEN_PATHS = new HashMap<>();
    
    static {
        SCREEN_PATHS.put("splash", "/fxml/splash.fxml");
        SCREEN_PATHS.put("onboarding", "/fxml/onboarding.fxml");
        SCREEN_PATHS.put("permission", "/fxml/permission.fxml");
        SCREEN_PATHS.put("calibration", "/fxml/calibration.fxml");
        SCREEN_PATHS.put("main-monitoring", "/fxml/main-monitoring.fxml");
        SCREEN_PATHS.put("settings", "/fxml/settings.fxml");
        SCREEN_PATHS.put("new-session", "/fxml/new-session.fxml");
        SCREEN_PATHS.put("break", "/fxml/break.fxml");
        SCREEN_PATHS.put("stats", "/fxml/stats.fxml");
    }
    
    public static void initialize(Stage primaryStage) {
        stage = primaryStage;
    }
    
    public static void navigateTo(String screenName) {
        navigateTo(screenName, null);
    }
    
    public static void navigateTo(String screenName, Object data) {
        String fxmlPath = SCREEN_PATHS.get(screenName);
        if (fxmlPath == null) {
            logger.error("Unknown screen: {}", screenName);
            return;
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(NavigationManager.class.getResource(fxmlPath));
            Scene scene = new Scene(loader.load());
            
            // Apply global stylesheet
            scene.getStylesheets().add(
                NavigationManager.class.getResource("/css/styles.css").toExternalForm()
            );
            
            // Pass data to controller if provided
            if (data != null && loader.getController() != null) {
                // Controllers can implement a setData method to receive navigation data
                try {
                    loader.getController().getClass()
                        .getMethod("setData", Object.class)
                        .invoke(loader.getController(), data);
                } catch (Exception e) {
                    logger.debug("Controller does not have setData method", e);
                }
            }
            
            stage.setScene(scene);
            logger.info("Navigated to screen: {}", screenName);
            
        } catch (IOException e) {
            logger.error("Failed to load screen: {}", screenName, e);
        }
    }
    
    public static Stage getStage() {
        return stage;
    }
}
