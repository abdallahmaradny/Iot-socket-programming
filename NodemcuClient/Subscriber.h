#ifndef Subscriber_h
#define Subscriber_h

#include "Arduino.h"
#include <ESP8266WiFi.h>
#include <WiFiUdp.h>

class Subscriber {
  private:
    WiFiServer* m_tcp_server_pointer;

    const int m_BROKER_BROADCAST_PORT = 6666;
    int m_broker_tcp_port;
    String m_broker_ip;
    int m_port;
    String m_topic;
    String m_value;

  public:
    Subscriber(int t_port, String t_topic);
    void broadcast();
    void subscribe();
    void initServer();
    bool checkForUpdate();
    String getValue();


};

#endif
