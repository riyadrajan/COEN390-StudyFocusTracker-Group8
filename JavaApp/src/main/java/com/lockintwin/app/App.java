package com.lockintwin.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import com.lockintwin.app.utils.NavigationManager;
import com.lockintwin.app.services.DatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Main application entry point for Lock In Twin Study Focus Tracker
 */
public class App extends Application {
    
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static Stage primaryStage;
    
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        
        // Initialize database
        DatabaseService.getInstance().initialize();
        
        // Set up the primary stage
        primaryStage.setTitle("Lock In Twin - Study Focus Tracker");
        primaryStage.setWidth(1200);
        primaryStage.setHeight(800);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        
        // Set application icon
        try {
            primaryStage.getIcons().add(new Image(
                getClass().getResourceAsStream("/images/icon.png")
            ));
        } catch (Exception e) {
            logger.warn("Could not load application icon", e);
        }
        
        // Initialize navigation manager
        NavigationManager.initialize(primaryStage);
        
        // Navigate to splash screen
        NavigationManager.navigateTo("splash");
        
        primaryStage.show();
        
        logger.info("Application started successfully");
    }
    
    @Override
    public void stop() {
        logger.info("Application shutting down");
        // Clean up resources
        DatabaseService.getInstance().close();
    }
    
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
