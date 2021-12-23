#ifndef Publisher_h
#define Publisher_h

#include "Arduino.h"
#include <ESP8266WiFi.h>
#include <WiFiUdp.h>

class Publisher {

  private:
    const int m_BROKER_BROADCAST_PORT = 6666;
    int m_broker_tcp_port;
    String m_broker_ip;
    String m_topic;
    
  public:
    Publisher(String t_topic);
    void broadcast();
    void publish(String value);


};

#endif
