#include "Subscriber.h"
#include "Publisher.h"

#include <math.h>
#include <Ticker.h>

//predefine a tcp port 2^10 - 2^16
int subscriberPort = (rand() % 64512) + pow(2, 10);
String topic = "color";
Subscriber colorSubscriber(subscriberPort, topic);

Publisher colorPublisher(topic);
Ticker ticker;


void setup() {

  Serial.begin(115200);

  //Connecting to the WiFi
  //    WiFi.begin("Phi Coworking Space", "phi123456");
//    WiFi.begin("mo2men", "mo2aengineer");
  WiFi.begin("maradny", "0000aaaa");
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("WiFi connected");

  //init subscriber
  colorSubscriber.broadcast();
  colorSubscriber.subscribe();
  colorSubscriber.initServer();


  //init publisher
  colorPublisher.broadcast();

  ticker.attach(10, publishValue);

}

void loop() {
  if (colorSubscriber.checkForUpdate())
    Serial.println(colorSubscriber.getValue());

  yield();
}


void publishValue() {
  colorPublisher.publish(String(rand()));
}
