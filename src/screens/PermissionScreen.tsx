import React, { useState } from 'react';
import { View, Text, TouchableOpacity, Alert } from 'react-native';
import { StackNavigationProp } from '@react-navigation/stack';
import { RootStackParamList } from '../../App';
import { Camera } from 'expo-camera';
import * as Notifications from 'expo-notifications';

type PermissionScreenNavigationProp = StackNavigationProp<RootStackParamList, 'Permission'>;

interface Props {
  navigation: PermissionScreenNavigationProp;
}

export default function PermissionScreen({ navigation }: Props) {
  const [cameraGranted, setCameraGranted] = useState(false);
  const [notificationGranted, setNotificationGranted] = useState(false);

  const requestCameraPermission = async () => {
    try {
      const { status } = await Camera.requestCameraPermissionsAsync();
      setCameraGranted(status === 'granted');
      if (status !== 'granted') {
        Alert.alert(
          'Permission Required',
          'Camera access is needed for posture monitoring.',
          [{ text: 'OK' }]
        );
      }
    } catch (error) {
      console.error('Camera permission error:', error);
    }
  };

  const requestNotificationPermission = async () => {
    try {
      const { status } = await Notifications.requestPermissionsAsync();
      setNotificationGranted(status === 'granted');
      if (status !== 'granted') {
        Alert.alert(
          'Permission Required',
          'Notification access is needed for break reminders.',
          [{ text: 'OK' }]
        );
      }
    } catch (error) {
      console.error('Notification permission error:', error);
    }
  };

  const handleContinue = () => {
    if (cameraGranted && notificationGranted) {
      navigation.replace('Calibration');
    } else {
      Alert.alert(
        'Permissions Required',
        'Please grant all permissions to continue.',
        [{ text: 'OK' }]
      );
    }
  };

  return (
    <View style={{ flex: 1, backgroundColor: '#0a0a0a' }}>
      <View style={{ flex: 1, paddingHorizontal: 24, paddingTop: 80 }}>
        <Text style={{ fontSize: 32, fontWeight: 'bold', color: '#ffffff', marginBottom: 12 }}>Permissions Required</Text>
        <Text style={{ fontSize: 16, color: '#94a3b8', marginBottom: 40, lineHeight: 24 }}>
          Lock In Twin needs these permissions to provide the best experience
        </Text>

        <View style={{ backgroundColor: '#18181b', borderRadius: 16, padding: 20, marginBottom: 16, borderWidth: 1, borderColor: '#27272a' }}>
          <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 16 }}>
            <Text style={{ fontSize: 40, marginRight: 16 }}>📷</Text>
            <View style={{ flex: 1 }}>
              <Text style={{ fontSize: 18, fontWeight: '600', color: '#ffffff', marginBottom: 4 }}>Camera Access</Text>
              <Text style={{ fontSize: 14, color: '#94a3b8' }}>
                For posture and focus monitoring
              </Text>
            </View>
          </View>
          <TouchableOpacity
            style={{ backgroundColor: cameraGranted ? '#22c55e' : '#14b8a6', paddingVertical: 12, borderRadius: 8, alignItems: 'center' }}
            onPress={requestCameraPermission}
            disabled={cameraGranted}
          >
            <Text style={{ color: '#ffffff', fontSize: 16, fontWeight: '600' }}>
              {cameraGranted ? 'Granted ✓' : 'Allow'}
            </Text>
          </TouchableOpacity>
        </View>

        <View style={{ backgroundColor: '#18181b', borderRadius: 16, padding: 20, marginBottom: 16, borderWidth: 1, borderColor: '#27272a' }}>
          <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 16 }}>
            <Text style={{ fontSize: 40, marginRight: 16 }}>🔔</Text>
            <View style={{ flex: 1 }}>
              <Text style={{ fontSize: 18, fontWeight: '600', color: '#ffffff', marginBottom: 4 }}>Notifications</Text>
              <Text style={{ fontSize: 14, color: '#94a3b8' }}>
                For break reminders and alerts
              </Text>
            </View>
          </View>
          <TouchableOpacity
            style={{ backgroundColor: notificationGranted ? '#22c55e' : '#14b8a6', paddingVertical: 12, borderRadius: 8, alignItems: 'center' }}
            onPress={requestNotificationPermission}
            disabled={notificationGranted}
          >
            <Text style={{ color: '#ffffff', fontSize: 16, fontWeight: '600' }}>
              {notificationGranted ? 'Granted ✓' : 'Allow'}
            </Text>
          </TouchableOpacity>
        </View>
      </View>

      <View style={{ paddingHorizontal: 24, paddingBottom: 40 }}>
        <TouchableOpacity
          style={{ backgroundColor: (cameraGranted && notificationGranted) ? '#14b8a6' : '#27272a', paddingVertical: 16, borderRadius: 12, alignItems: 'center' }}
          onPress={handleContinue}
          disabled={!(cameraGranted && notificationGranted)}
        >
          <Text style={{ color: '#ffffff', fontSize: 18, fontWeight: '600' }}>Continue</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}

