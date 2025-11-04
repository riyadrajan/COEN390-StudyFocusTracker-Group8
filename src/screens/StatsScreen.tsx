import React from 'react';
import { View, Text, TouchableOpacity, ScrollView } from 'react-native';
import { StackNavigationProp } from '@react-navigation/stack';
import { RootStackParamList } from '../../App';

type StatsScreenNavigationProp = StackNavigationProp<RootStackParamList, 'Stats'>;

interface Props {
  navigation: StatsScreenNavigationProp;
}

export default function StatsScreen({ navigation }: Props) {
  return (
    <View style={{ flex: 1, backgroundColor: '#0a0a0a' }}>
      <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', paddingHorizontal: 24, paddingTop: 60, paddingBottom: 20 }}>
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <Text style={{ fontSize: 32, color: '#ffffff' }}>←</Text>
        </TouchableOpacity>
        <Text style={{ fontSize: 20, fontWeight: 'bold', color: '#ffffff' }}>Statistics</Text>
        <View style={{ width: 40 }} />
      </View>

      <ScrollView style={{ flex: 1, paddingHorizontal: 24 }}>
        <View style={{ marginBottom: 32 }}>
          <Text style={{ fontSize: 16, fontWeight: '600', color: '#94a3b8', marginBottom: 12, textTransform: 'uppercase' }}>Today</Text>
          <View style={{ flexDirection: 'row', gap: 12 }}>
            <View style={{ flex: 1, backgroundColor: '#18181b', padding: 20, borderRadius: 16, borderWidth: 1, borderColor: '#27272a' }}>
              <Text style={{ fontSize: 32, fontWeight: 'bold', color: '#14b8a6', marginBottom: 8 }}>2.5h</Text>
              <Text style={{ fontSize: 14, color: '#94a3b8' }}>Focus Time</Text>
            </View>
            <View style={{ flex: 1, backgroundColor: '#18181b', padding: 20, borderRadius: 16, borderWidth: 1, borderColor: '#27272a' }}>
              <Text style={{ fontSize: 32, fontWeight: 'bold', color: '#8b5cf6', marginBottom: 8 }}>92%</Text>
              <Text style={{ fontSize: 14, color: '#94a3b8' }}>Avg Score</Text>
            </View>
          </View>
        </View>

        <View style={{ marginBottom: 32 }}>
          <Text style={{ fontSize: 16, fontWeight: '600', color: '#94a3b8', marginBottom: 12, textTransform: 'uppercase' }}>This Week</Text>
          <View style={{ backgroundColor: '#18181b', padding: 20, borderRadius: 16, borderWidth: 1, borderColor: '#27272a', marginBottom: 12 }}>
            <View style={{ flexDirection: 'row', justifyContent: 'space-between', marginBottom: 8 }}>
              <Text style={{ fontSize: 16, color: '#ffffff' }}>Total Sessions</Text>
              <Text style={{ fontSize: 16, fontWeight: 'bold', color: '#14b8a6' }}>12</Text>
            </View>
            <View style={{ flexDirection: 'row', justifyContent: 'space-between', marginBottom: 8 }}>
              <Text style={{ fontSize: 16, color: '#ffffff' }}>Total Time</Text>
              <Text style={{ fontSize: 16, fontWeight: 'bold', color: '#14b8a6' }}>8.5h</Text>
            </View>
            <View style={{ flexDirection: 'row', justifyContent: 'space-between' }}>
              <Text style={{ fontSize: 16, color: '#ffffff' }}>Posture Alerts</Text>
              <Text style={{ fontSize: 16, fontWeight: 'bold', color: '#f59e0b' }}>3</Text>
            </View>
          </View>
        </View>

        <View style={{ marginBottom: 32 }}>
          <Text style={{ fontSize: 16, fontWeight: '600', color: '#94a3b8', marginBottom: 12, textTransform: 'uppercase' }}>Achievements</Text>
          <View style={{ backgroundColor: '#18181b', padding: 20, borderRadius: 16, borderWidth: 1, borderColor: '#27272a', marginBottom: 12 }}>
            <Text style={{ fontSize: 24, marginBottom: 8 }}>🔥</Text>
            <Text style={{ fontSize: 16, fontWeight: '600', color: '#ffffff', marginBottom: 4 }}>7 Day Streak</Text>
            <Text style={{ fontSize: 14, color: '#94a3b8' }}>Keep it going!</Text>
          </View>
          <View style={{ backgroundColor: '#18181b', padding: 20, borderRadius: 16, borderWidth: 1, borderColor: '#27272a' }}>
            <Text style={{ fontSize: 24, marginBottom: 8 }}>⭐</Text>
            <Text style={{ fontSize: 16, fontWeight: '600', color: '#ffffff', marginBottom: 4 }}>Focus Master</Text>
            <Text style={{ fontSize: 14, color: '#94a3b8' }}>Maintained 90%+ score</Text>
          </View>
        </View>
      </ScrollView>
    </View>
  );
}
