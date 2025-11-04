import 'react-native-gesture-handler';
import React from 'react';
import { View, Text, StatusBar } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';

export default function App() {
  return (
    <>
      <StatusBar barStyle="light-content" backgroundColor="#0a0a0a" />
      <SafeAreaView style={{ flex: 1, backgroundColor: '#0a0a0a' }}>
        <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
          <View style={{ 
            width: 120, 
            height: 120, 
            borderRadius: 60, 
            backgroundColor: 'rgba(20, 184, 166, 0.2)', 
            justifyContent: 'center', 
            alignItems: 'center',
            borderWidth: 3,
            borderColor: '#14b8a6',
            marginBottom: 24
          }}>
            <Text style={{ fontSize: 60 }}>🔒</Text>
          </View>
          <Text style={{ color: '#14b8a6', fontSize: 32, fontWeight: 'bold' }}>Lock In Twin</Text>
          <Text style={{ color: '#ffffff', fontSize: 16, marginTop: 8 }}>Focus. Monitor. Achieve.</Text>
        </View>
      </SafeAreaView>
    </>
  );
}
