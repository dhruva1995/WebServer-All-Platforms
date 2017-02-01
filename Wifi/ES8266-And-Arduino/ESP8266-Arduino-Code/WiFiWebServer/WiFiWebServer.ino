/*
 *  This sketch demonstrates how to set up a simple HTTP-like server.
 *  The server will set a GPIO pin depending on the request
 *    http://server_ip/gpio/0 will set the GPIO2 low,
 *    http://server_ip/gpio/1 will set the GPIO2 high
 *  server_ip is the IP address of the ESP8266 module, will be 
 *  printed to Serial when the module is connected.
 */

#include <ESP8266WiFi.h>
#include <Adafruit_NeoPixel.h>

#define PIN            0

#define NUMPIXELS      1

Adafruit_NeoPixel pixels = Adafruit_NeoPixel(NUMPIXELS, PIN, NEO_GRB + NEO_KHZ800);

int delayval = 500; // delay for half a second

const char* ssid = "SSID-OF-YOUR-WIFI";
const char* password = "PASSWORD-OF-YOUR-WIFI";

// Create an instance of the server
// specify the port to listen on as an argument
WiFiServer server(80);

void setup() {
  Serial.begin(115200);
  delay(10);

  // prepare GPIO2
  pinMode(2, OUTPUT);
  digitalWrite(2, 0);
  pixels.begin();

  pixels.setPixelColor(0, pixels.Color(0,150,0)); // Moderately bright green color.

  pixels.show(); // This sends the updated pixel 
  // Connect to WiFi network
  Serial.println();
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);
  
  WiFi.begin(ssid, password);
  
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi connected");
  
  // Start the server
  server.begin();
  Serial.println("Server started");

  // Print the IP address
  Serial.println(WiFi.localIP());
}

void loop() {
  // Check if a client has connected
  WiFiClient client = server.available();
  if (!client) {
    return;
  }
  
  // Wait until the client sends some data
  Serial.println("new client");
  while(!client.available()){
    delay(1);
  }
  
  // Read the first line of the request
  String req = client.readStringUntil('\r');
  Serial.println(req);
  client.flush();
  
  // Match the request
  int val;
  if (req.indexOf("/gpio/0") != -1)
    val = 0;
  else if (req.indexOf("/gpio/1") != -1)
    val = 1;
  else if (req.indexOf("/rgb/") != -1){
    
    Serial.print("The req is ");
    Serial.println(req);
  
    String bStr = req.substring(req.lastIndexOf(',') + 1);
  
    req.remove(req.lastIndexOf(','));
  
    String gStr = req.substring(req.lastIndexOf(',') + 1);
  
    req.remove(req.lastIndexOf(','));
  
    String rStr = req.substring(req.lastIndexOf('/') + 1);
  
    Serial.print("R = ");
    Serial.println(rStr.toInt());
  
    Serial.print("G = ");
    Serial.println(gStr.toInt());
  
    Serial.print("B = ");
    Serial.println(bStr.toInt());

    pixels.setPixelColor(0, pixels.Color(rStr.toInt(),gStr.toInt(),bStr.toInt())); // Moderately bright green color.

    pixels.show(); // This sends the updated pixel 
  
  }
  else {
    Serial.println("invalid request");
    client.stop();
    return;
  }

  // Set GPIO2 according to the request
  digitalWrite(2, val);
  
  client.flush();

  // Prepare the response
  String s = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<!DOCTYPE HTML>\r\n<html>\r\nGPIO is now ";
  s += (val)?"high":"low";
  s += "</html>\n";

  // Send the response to the client
  client.print(s);
  delay(1);
  Serial.println("Client disonnected");

  // The client will actually be disconnected 
  // when the function returns and 'client' object is detroyed
}

