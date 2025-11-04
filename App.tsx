import 'react-native-gesture-handler';
import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import { StatusBar } from 'expo-status-bar';
import { SafeAreaProvider } from 'react-native-safe-area-context';

// Screens
import SplashScreen from './src/screens/SplashScreen';
import OnboardingScreen from './src/screens/OnboardingScreen';
import PermissionScreen from './src/screens/PermissionScreen';
import CalibrationScreen from './src/screens/CalibrationScreen';
import MainMonitoringScreen from './src/screens/MainMonitoringScreen';
import SettingsScreen from './src/screens/SettingsScreen';
import NewSessionScreen from './src/screens/NewSessionScreen';
import BreakScreen from './src/screens/BreakScreen';
import StatsScreen from './src/screens/StatsScreen';

export type RootStackParamList = {
  Splash: undefined;
  Onboarding: undefined;
  Permission: undefined;
  Calibration: undefined;
  MainMonitoring: undefined;
  Settings: undefined;
  NewSession: undefined;
  Break: undefined;
  Stats: undefined;
};

const Stack = createStackNavigator<RootStackParamList>();

export default function App() {
  return (
    <SafeAreaProvider>
      <StatusBar style="light" />
      <NavigationContainer>
        <Stack.Navigator
          initialRouteName="Splash"
          screenOptions={{
            headerShown: false,
            cardStyle: { backgroundColor: '#0a0a0a' },
            animationEnabled: true,
          }}
        >
          <Stack.Screen name="Splash" component={SplashScreen} />
          <Stack.Screen name="Onboarding" component={OnboardingScreen} />
          <Stack.Screen name="Permission" component={PermissionScreen} />
          <Stack.Screen name="Calibration" component={CalibrationScreen} />
          <Stack.Screen name="MainMonitoring" component={MainMonitoringScreen} />
          <Stack.Screen name="Settings" component={SettingsScreen} />
          <Stack.Screen name="NewSession" component={NewSessionScreen} />
          <Stack.Screen name="Break" component={BreakScreen} />
          <Stack.Screen name="Stats" component={StatsScreen} />
        </Stack.Navigator>
      </NavigationContainer>
    </SafeAreaProvider>
  );
}
