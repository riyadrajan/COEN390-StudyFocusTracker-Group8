#include <WiFi.h>
#include <WebSocketsClient.h>



const int LED_PIN =18;
const char *ssid = "WIFI NAME";
const char *password = "WIFI PASSWORD";

const char* server_ip ="COMPUTERS IP"; 
const uint16_t server_port = 3000; 


WebSocketsClient webSocket;
bool light_state =false;

void webSocketEvent(WStype_t type, uint8_t * payload, size_t length) {
    switch(type) {
        case WStype_CONNECTED:
            Serial.println("[WS] Connected to server");
            break;

        case WStype_DISCONNECTED:
            Serial.println("[WS] Disconnected from server");
            break;

        case WStype_TEXT: {
            String msg= String((char*)payload).substring(0,length);
            msg.trim();
            Serial.printf("Message passed - '%s'\n",msg.c_str());
            if(msg.equals("ON") && !light_state){
                light_state = true;
                Serial.println("[WS]Light turned !");
                digitalWrite(LED_PIN,HIGH);
            } else if (msg.equals("OFF")&& light_state){
                light_state=false;
                Serial.println("[WS]Light turned OFF!");
                digitalWrite(LED_PIN,LOW);
            }else{
                Serial.println("[WS] Unknown Message");
            }
            break;
        }

        default:
            break;
    }
}

void connectWebSocket(){
    Serial.println("[WS] Attemping to connect...");
    webSocket.begin(server_ip,server_port, "/ws");
    webSocket.onEvent(webSocketEvent);
}
void setup() {
    Serial.begin(115200);
    pinMode(LED_PIN, OUTPUT);
    digitalWrite(LED_PIN, LOW);

    // Connect to WiFi
    WiFi.begin(ssid, password);
    Serial.print("Connecting to WiFi");
    while (WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.print(".");
    }
    
    Serial.println("\nWiFi connected! IP" +WiFi.localIP().toString());

    connectWebSocket();
}

void loop() {
    webSocket.loop();

    static unsigned long lastReconnectAttempt = 0;
    if(webSocket.isConnected()== false && millis() - lastReconnectAttempt >5000){
        lastReconnectAttempt=millis();
        Serial.println("[WS] Reconnecting...");
        connectWebSocket();
    }
    
}
