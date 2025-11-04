import React, { useEffect } from 'react';
import { View, Text, Animated } from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import { StackNavigationProp } from '@react-navigation/stack';
import { RootStackParamList } from '../../App';

type SplashScreenNavigationProp = StackNavigationProp<RootStackParamList, 'Splash'>;

interface Props {
  navigation: SplashScreenNavigationProp;
}

export default function SplashScreen({ navigation }: Props) {
  const fadeAnim = new Animated.Value(0);
  const scaleAnim = new Animated.Value(0.3);

  useEffect(() => {
    Animated.parallel([
      Animated.timing(fadeAnim, {
        toValue: 1,
        duration: 1000,
        useNativeDriver: true,
      }),
      Animated.spring(scaleAnim, {
        toValue: 1,
        friction: 4,
        useNativeDriver: true,
      }),
    ]).start();

    const timer = setTimeout(() => {
      navigation.replace('Onboarding');
    }, 2500);

    return () => clearTimeout(timer);
  }, []);

  return (
    <LinearGradient
      colors={['#0a0a0a', '#14b8a6', '#0a0a0a']}
      style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}
      start={{ x: 0, y: 0 }}
      end={{ x: 1, y: 1 }}
    >
      <Animated.View
        style={{
          alignItems: 'center',
          opacity: fadeAnim,
          transform: [{ scale: scaleAnim }],
        }}
      >
        <View style={{
          width: 120,
          height: 120,
          borderRadius: 60,
          backgroundColor: 'rgba(20, 184, 166, 0.2)',
          justifyContent: 'center',
          alignItems: 'center',
          marginBottom: 24,
          borderWidth: 3,
          borderColor: '#14b8a6',
        }}>
          <Text style={{ fontSize: 60 }}>🔒</Text>
        </View>
        <Text style={{ fontSize: 32, fontWeight: 'bold', color: '#ffffff', marginBottom: 8 }}>Lock In Twin</Text>
        <Text style={{ fontSize: 16, color: '#94a3b8', letterSpacing: 1 }}>Focus. Monitor. Achieve.</Text>
      </Animated.View>
    </LinearGradient>
  );
}
