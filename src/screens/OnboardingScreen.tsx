import React, { useState, useRef } from 'react';
import {
  View,
  Text,
  Dimensions,
  TouchableOpacity,
  FlatList,
  Animated,
} from 'react-native';
import { StackNavigationProp } from '@react-navigation/stack';
import { RootStackParamList } from '../../App';

const { width } = Dimensions.get('window');

type OnboardingScreenNavigationProp = StackNavigationProp<RootStackParamList, 'Onboarding'>;

interface Props {
  navigation: OnboardingScreenNavigationProp;
}

const slides = [
  {
    id: '1',
    title: 'Monitor Your Focus',
    description: 'Track your concentration levels and stay locked in during study or work sessions.',
    icon: '👁️',
    color: '#14b8a6',
  },
  {
    id: '2',
    title: 'Posture Detection',
    description: 'Get real-time alerts when your posture needs correction to maintain optimal focus.',
    icon: '🧘',
    color: '#8b5cf6',
  },
  {
    id: '3',
    title: 'Smart Breaks',
    description: 'Receive intelligent break reminders to maintain peak productivity throughout the day.',
    icon: '⏰',
    color: '#f59e0b',
  },
];

export default function OnboardingScreen({ navigation }: Props) {
  const [currentIndex, setCurrentIndex] = useState(0);
  const scrollX = useRef(new Animated.Value(0)).current;
  const slidesRef = useRef<FlatList>(null);

  const viewableItemsChanged = useRef(({ viewableItems }: any) => {
    if (viewableItems[0]) {
      setCurrentIndex(viewableItems[0].index);
    }
  }).current;

  const viewConfig = useRef({ viewAreaCoveragePercentThreshold: 50 }).current;

  const scrollTo = () => {
    if (currentIndex < slides.length - 1) {
      slidesRef.current?.scrollToIndex({ index: currentIndex + 1 });
    } else {
      navigation.replace('Permission');
    }
  };

  const renderItem = ({ item }: { item: typeof slides[0] }) => (
    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center', paddingHorizontal: 40, width }}>
      <View style={{ width: 140, height: 140, borderRadius: 70, justifyContent: 'center', alignItems: 'center', marginBottom: 40, backgroundColor: `${item.color}20` }}>
        <Text style={{ fontSize: 70 }}>{item.icon}</Text>
      </View>
      <Text style={{ fontSize: 28, fontWeight: 'bold', color: '#ffffff', textAlign: 'center', marginBottom: 16 }}>{item.title}</Text>
      <Text style={{ fontSize: 16, color: '#94a3b8', textAlign: 'center', lineHeight: 24 }}>{item.description}</Text>
    </View>
  );

  return (
    <View style={{ flex: 1, backgroundColor: '#0a0a0a' }}>
      <View style={{ alignItems: 'flex-end', paddingHorizontal: 20, paddingTop: 60 }}>
        <TouchableOpacity onPress={() => navigation.replace('Permission')}>
          <Text style={{ color: '#94a3b8', fontSize: 16 }}>Skip</Text>
        </TouchableOpacity>
      </View>

      <FlatList
        data={slides}
        renderItem={renderItem}
        horizontal
        showsHorizontalScrollIndicator={false}
        pagingEnabled
        bounces={false}
        keyExtractor={(item) => item.id}
        onScroll={Animated.event([{ nativeEvent: { contentOffset: { x: scrollX } } }], {
          useNativeDriver: false,
        })}
        scrollEventThrottle={32}
        onViewableItemsChanged={viewableItemsChanged}
        viewabilityConfig={viewConfig}
        ref={slidesRef}
      />

      <View style={{ paddingHorizontal: 20, paddingBottom: 60 }}>
        <View style={{ flexDirection: 'row', justifyContent: 'center', marginBottom: 30 }}>
          {slides.map((_, index) => {
            const inputRange = [(index - 1) * width, index * width, (index + 1) * width];
            const dotWidth = scrollX.interpolate({
              inputRange,
              outputRange: [10, 30, 10],
              extrapolate: 'clamp',
            });
            const opacity = scrollX.interpolate({
              inputRange,
              outputRange: [0.3, 1, 0.3],
              extrapolate: 'clamp',
            });

            return (
              <Animated.View
                key={index.toString()}
                style={{ height: 10, borderRadius: 5, backgroundColor: '#14b8a6', marginHorizontal: 5, width: dotWidth, opacity }}
              />
            );
          })}
        </View>

        <TouchableOpacity style={{ backgroundColor: '#14b8a6', paddingVertical: 16, borderRadius: 12, alignItems: 'center' }} onPress={scrollTo}>
          <Text style={{ color: '#ffffff', fontSize: 18, fontWeight: '600' }}>
            {currentIndex === slides.length - 1 ? 'Get Started' : 'Next'}
          </Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}

