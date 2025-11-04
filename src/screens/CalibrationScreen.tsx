import React, { useState, useEffect } from 'react';
import { View, Text, TouchableOpacity, Animated } from 'react-native';
import { StackNavigationProp } from '@react-navigation/stack';
import { RootStackParamList } from '../../App';

type CalibrationScreenNavigationProp = StackNavigationProp<RootStackParamList, 'Calibration'>;

interface Props {
  navigation: CalibrationScreenNavigationProp;
}

export default function CalibrationScreen({ navigation }: Props) {
  const [step, setStep] = useState(1);
  const [calibrating, setCalibrating] = useState(false);
  const pulseAnim = new Animated.Value(1);

  useEffect(() => {
    if (calibrating) {
      Animated.loop(
        Animated.sequence([
          Animated.timing(pulseAnim, {
            toValue: 1.1,
            duration: 800,
            useNativeDriver: true,
          }),
          Animated.timing(pulseAnim, {
            toValue: 1,
            duration: 800,
            useNativeDriver: true,
          }),
        ])
      ).start();
    }
  }, [calibrating]);

  const startCalibration = () => {
    setCalibrating(true);
    setTimeout(() => {
      setStep(2);
      setCalibrating(false);
    }, 3000);
  };

  const completeCalibration = () => {
    navigation.replace('MainMonitoring');
  };

  return (
    <View style={{ flex: 1, backgroundColor: '#0a0a0a' }}>
      <View style={{ flex: 1, paddingHorizontal: 24, paddingTop: 80, alignItems: 'center' }}>
        <Text style={{ fontSize: 32, fontWeight: 'bold', color: '#ffffff', marginBottom: 12 }}>Posture Calibration</Text>
        <Text style={{ fontSize: 16, color: '#94a3b8', textAlign: 'center', marginBottom: 60, lineHeight: 24 }}>
          {step === 1
            ? 'Position yourself comfortably in your ideal posture'
            : 'Calibration complete! Your baseline has been set.'}
        </Text>

        <Animated.View
          style={{
            width: 200,
            height: 200,
            borderRadius: 100,
            backgroundColor: 'rgba(20, 184, 166, 0.1)',
            justifyContent: 'center',
            alignItems: 'center',
            marginBottom: 60,
            borderWidth: 3,
            borderColor: '#14b8a6',
            transform: calibrating ? [{ scale: pulseAnim }] : [{ scale: 1 }],
          }}
        >
          <View style={{ width: 160, height: 160, borderRadius: 80, backgroundColor: 'rgba(20, 184, 166, 0.2)', justifyContent: 'center', alignItems: 'center' }}>
            <Text style={{ fontSize: 80 }}>
              {step === 1 ? '🎯' : '✓'}
            </Text>
          </View>
        </Animated.View>

        {step === 1 && (
          <View style={{ width: '100%' }}>
            <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 20, backgroundColor: '#18181b', padding: 16, borderRadius: 12, borderWidth: 1, borderColor: '#27272a' }}>
              <Text style={{ width: 32, height: 32, borderRadius: 16, backgroundColor: '#14b8a6', color: '#ffffff', fontSize: 16, fontWeight: 'bold', textAlign: 'center', lineHeight: 32, marginRight: 16 }}>1</Text>
              <Text style={{ flex: 1, fontSize: 16, color: '#ffffff' }}>
                Sit upright with your back straight
              </Text>
            </View>
            <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 20, backgroundColor: '#18181b', padding: 16, borderRadius: 12, borderWidth: 1, borderColor: '#27272a' }}>
              <Text style={{ width: 32, height: 32, borderRadius: 16, backgroundColor: '#14b8a6', color: '#ffffff', fontSize: 16, fontWeight: 'bold', textAlign: 'center', lineHeight: 32, marginRight: 16 }}>2</Text>
              <Text style={{ flex: 1, fontSize: 16, color: '#ffffff' }}>
                Keep your device at eye level
              </Text>
            </View>
            <View style={{ flexDirection: 'row', alignItems: 'center', marginBottom: 20, backgroundColor: '#18181b', padding: 16, borderRadius: 12, borderWidth: 1, borderColor: '#27272a' }}>
              <Text style={{ width: 32, height: 32, borderRadius: 16, backgroundColor: '#14b8a6', color: '#ffffff', fontSize: 16, fontWeight: 'bold', textAlign: 'center', lineHeight: 32, marginRight: 16 }}>3</Text>
              <Text style={{ flex: 1, fontSize: 16, color: '#ffffff' }}>
                Relax your shoulders
              </Text>
            </View>
          </View>
        )}

        {step === 2 && (
          <View style={{ backgroundColor: '#18181b', padding: 24, borderRadius: 16, borderWidth: 1, borderColor: '#22c55e' }}>
            <Text style={{ fontSize: 16, color: '#94a3b8', textAlign: 'center', lineHeight: 24 }}>
              Your optimal posture has been recorded. You'll receive alerts when you deviate from this position.
            </Text>
          </View>
        )}
      </View>

      <View style={{ paddingHorizontal: 24, paddingBottom: 40 }}>
        {step === 1 ? (
          <TouchableOpacity
            style={{ backgroundColor: calibrating ? '#27272a' : '#14b8a6', paddingVertical: 16, borderRadius: 12, alignItems: 'center' }}
            onPress={startCalibration}
            disabled={calibrating}
          >
            <Text style={{ color: '#ffffff', fontSize: 18, fontWeight: '600' }}>
              {calibrating ? 'Calibrating...' : 'Start Calibration'}
            </Text>
          </TouchableOpacity>
        ) : (
          <TouchableOpacity style={{ backgroundColor: '#14b8a6', paddingVertical: 16, borderRadius: 12, alignItems: 'center' }} onPress={completeCalibration}>
            <Text style={{ color: '#ffffff', fontSize: 18, fontWeight: '600' }}>Continue</Text>
          </TouchableOpacity>
        )}
      </View>
    </View>
  );
}

