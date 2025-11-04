package com.lockintwin.app.controllers;

import com.lockintwin.app.models.Session;
import com.lockintwin.app.services.DatabaseService;
import com.lockintwin.app.utils.NavigationManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller for the statistics screen
 */
public class StatsController {
    
    @FXML
    private Label totalSessionsLabel;
    
    @FXML
    private Label totalTimeLabel;
    
    @FXML
    private Label averageAttentionLabel;
    
    @FXML
    private TableView<Session> sessionsTable;
    
    @FXML
    private TableColumn<Session, String> titleColumn;
    
    @FXML
    private TableColumn<Session, String> dateColumn;
    
    @FXML
    private TableColumn<Session, Integer> durationColumn;
    
    @FXML
    private TableColumn<Session, Double> attentionColumn;
    
    @FXML
    private BarChart<String, Number> sessionChart;
    
    @FXML
    private PieChart statusPieChart;
    
    @FXML
    private Button backButton;
    
    @FXML
    public void initialize() {
        backButton.setOnAction(event -> NavigationManager.navigateTo("new-session"));
        
        setupTable();
        loadStatistics();
    }
    
    private void setupTable() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        
        dateColumn.setCellValueFactory(cellData -> {
            String date = cellData.getValue().getStartTime()
                .format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));
            return new javafx.beans.property.SimpleStringProperty(date);
        });
        
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        
        attentionColumn.setCellValueFactory(cellData -> {
            double attention = cellData.getValue().getAverageAttention() * 100;
            return new javafx.beans.property.SimpleDoubleProperty(attention).asObject();
        });
    }
    
    private void loadStatistics() {
        List<Session> sessions = DatabaseService.getInstance().getAllSessions();
        
        // Update summary statistics
        totalSessionsLabel.setText(String.valueOf(sessions.size()));
        
        int totalMinutes = sessions.stream()
            .mapToInt(Session::getDuration)
            .sum();
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        totalTimeLabel.setText(String.format("%dh %dm", hours, minutes));
        
        double avgAttention = sessions.stream()
            .mapToDouble(Session::getAverageAttention)
            .average()
            .orElse(0.0);
        averageAttentionLabel.setText(String.format("%.1f%%", avgAttention * 100));
        
        // Populate table
        sessionsTable.setItems(FXCollections.observableArrayList(sessions));
        
        // Update bar chart (last 7 sessions)
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Session Duration");
        
        sessions.stream()
            .limit(7)
            .forEach(session -> {
                series.getData().add(new XYChart.Data<>(
                    session.getTitle(),
                    session.getDuration()
                ));
            });
        
        sessionChart.getData().clear();
        sessionChart.getData().add(series);
        
        // Update pie chart (session status distribution)
        long completed = sessions.stream()
            .filter(s -> s.getStatus() == Session.SessionStatus.COMPLETED)
            .count();
        long cancelled = sessions.stream()
            .filter(s -> s.getStatus() == Session.SessionStatus.CANCELLED)
            .count();
        
        statusPieChart.setData(FXCollections.observableArrayList(
            new PieChart.Data("Completed", completed),
            new PieChart.Data("Cancelled", cancelled)
        ));
    }
}
