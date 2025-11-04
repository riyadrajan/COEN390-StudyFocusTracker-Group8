# Lock In Twin - Java Desktop Application

A Java desktop application for study focus tracking with real-time attention monitoring using computer vision.

## Features

- 📹 Real-time webcam-based attention monitoring
- 📊 Session tracking and statistics
- ⏱️ Pomodoro-style break reminders
- 🎯 Focus score calculation
- 💾 Local SQLite database for session history
- 🎨 Modern JavaFX UI

## Requirements

- Java 17 or higher
- Maven 3.6+
- Webcam
- Python 3.11+ (for state detection backend)

## Project Structure

```
JavaApp/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── lockintwin/
│       │           └── app/
│       │               ├── App.java                    # Main application entry
│       │               ├── controllers/
│       │               │   ├── SplashController.java
│       │               │   ├── OnboardingController.java
│       │               │   ├── PermissionController.java
│       │               │   ├── CalibrationController.java
│       │               │   ├── MainMonitoringController.java
│       │               │   ├── SettingsController.java
│       │               │   ├── NewSessionController.java
│       │               │   ├── BreakController.java
│       │               │   └── StatsController.java
│       │               ├── models/
│       │               │   ├── Session.java
│       │               │   ├── AttentionData.java
│       │               │   └── Settings.java
│       │               ├── services/
│       │               │   ├── CameraService.java
│       │               │   ├── AttentionMonitorService.java
│       │               │   ├── DatabaseService.java
│       │               │   └── NotificationService.java
│       │               └── utils/
│       │                   ├── NavigationManager.java
│       │                   └── Constants.java
│       └── resources/
│           ├── fxml/
│           │   ├── splash.fxml
│           │   ├── onboarding.fxml
│           │   ├── permission.fxml
│           │   ├── calibration.fxml
│           │   ├── main-monitoring.fxml
│           │   ├── settings.fxml
│           │   ├── new-session.fxml
│           │   ├── break.fxml
│           │   └── stats.fxml
│           ├── css/
│           │   └── styles.css
│           └── images/
│               └── icon.png
├── pom.xml
└── README.md
```

## Build & Run

### Build the project
```bash
cd JavaApp
mvn clean package
```

### Run the application
```bash
mvn javafx:run
```

### Create executable JAR
```bash
mvn clean package
java -jar target/study-focus-tracker-1.0.0.jar
```

## Backend Integration

The Java app communicates with the Python state detection backend:

1. Start the Python backend server:
```bash
cd ../StateDetectionLogic/driver_state_detection
python server.py
```

2. The Java app connects to `http://localhost:5000` for attention monitoring

## Database Schema

SQLite database (`study_tracker.db`) with tables:
- `sessions` - Study session records
- `attention_data` - Real-time attention scores
- `settings` - User preferences

## Configuration

Edit `src/main/resources/application.properties`:
```properties
backend.url=http://localhost:5000
camera.index=0
session.break.interval=25
session.break.duration=5
```

## License

See LICENSE file in the root directory.
