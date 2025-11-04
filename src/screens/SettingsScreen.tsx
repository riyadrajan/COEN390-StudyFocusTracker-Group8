import React from 'react';
import { View, Text, TouchableOpacity, ScrollView, Switch } from 'react-native';
import { StackNavigationProp } from '@react-navigation/stack';
import { RootStackParamList } from '../../App';

type SettingsScreenNavigationProp = StackNavigationProp<RootStackParamList, 'Settings'>;

interface Props {
  navigation: SettingsScreenNavigationProp;
}

export default function SettingsScreen({ navigation }: Props) {
  const [postureMonitoring, setPostureMonitoring] = React.useState(true);
  const [breakReminders, setBreakReminders] = React.useState(true);
  const [soundEffects, setSoundEffects] = React.useState(true);
  const [vibration, setVibration] = React.useState(true);

  return (
    <View style={{ flex: 1, backgroundColor: '#0a0a0a' }}>
      {/* Header */}
      <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', paddingHorizontal: 24, paddingTop: 60, paddingBottom: 20 }}>
        <TouchableOpacity onPress={() => navigation.goBack()}>
          <Text style={{ fontSize: 32, color: '#ffffff' }}>←</Text>
        </TouchableOpacity>
        <Text style={{ fontSize: 20, fontWeight: 'bold', color: '#ffffff' }}>Settings</Text>
        <View style={{ width: 40 }} />
      </View>

      <ScrollView style={{ flex: 1, paddingHorizontal: 24 }} showsVerticalScrollIndicator={false}>
        {/* Monitoring Settings */}
        <View style={{ marginBottom: 32 }}>
          <Text style={{ fontSize: 16, fontWeight: '600', color: '#94a3b8', marginBottom: 12, textTransform: 'uppercase', letterSpacing: 0.5 }}>Monitoring</Text>
          
          <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', backgroundColor: '#18181b', borderRadius: 12, padding: 16, marginBottom: 8, borderWidth: 1, borderColor: '#27272a' }}>
            <View style={{ flex: 1 }}>
              <Text style={{ fontSize: 16, fontWeight: '600', color: '#ffffff', marginBottom: 4 }}>Posture Monitoring</Text>
              <Text style={{ fontSize: 14, color: '#94a3b8' }}>
                Track and alert for poor posture
              </Text>
            </View>
            <Switch
              value={postureMonitoring}
              onValueChange={setPostureMonitoring}
              trackColor={{ false: '#27272a', true: '#14b8a6' }}
              thumbColor="#ffffff"
            />
          </View>

          <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', backgroundColor: '#18181b', borderRadius: 12, padding: 16, marginBottom: 8, borderWidth: 1, borderColor: '#27272a' }}>
            <View style={{ flex: 1 }}>
              <Text style={{ fontSize: 16, fontWeight: '600', color: '#ffffff', marginBottom: 4 }}>Break Reminders</Text>
              <Text style={{ fontSize: 14, color: '#94a3b8' }}>
                Get notified when it's time for a break
              </Text>
            </View>
            <Switch
              value={breakReminders}
              onValueChange={setBreakReminders}
              trackColor={{ false: '#27272a', true: '#14b8a6' }}
              thumbColor="#ffffff"
            />
          </View>

          <TouchableOpacity style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', backgroundColor: '#18181b', borderRadius: 12, padding: 16, marginBottom: 8, borderWidth: 1, borderColor: '#27272a' }}>
            <View style={{ flex: 1 }}>
              <Text style={{ fontSize: 16, fontWeight: '600', color: '#ffffff', marginBottom: 4 }}>Sensitivity</Text>
              <Text style={{ fontSize: 14, color: '#94a3b8' }}>Medium</Text>
            </View>
            <Text style={{ fontSize: 24, color: '#94a3b8' }}>›</Text>
          </TouchableOpacity>
        </View>

        {/* Notifications */}
        <View style={{ marginBottom: 32 }}>
          <Text style={{ fontSize: 16, fontWeight: '600', color: '#94a3b8', marginBottom: 12, textTransform: 'uppercase', letterSpacing: 0.5 }}>Notifications</Text>
          
          <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', backgroundColor: '#18181b', borderRadius: 12, padding: 16, marginBottom: 8, borderWidth: 1, borderColor: '#27272a' }}>
            <View style={{ flex: 1 }}>
              <Text style={{ fontSize: 16, fontWeight: '600', color: '#ffffff', marginBottom: 4 }}>Sound Effects</Text>
              <Text style={{ fontSize: 14, color: '#94a3b8' }}>
                Play sounds for alerts
              </Text>
            </View>
            <Switch
              value={soundEffects}
              onValueChange={setSoundEffects}
              trackColor={{ false: '#27272a', true: '#14b8a6' }}
              thumbColor="#ffffff"
            />
          </View>

          <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', backgroundColor: '#18181b', borderRadius: 12, padding: 16, marginBottom: 8, borderWidth: 1, borderColor: '#27272a' }}>
            <View style={{ flex: 1 }}>
              <Text style={{ fontSize: 16, fontWeight: '600', color: '#ffffff', marginBottom: 4 }}>Vibration</Text>
              <Text style={{ fontSize: 14, color: '#94a3b8' }}>
                Vibrate on alerts
              </Text>
            </View>
            <Switch
              value={vibration}
              onValueChange={setVibration}
              trackColor={{ false: '#27272a', true: '#14b8a6' }}
              thumbColor="#ffffff"
            />
          </View>
        </View>

        {/* Session Defaults */}
        <View style={{ marginBottom: 32 }}>
          <Text style={{ fontSize: 16, fontWeight: '600', color: '#94a3b8', marginBottom: 12, textTransform: 'uppercase', letterSpacing: 0.5 }}>Session Defaults</Text>
          
          <TouchableOpacity style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', backgroundColor: '#18181b', borderRadius: 12, padding: 16, marginBottom: 8, borderWidth: 1, borderColor: '#27272a' }}>
            <View style={{ flex: 1 }}>
              <Text style={{ fontSize: 16, fontWeight: '600', color: '#ffffff', marginBottom: 4 }}>Default Duration</Text>
              <Text style={{ fontSize: 14, color: '#94a3b8' }}>25 minutes</Text>
            </View>
            <Text style={{ fontSize: 24, color: '#94a3b8' }}>›</Text>
          </TouchableOpacity>

          <TouchableOpacity style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', backgroundColor: '#18181b', borderRadius: 12, padding: 16, marginBottom: 8, borderWidth: 1, borderColor: '#27272a' }}>
            <View style={{ flex: 1 }}>
              <Text style={{ fontSize: 16, fontWeight: '600', color: '#ffffff', marginBottom: 4 }}>Break Interval</Text>
              <Text style={{ fontSize: 14, color: '#94a3b8' }}>25 minutes</Text>
            </View>
            <Text style={{ fontSize: 24, color: '#94a3b8' }}>›</Text>
          </TouchableOpacity>

          <TouchableOpacity style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', backgroundColor: '#18181b', borderRadius: 12, padding: 16, marginBottom: 8, borderWidth: 1, borderColor: '#27272a' }}>
            <View style={{ flex: 1 }}>
              <Text style={{ fontSize: 16, fontWeight: '600', color: '#ffffff', marginBottom: 4 }}>Break Duration</Text>
              <Text style={{ fontSize: 14, color: '#94a3b8' }}>5 minutes</Text>
            </View>
            <Text style={{ fontSize: 24, color: '#94a3b8' }}>›</Text>
          </TouchableOpacity>
        </View>

        {/* Account */}
        <View style={{ marginBottom: 32 }}>
          <Text style={{ fontSize: 16, fontWeight: '600', color: '#94a3b8', marginBottom: 12, textTransform: 'uppercase', letterSpacing: 0.5 }}>Account</Text>
          
          <TouchableOpacity style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', backgroundColor: '#18181b', borderRadius: 12, padding: 16, marginBottom: 8, borderWidth: 1, borderColor: '#27272a' }}>
            <View style={{ flex: 1 }}>
              <Text style={{ fontSize: 16, fontWeight: '600', color: '#ffffff', marginBottom: 4 }}>Profile</Text>
              <Text style={{ fontSize: 14, color: '#94a3b8' }}>Manage your profile</Text>
            </View>
            <Text style={{ fontSize: 24, color: '#94a3b8' }}>›</Text>
          </TouchableOpacity>

          <TouchableOpacity style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', backgroundColor: '#18181b', borderRadius: 12, padding: 16, marginBottom: 8, borderWidth: 1, borderColor: '#27272a' }}>
            <View style={{ flex: 1 }}>
              <Text style={{ fontSize: 16, fontWeight: '600', color: '#ffffff', marginBottom: 4 }}>Data & Privacy</Text>
              <Text style={{ fontSize: 14, color: '#94a3b8' }}>Control your data</Text>
            </View>
            <Text style={{ fontSize: 24, color: '#94a3b8' }}>›</Text>
          </TouchableOpacity>
        </View>

        {/* About */}
        <View style={{ marginBottom: 32 }}>
          <Text style={{ fontSize: 16, fontWeight: '600', color: '#94a3b8', marginBottom: 12, textTransform: 'uppercase', letterSpacing: 0.5 }}>About</Text>
          
          <TouchableOpacity style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', backgroundColor: '#18181b', borderRadius: 12, padding: 16, marginBottom: 8, borderWidth: 1, borderColor: '#27272a' }}>
            <View style={{ flex: 1 }}>
              <Text style={{ fontSize: 16, fontWeight: '600', color: '#ffffff', marginBottom: 4 }}>Help & Support</Text>
              <Text style={{ fontSize: 14, color: '#94a3b8' }}>Get help with the app</Text>
            </View>
            <Text style={{ fontSize: 24, color: '#94a3b8' }}>›</Text>
          </TouchableOpacity>

          <TouchableOpacity style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', backgroundColor: '#18181b', borderRadius: 12, padding: 16, marginBottom: 8, borderWidth: 1, borderColor: '#27272a' }}>
            <View style={{ flex: 1 }}>
              <Text style={{ fontSize: 16, fontWeight: '600', color: '#ffffff', marginBottom: 4 }}>Privacy Policy</Text>
              <Text style={{ fontSize: 14, color: '#94a3b8' }}>Read our privacy policy</Text>
            </View>
            <Text style={{ fontSize: 24, color: '#94a3b8' }}>›</Text>
          </TouchableOpacity>

          <TouchableOpacity style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', backgroundColor: '#18181b', borderRadius: 12, padding: 16, marginBottom: 8, borderWidth: 1, borderColor: '#27272a' }}>
            <View style={{ flex: 1 }}>
              <Text style={{ fontSize: 16, fontWeight: '600', color: '#ffffff', marginBottom: 4 }}>Terms of Service</Text>
              <Text style={{ fontSize: 14, color: '#94a3b8' }}>Read our terms</Text>
            </View>
            <Text style={{ fontSize: 24, color: '#94a3b8' }}>›</Text>
          </TouchableOpacity>

          <View style={{ flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center', backgroundColor: '#18181b', borderRadius: 12, padding: 16, marginBottom: 8, borderWidth: 1, borderColor: '#27272a' }}>
            <View style={{ flex: 1 }}>
              <Text style={{ fontSize: 16, fontWeight: '600', color: '#ffffff', marginBottom: 4 }}>Version</Text>
              <Text style={{ fontSize: 14, color: '#94a3b8' }}>1.0.0</Text>
            </View>
          </View>
        </View>

        {/* Danger Zone */}
        <View style={{ marginBottom: 32 }}>
          <TouchableOpacity style={{ backgroundColor: '#18181b', borderRadius: 12, padding: 16, marginBottom: 8, borderWidth: 1, borderColor: '#ef4444', alignItems: 'center' }}>
            <Text style={{ fontSize: 16, fontWeight: '600', color: '#ef4444' }}>Recalibrate Posture</Text>
          </TouchableOpacity>
          
          <TouchableOpacity style={{ backgroundColor: '#18181b', borderRadius: 12, padding: 16, marginBottom: 8, borderWidth: 1, borderColor: '#ef4444', alignItems: 'center' }}>
            <Text style={{ fontSize: 16, fontWeight: '600', color: '#ef4444' }}>Clear All Data</Text>
          </TouchableOpacity>
        </View>
      </ScrollView>
    </View>
  );
}

