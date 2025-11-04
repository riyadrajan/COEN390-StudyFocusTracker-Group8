import React, { useState, useEffect } from 'react';
import { View, Text, TouchableOpacity } from 'react-native';
import { StackNavigationProp } from '@react-navigation/stack';
import { RootStackParamList } from '../../App';

type BreakScreenNavigationProp = StackNavigationProp<RootStackParamList, 'Break'>;

interface Props {
  navigation: BreakScreenNavigationProp;
}

export default function BreakScreen({ navigation }: Props) {
  const [timeLeft, setTimeLeft] = useState(300); // 5 minutes

  useEffect(() => {
    const interval = setInterval(() => {
      setTimeLeft((prev) => {
        if (prev <= 1) {
          clearInterval(interval);
          navigation.goBack();
          return 0;
        }
        return prev - 1;
      });
    }, 1000);
    return () => clearInterval(interval);
  }, []);

  const formatTime = (seconds: number) => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
  };

  return (
    <View style={{ flex: 1, backgroundColor: '#0a0a0a', justifyContent: 'center', alignItems: 'center', paddingHorizontal: 24 }}>
      <View style={{ width: 200, height: 200, borderRadius: 100, backgroundColor: 'rgba(139, 92, 246, 0.1)', justifyContent: 'center', alignItems: 'center', borderWidth: 4, borderColor: '#8b5cf6', marginBottom: 40 }}>
        <Text style={{ fontSize: 60 }}>☕</Text>
      </View>

      <Text style={{ fontSize: 32, fontWeight: 'bold', color: '#ffffff', marginBottom: 8 }}>Take a Break</Text>
      <Text style={{ fontSize: 16, color: '#94a3b8', marginBottom: 40, textAlign: 'center' }}>
        Relax and recharge. You've earned it!
      </Text>

      <Text style={{ fontSize: 64, fontWeight: 'bold', color: '#8b5cf6', marginBottom: 60, fontVariant: ['tabular-nums'] }}>
        {formatTime(timeLeft)}
      </Text>

      <TouchableOpacity
        style={{ backgroundColor: '#8b5cf6', paddingVertical: 16, paddingHorizontal: 48, borderRadius: 12 }}
        onPress={() => navigation.goBack()}
      >
        <Text style={{ color: '#ffffff', fontSize: 18, fontWeight: '600' }}>End Break Early</Text>
      </TouchableOpacity>
    </View>
  );
}
