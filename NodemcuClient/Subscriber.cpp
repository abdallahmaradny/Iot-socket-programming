#include "Subscriber.h"


Subscriber::Subscriber(int t_port , String t_topic): m_port(t_port), m_topic(t_topic) {

}

void Subscriber::broadcast() {
  //udp broadcast (peer discovery)
  WiFiUDP udp;

  //send broadcast message
  udp.flush();
  udp.beginPacket("255.255.255.255", m_BROKER_BROADCAST_PORT);
  udp.write("Hi");
  udp.endPacket();

  //listen on local port
  udp.begin(udp.localPort());
  Serial.printf("listening at IP %s, UDP port %d\n", WiFi.localIP().toString().c_str(), udp.localPort());

  //retreive broker ip and port for tcp connections in the future
  while (udp.parsePacket() == 0);
  m_broker_ip = udp.remoteIP().toString();
  m_broker_tcp_port = udp.readString().toInt();

  Serial.println("Server Tcp listening socket is at " + m_broker_ip + ":" + m_broker_tcp_port);

  //close udp session
  udp.stop();

  Serial.println("Udp socket closed");

}

void Subscriber::subscribe() {

  //send subscription message to broker
  WiFiClient tcpClient;

  do {
    tcpClient.connect(m_broker_ip, m_broker_tcp_port);
  } while (!tcpClient.connected());

  Serial.println("connected to broker");

  //subscribe to topic
  tcpClient.print("Subscribe" + String(" ") + m_topic + String(" ") + String(m_port));

  Serial.println("Subscribed to " + m_topic);


  tcpClient.stop();
  Serial.println("closed tcp connection");

}

void Subscriber::initServer() {

  WiFiServer tcpServer(m_port);
  m_tcp_server_pointer = &tcpServer;
  m_tcp_server_pointer->begin();

}

bool Subscriber::checkForUpdate() {
  // wait for broker to connect
  WiFiClient broker = m_tcp_server_pointer->available();

  if (broker && broker.available()) {

    Serial.println("Broker connected");

    String message = broker.readString();
    Serial.print("received msg : " + message);

    // close the connection:
    broker.stop();
    Serial.println("broker tcp connection closed");

    return true;
  }

  return false;
}

String Subscriber::getValue() {
  return m_value;
}
