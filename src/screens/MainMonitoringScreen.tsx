import React, { useState, useEffect } from 'react';
import {
  View,
  Text,
  TouchableOpacity,
  Animated,
  Dimensions,
} from 'react-native';
import { StackNavigationProp } from '@react-navigation/stack';
import { RootStackParamList } from '../../App';

const { width } = Dimensions.get('window');

type MainMonitoringScreenNavigationProp = StackNavigationProp<
  RootStackParamList,
  'MainMonitoring'
>;

interface Props {
  navigation: MainMonitoringScreenNavigationProp;
}

export default function MainMonitoringScreen({ navigation }: Props) {
  const [isMonitoring, setIsMonitoring] = useState(false);
  const [sessionTime, setSessionTime] = useState(0);
  const [focusScore, setFocusScore] = useState(95);
  const pulseAnim = new Animated.Value(1);

  useEffect(() => {
    let interval: NodeJS.Timeout;
    if (isMonitoring) {
      interval = setInterval(() => {
        setSessionTime((prev) => prev + 1);
        // Simulate focus score fluctuation
        setFocusScore((prev) => Math.max(70, Math.min(100, prev + (Math.random() - 0.5) * 5)));
      }, 1000);

      Animated.loop(
        Animated.sequence([
          Animated.timing(pulseAnim, {
            toValue: 1.05,
            duration: 1500,
            useNativeDriver: true,
          }),
          Animated.timing(pulseAnim, {
            toValue: 1,
            duration: 1500,
            useNativeDriver: true,
          }),
        ])
      ).start();
    }
    return () => clearInterval(interval);
  }, [isMonitoring]);

  const formatTime = (seconds: number) => {
    const hrs = Math.floor(seconds / 3600);
    const mins = Math.floor((seconds % 3600) / 60);
    const secs = seconds % 60;
    return `${hrs.toString().padStart(2, '0')}:${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
  };

  const toggleMonitoring = () => {
    if (!isMonitoring) {
      navigation.navigate('NewSession');
    } else {
      setIsMonitoring(false);
    }
  };

  return (
    <View style={{ flex: 1, backgroundColor: '#0a0a0a' }}>
      {/* Header */}
      <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', paddingHorizontal: 24, paddingTop: 60, paddingBottom: 20 }}>
        <Text style={{ fontSize: 24, fontWeight: 'bold', color: '#ffffff' }}>Lock In Twin</Text>
        <View style={{ flexDirection: 'row', gap: 12 }}>
          <TouchableOpacity
            style={{ width: 44, height: 44, borderRadius: 22, backgroundColor: '#18181b', justifyContent: 'center', alignItems: 'center', borderWidth: 1, borderColor: '#27272a' }}
            onPress={() => navigation.navigate('Stats')}
          >
            <Text style={{ fontSize: 20 }}>📊</Text>
          </TouchableOpacity>
          <TouchableOpacity
            style={{ width: 44, height: 44, borderRadius: 22, backgroundColor: '#18181b', justifyContent: 'center', alignItems: 'center', borderWidth: 1, borderColor: '#27272a' }}
            onPress={() => navigation.navigate('Settings')}
          >
            <Text style={{ fontSize: 20 }}>⚙️</Text>
          </TouchableOpacity>
        </View>
      </View>

      {/* Main Content */}
      <View style={{ flex: 1, paddingHorizontal: 24, alignItems: 'center', justifyContent: 'center' }}>
        {/* Focus Circle */}
        <Animated.View
          style={{
            width: width * 0.6,
            height: width * 0.6,
            borderRadius: width * 0.3,
            backgroundColor: 'rgba(20, 184, 166, 0.1)',
            justifyContent: 'center',
            alignItems: 'center',
            borderWidth: 4,
            borderColor: '#14b8a6',
            marginBottom: 40,
            transform: isMonitoring ? [{ scale: pulseAnim }] : undefined,
          }}
        >
          <View style={{ alignItems: 'center' }}>
            <Text style={{ fontSize: 64, fontWeight: 'bold', color: '#14b8a6' }}>{Math.round(focusScore)}%</Text>
            <Text style={{ fontSize: 16, color: '#94a3b8', marginTop: 8 }}>Focus Score</Text>
          </View>
        </Animated.View>

        {/* Session Time */}
        <View style={{ alignItems: 'center', marginBottom: 40 }}>
          <Text style={{ fontSize: 14, color: '#94a3b8', marginBottom: 8 }}>Session Time</Text>
          <Text style={{ fontSize: 32, fontWeight: 'bold', color: '#ffffff', fontVariant: ['tabular-nums'] }}>{formatTime(sessionTime)}</Text>
        </View>

        {/* Stats Grid */}
        <View style={{ flexDirection: 'row', gap: 16, marginBottom: 30 }}>
          <View style={{ flex: 1, backgroundColor: '#18181b', padding: 20, borderRadius: 16, alignItems: 'center', borderWidth: 1, borderColor: '#27272a' }}>
            <Text style={{ fontSize: 28, fontWeight: 'bold', color: '#ffffff', marginBottom: 8 }}>0</Text>
            <Text style={{ fontSize: 12, color: '#94a3b8', textAlign: 'center' }}>Posture Alerts</Text>
          </View>
          <View style={{ flex: 1, backgroundColor: '#18181b', padding: 20, borderRadius: 16, alignItems: 'center', borderWidth: 1, borderColor: '#27272a' }}>
            <Text style={{ fontSize: 28, fontWeight: 'bold', color: '#ffffff', marginBottom: 8 }}>0</Text>
            <Text style={{ fontSize: 12, color: '#94a3b8', textAlign: 'center' }}>Breaks Taken</Text>
          </View>
        </View>

        {/* Status Indicator */}
        <View style={{ flexDirection: 'row', alignItems: 'center', gap: 8 }}>
          <View style={{ width: 12, height: 12, borderRadius: 6, backgroundColor: isMonitoring ? '#22c55e' : '#71717a' }} />
          <Text style={{ fontSize: 14, color: '#94a3b8' }}>
            {isMonitoring ? 'Monitoring Active' : 'Ready to Start'}
          </Text>
        </View>
      </View>

      {/* Footer Controls */}
      <View style={{ paddingHorizontal: 24, paddingBottom: 40, gap: 12 }}>
        {isMonitoring && (
          <TouchableOpacity
            style={{ backgroundColor: '#18181b', paddingVertical: 16, borderRadius: 12, alignItems: 'center', borderWidth: 1, borderColor: '#27272a' }}
            onPress={() => navigation.navigate('Break')}
          >
            <Text style={{ color: '#ffffff', fontSize: 16, fontWeight: '600' }}>Take a Break</Text>
          </TouchableOpacity>
        )}
        <TouchableOpacity
          style={{ backgroundColor: isMonitoring ? '#ef4444' : '#14b8a6', paddingVertical: 18, borderRadius: 12, alignItems: 'center' }}
          onPress={toggleMonitoring}
        >
          <Text style={{ color: '#ffffff', fontSize: 18, fontWeight: '600' }}>
            {isMonitoring ? 'End Session' : 'Start Session'}
          </Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}

