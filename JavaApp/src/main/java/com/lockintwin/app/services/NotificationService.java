package com.lockintwin.app.services;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Manages system notifications and alerts
 */
public class NotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private static NotificationService instance;
    
    private NotificationService() {}
    
    public static synchronized NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }
    
    public void showNotification(String title, String message, NotificationType type) {
        Platform.runLater(() -> {
            Popup popup = new Popup();
            
            StackPane container = new StackPane();
            container.setStyle(getStyleForType(type));
            container.setPrefWidth(300);
            container.setPrefHeight(80);
            
            Label titleLabel = new Label(title);
            titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
            
            Label messageLabel = new Label(message);
            messageLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");
            messageLabel.setWrapText(true);
            
            javafx.scene.layout.VBox content = new javafx.scene.layout.VBox(5);
            content.setAlignment(Pos.CENTER);
            content.getChildren().addAll(titleLabel, messageLabel);
            
            container.getChildren().add(content);
            popup.getContent().add(container);
            
            Stage stage = com.lockintwin.app.App.getPrimaryStage();
            if (stage != null) {
                popup.show(stage, 
                    stage.getX() + stage.getWidth() - 320,
                    stage.getY() + 20
                );
                
                // Auto-hide after 5 seconds
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(popup::hide);
                    }
                }, 5000);
            }
            
            logger.info("Notification shown: {} - {}", title, message);
        });
    }
    
    private String getStyleForType(NotificationType type) {
        String baseStyle = "-fx-background-color: %s; -fx-background-radius: 8; -fx-padding: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);";
        
        return switch (type) {
            case SUCCESS -> String.format(baseStyle, "#22c55e");
            case WARNING -> String.format(baseStyle, "#f59e0b");
            case ERROR -> String.format(baseStyle, "#ef4444");
            case INFO -> String.format(baseStyle, "#6366f1");
        };
    }
    
    public void showBreakReminder(int breakDuration) {
        showNotification(
            "Time for a Break!",
            "You've been studying for a while. Take a " + breakDuration + " minute break.",
            NotificationType.INFO
        );
    }
    
    public void showLowAttentionAlert() {
        showNotification(
            "Attention Alert",
            "Your attention seems to be wandering. Take a moment to refocus.",
            NotificationType.WARNING
        );
    }
    
    public void showSessionComplete(String sessionTitle) {
        showNotification(
            "Session Complete!",
            "Great job! You've completed your " + sessionTitle + " session.",
            NotificationType.SUCCESS
        );
    }
    
    public enum NotificationType {
        SUCCESS, WARNING, ERROR, INFO
    }
}
