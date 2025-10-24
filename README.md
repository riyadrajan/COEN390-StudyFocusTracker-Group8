# Instructions  
## Android App configurations
- Android manifest.xml : include the uses permissions, and networkSecurityConfig
```xml
    <!--In manifest.xml -->
    <!-- Needed for network calls -->
    <uses-permission android:name="android.permission.INTERNET" />
...
    <application
    ...
        android:networkSecurityConfig="@xml/network_security_config">
    ...
     </application>
```
- In networkSecurityConfig.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <base-config cleartextTrafficPermitted="true" />
</network-security-config>
```
- MainActivity.java
  - Use http://10.0.2.2:3000 as the base URL when using the android emulator 
  - Use local IP address if using the physical android phone  
Quick way to find local IP address on mac/linux:
```bash
ipconfig getifaddr en0
```

## Running Flask in Python
- Navigate to the StateDetectionLogic Folder  
- In terminal, run the following commands
```bash
python3 -m venv .venv
```
```bash
. .venv/bin/activate
```
```bash
pip install Flask
```
```bash
PORT=3000 .venv/bin/python -m driver_state_detection.server
```

## Run server from StateDetectorLogic dir
```bash
PORT=3000 driver_state_detection/.venv/bin/python -m driver_state_detection.server
```

## Run logic detection 
```bash
go into driver_state_detection dir
```
```bash
python3 -m venv .venv
```
```bash
source .venv/bin/activate
```
* pip install flask opencv-python mediapipe numpy (If not installed)
```bash
python3 main.py
```


## Clean port  
- Check if the port is being used:
```bash
sudo lsof -iTCP -sTCP:LISTEN -P -n
```  
- Kill process using its pid  
```bash
  kill -9 <pid>
```
# References
This project makes use of Driver State Detection (https://github.com/e-candeloro/Driver-State-Detection).
