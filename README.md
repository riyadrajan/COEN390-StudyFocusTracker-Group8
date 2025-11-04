<<<<<<< HEAD
# Instructions  
## Android App configurations
- Android manifest.xml : include the uses permissions, and networkSecurityConfig
```xml
    <!--In manifest.xml -->
    <!-- Needed for network calls -->
    <uses-permission android:name="android.permission.INTERNET" />
...
    <application
    ...
        android:networkSecurityConfig="@xml/network_security_config">
    ...
     </application>
```
- In networkSecurityConfig.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <base-config cleartextTrafficPermitted="true" />
</network-security-config>
```
- MainActivity.java
  - Use http://10.0.2.2:3000 as the base URL when using the android emulator 
  - Use local IP address if using the physical android phone  
Quick way to find local IP address on mac/linux:
```bash
ipconfig getifaddr en0
```

## Running Flask in Python
- Navigate to the StateDetectionLogic Folder  
- In terminal, run the following commands
```bash
python3 -m venv .venv
```
```bash
. .venv/bin/activate
```
```bash
pip install Flask
```
```bash
PORT=3000 .venv/bin/python -m driver_state_detection.server
```

## Run server from StateDetectorLogic dir
```bash
PORT=3000 driver_state_detection/.venv/bin/python -m driver_state_detection.server
```

## Run logic detection 
```bash
go into driver_state_detection dir
```
```bash
python3 -m venv .venv
```
```bash
source .venv/bin/activate
```
* pip install flask opencv-python mediapipe numpy (If not installed)
```bash
python3 main.py
```


## Clean port  
- Check if the port is being used:
```bash
sudo lsof -iTCP -sTCP:LISTEN -P -n
```  
- Kill process using its pid  
```bash
  kill -9 <pid>
```
# References
This project makes use of Driver State Detection (https://github.com/e-candeloro/Driver-State-Detection).
=======
# Lock In Twin - Focus & Posture Monitoring App

A production-ready React Native Android app for monitoring focus and posture during study/work sessions.

## Features

- **Splash Screen**: Animated welcome screen with gradient background
- **Onboarding**: 3-screen onboarding flow with smooth transitions
- **Permission Management**: Camera and notification permission requests
- **Posture Calibration**: Set your optimal posture baseline
- **Main Monitoring**: Real-time focus tracking with circular progress indicator
- **Session Management**: Customizable session duration and break intervals
- **Smart Breaks**: Guided break screens with exercises
- **Statistics**: Detailed analytics with charts and achievements
- **Settings**: Comprehensive settings for customization

## Tech Stack

- **Framework**: React Native (Expo)
- **Navigation**: React Navigation (Stack Navigator)
- **State Management**: Zustand (ready for backend integration)
- **Sensors**: Expo Camera, Expo Sensors
- **Notifications**: Expo Notifications
- **Storage**: AsyncStorage
- **UI**: Custom components with Animated API

## Installation

```bash
# Install dependencies
npm install

# Start development server
npm start

# Run on Android
npm run android

# Run on iOS
npm run ios
```

## Project Structure

```
lock-in-twin-app/
├── App.tsx                 # Main app with navigation
├── src/
│   ├── screens/           # All screen components
│   │   ├── SplashScreen.tsx
│   │   ├── OnboardingScreen.tsx
│   │   ├── PermissionScreen.tsx
│   │   ├── CalibrationScreen.tsx
│   │   ├── MainMonitoringScreen.tsx
│   │   ├── NewSessionScreen.tsx
│   │   ├── BreakScreen.tsx
│   │   ├── StatsScreen.tsx
│   │   └── SettingsScreen.tsx
│   ├── components/        # Reusable components (ready for expansion)
│   ├── store/            # State management (ready for backend)
│   ├── utils/            # Utility functions
│   ├── types/            # TypeScript types
│   └── constants/        # App constants
├── assets/               # Images, fonts, icons
├── app.json             # Expo configuration
└── package.json         # Dependencies

```

## Backend Integration Ready

The app is structured to easily integrate with a backend API:

1. **State Management**: Zustand store ready in `/src/store`
2. **API Layer**: Create services in `/src/services`
3. **Types**: TypeScript interfaces in `/src/types`
4. **AsyncStorage**: Local persistence already configured

### Suggested Backend Endpoints

```
POST /api/sessions/start
POST /api/sessions/end
GET  /api/sessions/history
POST /api/calibration
GET  /api/stats
POST /api/alerts
```

## Permissions

### Android
- **CAMERA**: For posture monitoring
- **VIBRATE**: For haptic feedback on alerts
- **RECEIVE_BOOT_COMPLETED**: For persistent reminders
- **SCHEDULE_EXACT_ALARM**: For precise break timing

### iOS
- **Camera**: Posture monitoring
- **Motion**: Device orientation detection

## Building for Production

### Android APK
```bash
# Build APK
eas build --platform android --profile preview

# Build AAB for Play Store
eas build --platform android --profile production
```

### iOS
```bash
# Build for TestFlight
eas build --platform ios --profile preview

# Build for App Store
eas build --platform ios --profile production
```

## Environment Variables

Create a `.env` file:

```env
API_BASE_URL=https://your-backend-api.com
SENTRY_DSN=your-sentry-dsn
ANALYTICS_KEY=your-analytics-key
```

## Features Ready for Backend

1. **Session Tracking**: Start/stop sessions with duration and focus scores
2. **Posture Monitoring**: Camera-based posture detection (ML model integration ready)
3. **Break Management**: Scheduled breaks with customizable intervals
4. **Statistics**: Historical data visualization
5. **Achievements**: Gamification system
6. **User Profiles**: Account management ready

## Design System

### Colors
- **Primary**: `#14b8a6` (Teal)
- **Success**: `#22c55e` (Green)
- **Warning**: `#f59e0b` (Amber)
- **Error**: `#ef4444` (Red)
- **Background**: `#0a0a0a` (Dark)
- **Surface**: `#18181b` (Dark Gray)
- **Border**: `#27272a` (Gray)
- **Text Primary**: `#ffffff` (White)
- **Text Secondary**: `#94a3b8` (Light Gray)

### Typography
- **Title**: 32px, Bold
- **Heading**: 24px, Bold
- **Subheading**: 20px, Semi-bold
- **Body**: 16px, Regular
- **Caption**: 14px, Regular
- **Small**: 12px, Regular

## Next Steps

1. **Install Dependencies**: Run `npm install`
2. **Test on Device**: Use Expo Go or build development client
3. **Integrate Backend**: Add API calls to screens
4. **Add ML Model**: Integrate posture detection model
5. **Testing**: Add unit and integration tests
6. **CI/CD**: Set up GitHub Actions for automated builds

## License

Proprietary - All rights reserved

## Support

For issues or questions, contact: support@lockintwin.app

---

**Status**: ✅ Production-ready frontend, awaiting backend integration
>>>>>>> ab9f17e (Add Lock In Twin app - Complete React Native implementation with all screens)
