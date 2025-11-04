import React, { useState } from 'react';
import { View, Text, TouchableOpacity, ScrollView } from 'react-native';
import { StackNavigationProp } from '@react-navigation/stack';
import { RootStackParamList } from '../../App';

type NewSessionScreenNavigationProp = StackNavigationProp<RootStackParamList, 'NewSession'>;

interface Props {
  navigation: NewSessionScreenNavigationProp;
}

export default function NewSessionScreen({ navigation }: Props) {
  const [duration, setDuration] = useState(25);

  const startSession = () => {
    navigation.navigate('MainMonitoring');
  };

  return (
    <View style={{ flex: 1, backgroundColor: '#0a0a0a' }}>
      <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', paddingHorizontal: 24, paddingTop: 60, paddingBottom: 20 }}>
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <Text style={{ fontSize: 32, color: '#ffffff' }}>←</Text>
        </TouchableOpacity>
        <Text style={{ fontSize: 20, fontWeight: 'bold', color: '#ffffff' }}>New Session</Text>
        <View style={{ width: 40 }} />
      </View>

      <ScrollView style={{ flex: 1, paddingHorizontal: 24 }}>
        <View style={{ marginBottom: 32 }}>
          <Text style={{ fontSize: 16, fontWeight: '600', color: '#94a3b8', marginBottom: 12 }}>Duration</Text>
          <View style={{ flexDirection: 'row', gap: 12 }}>
            {[15, 25, 45, 60].map((min) => (
              <TouchableOpacity
                key={min}
                onPress={() => setDuration(min)}
                style={{ flex: 1, backgroundColor: duration === min ? '#14b8a6' : '#18181b', paddingVertical: 16, borderRadius: 12, alignItems: 'center', borderWidth: 1, borderColor: duration === min ? '#14b8a6' : '#27272a' }}
              >
                <Text style={{ color: '#ffffff', fontSize: 18, fontWeight: '600' }}>{min}m</Text>
              </TouchableOpacity>
            ))}
          </View>
        </View>

        <View style={{ marginBottom: 32 }}>
          <Text style={{ fontSize: 16, fontWeight: '600', color: '#94a3b8', marginBottom: 12 }}>Features</Text>
          <View style={{ backgroundColor: '#18181b', borderRadius: 12, padding: 16, marginBottom: 8, borderWidth: 1, borderColor: '#27272a' }}>
            <Text style={{ fontSize: 16, fontWeight: '600', color: '#ffffff' }}>✓ Posture Monitoring</Text>
          </View>
          <View style={{ backgroundColor: '#18181b', borderRadius: 12, padding: 16, marginBottom: 8, borderWidth: 1, borderColor: '#27272a' }}>
            <Text style={{ fontSize: 16, fontWeight: '600', color: '#ffffff' }}>✓ Focus Tracking</Text>
          </View>
          <View style={{ backgroundColor: '#18181b', borderRadius: 12, padding: 16, marginBottom: 8, borderWidth: 1, borderColor: '#27272a' }}>
            <Text style={{ fontSize: 16, fontWeight: '600', color: '#ffffff' }}>✓ Break Reminders</Text>
          </View>
        </View>
      </ScrollView>

      <View style={{ paddingHorizontal: 24, paddingBottom: 40 }}>
        <TouchableOpacity style={{ backgroundColor: '#14b8a6', paddingVertical: 18, borderRadius: 12, alignItems: 'center' }} onPress={startSession}>
          <Text style={{ color: '#ffffff', fontSize: 18, fontWeight: '600' }}>Start {duration} Minute Session</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}
