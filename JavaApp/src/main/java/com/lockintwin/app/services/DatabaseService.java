package com.lockintwin.app.services;

import com.lockintwin.app.models.AttentionData;
import com.lockintwin.app.models.Session;
import com.lockintwin.app.models.Settings;
import com.lockintwin.app.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages database operations for the application
 */
public class DatabaseService {
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);
    private static DatabaseService instance;
    private Connection connection;
    
    private DatabaseService() {}
    
    public static synchronized DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }
    
    public void initialize() {
        try {
            // Create directory if it doesn't exist
            File dbDir = new File(Constants.DB_PATH);
            if (!dbDir.exists()) {
                dbDir.mkdirs();
            }
            
            String url = "jdbc:sqlite:" + Constants.DB_PATH + Constants.DB_NAME;
            connection = DriverManager.getConnection(url);
            
            createTables();
            logger.info("Database initialized successfully");
            
        } catch (SQLException e) {
            logger.error("Failed to initialize database", e);
        }
    }
    
    private void createTables() throws SQLException {
        String createSessionsTable = """
            CREATE TABLE IF NOT EXISTS sessions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                start_time TEXT NOT NULL,
                end_time TEXT,
                duration INTEGER NOT NULL,
                average_attention REAL DEFAULT 0,
                breaks_taken INTEGER DEFAULT 0,
                notes TEXT,
                status TEXT NOT NULL
            )
        """;
        
        String createAttentionDataTable = """
            CREATE TABLE IF NOT EXISTS attention_data (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                session_id INTEGER NOT NULL,
                timestamp TEXT NOT NULL,
                attention_score REAL NOT NULL,
                head_pose_yaw REAL,
                head_pose_pitch REAL,
                head_pose_roll REAL,
                eye_aspect_ratio REAL,
                is_distracted INTEGER DEFAULT 0,
                FOREIGN KEY (session_id) REFERENCES sessions(id)
            )
        """;
        
        String createSettingsTable = """
            CREATE TABLE IF NOT EXISTS settings (
                id INTEGER PRIMARY KEY CHECK (id = 1),
                session_duration INTEGER DEFAULT 25,
                break_duration INTEGER DEFAULT 5,
                long_break_duration INTEGER DEFAULT 15,
                sessions_before_long_break INTEGER DEFAULT 4,
                sound_enabled INTEGER DEFAULT 1,
                notifications_enabled INTEGER DEFAULT 1,
                camera_index INTEGER DEFAULT 0,
                attention_threshold REAL DEFAULT 0.6,
                backend_url TEXT DEFAULT 'http://localhost:5000'
            )
        """;
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createSessionsTable);
            stmt.execute(createAttentionDataTable);
            stmt.execute(createSettingsTable);
            
            // Insert default settings if not exists
            stmt.execute("INSERT OR IGNORE INTO settings (id) VALUES (1)");
        }
    }
    
    // Session operations
    
    public Long saveSession(Session session) {
        String sql = """
            INSERT INTO sessions (title, start_time, duration, status)
            VALUES (?, ?, ?, ?)
        """;
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, session.getTitle());
            pstmt.setString(2, session.getStartTime().toString());
            pstmt.setInt(3, session.getDuration());
            pstmt.setString(4, session.getStatus().name());
            
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to save session", e);
        }
        return null;
    }
    
    public void updateSession(Session session) {
        String sql = """
            UPDATE sessions
            SET end_time = ?, average_attention = ?, breaks_taken = ?, notes = ?, status = ?
            WHERE id = ?
        """;
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, session.getEndTime() != null ? session.getEndTime().toString() : null);
            pstmt.setDouble(2, session.getAverageAttention());
            pstmt.setInt(3, session.getBreaksTaken());
            pstmt.setString(4, session.getNotes());
            pstmt.setString(5, session.getStatus().name());
            pstmt.setLong(6, session.getId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to update session", e);
        }
    }
    
    public List<Session> getAllSessions() {
        List<Session> sessions = new ArrayList<>();
        String sql = "SELECT * FROM sessions ORDER BY start_time DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Session session = new Session();
                session.setId(rs.getLong("id"));
                session.setTitle(rs.getString("title"));
                session.setStartTime(LocalDateTime.parse(rs.getString("start_time")));
                String endTime = rs.getString("end_time");
                if (endTime != null) {
                    session.setEndTime(LocalDateTime.parse(endTime));
                }
                session.setDuration(rs.getInt("duration"));
                session.setAverageAttention(rs.getDouble("average_attention"));
                session.setBreaksTaken(rs.getInt("breaks_taken"));
                session.setNotes(rs.getString("notes"));
                session.setStatus(Session.SessionStatus.valueOf(rs.getString("status")));
                
                sessions.add(session);
            }
        } catch (SQLException e) {
            logger.error("Failed to get sessions", e);
        }
        
        return sessions;
    }
    
    // Attention data operations
    
    public void saveAttentionData(AttentionData data) {
        String sql = """
            INSERT INTO attention_data (session_id, timestamp, attention_score, 
                head_pose_yaw, head_pose_pitch, head_pose_roll, eye_aspect_ratio, is_distracted)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, data.getSessionId());
            pstmt.setString(2, data.getTimestamp().toString());
            pstmt.setDouble(3, data.getAttentionScore());
            pstmt.setDouble(4, data.getHeadPoseYaw());
            pstmt.setDouble(5, data.getHeadPosePitch());
            pstmt.setDouble(6, data.getHeadPoseRoll());
            pstmt.setDouble(7, data.getEyeAspectRatio());
            pstmt.setInt(8, data.isDistracted() ? 1 : 0);
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to save attention data", e);
        }
    }
    
    public List<AttentionData> getAttentionDataForSession(Long sessionId) {
        List<AttentionData> dataList = new ArrayList<>();
        String sql = "SELECT * FROM attention_data WHERE session_id = ? ORDER BY timestamp";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, sessionId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    AttentionData data = new AttentionData();
                    data.setId(rs.getLong("id"));
                    data.setSessionId(rs.getLong("session_id"));
                    data.setTimestamp(LocalDateTime.parse(rs.getString("timestamp")));
                    data.setAttentionScore(rs.getDouble("attention_score"));
                    data.setHeadPoseYaw(rs.getDouble("head_pose_yaw"));
                    data.setHeadPosePitch(rs.getDouble("head_pose_pitch"));
                    data.setHeadPoseRoll(rs.getDouble("head_pose_roll"));
                    data.setEyeAspectRatio(rs.getDouble("eye_aspect_ratio"));
                    data.setDistracted(rs.getInt("is_distracted") == 1);
                    
                    dataList.add(data);
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to get attention data", e);
        }
        
        return dataList;
    }
    
    // Settings operations
    
    public Settings getSettings() {
        Settings settings = new Settings();
        String sql = "SELECT * FROM settings WHERE id = 1";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                settings.setSessionDuration(rs.getInt("session_duration"));
                settings.setBreakDuration(rs.getInt("break_duration"));
                settings.setLongBreakDuration(rs.getInt("long_break_duration"));
                settings.setSessionsBeforeLongBreak(rs.getInt("sessions_before_long_break"));
                settings.setSoundEnabled(rs.getInt("sound_enabled") == 1);
                settings.setNotificationsEnabled(rs.getInt("notifications_enabled") == 1);
                settings.setCameraIndex(rs.getInt("camera_index"));
                settings.setAttentionThreshold(rs.getDouble("attention_threshold"));
                settings.setBackendUrl(rs.getString("backend_url"));
            }
        } catch (SQLException e) {
            logger.error("Failed to get settings", e);
        }
        
        return settings;
    }
    
    public void saveSettings(Settings settings) {
        String sql = """
            UPDATE settings SET
                session_duration = ?,
                break_duration = ?,
                long_break_duration = ?,
                sessions_before_long_break = ?,
                sound_enabled = ?,
                notifications_enabled = ?,
                camera_index = ?,
                attention_threshold = ?,
                backend_url = ?
            WHERE id = 1
        """;
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, settings.getSessionDuration());
            pstmt.setInt(2, settings.getBreakDuration());
            pstmt.setInt(3, settings.getLongBreakDuration());
            pstmt.setInt(4, settings.getSessionsBeforeLongBreak());
            pstmt.setInt(5, settings.isSoundEnabled() ? 1 : 0);
            pstmt.setInt(6, settings.isNotificationsEnabled() ? 1 : 0);
            pstmt.setInt(7, settings.getCameraIndex());
            pstmt.setDouble(8, settings.getAttentionThreshold());
            pstmt.setString(9, settings.getBackendUrl());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to save settings", e);
        }
    }
    
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("Database connection closed");
            }
        } catch (SQLException e) {
            logger.error("Failed to close database connection", e);
        }
    }
}
