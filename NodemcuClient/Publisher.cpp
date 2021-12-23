#include "Publisher.h"


Publisher::Publisher(String t_topic): m_topic(t_topic) {

}

void Publisher::broadcast() {
  //udp broadcast (peer discovery)
  WiFiUDP udp;

  //send broadcast message
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

void Publisher::publish(String value) {

  //send publish message to broker
  WiFiClient tcpClient;
  Serial.println(m_broker_ip);
  Serial.println(m_broker_tcp_port);

  if (tcpClient.connect(m_broker_ip, m_broker_tcp_port)) {
    Serial.println("connected to broker");

    //publish new topic value
    tcpClient.print("Publish" + String(" ") + m_topic + String(":") + value);

    Serial.println("published " +  m_topic + ":" + value);


    tcpClient.stop();
    Serial.println("closed tcp connection");
  }

  //  do {
  //    tcpClient.connect(m_broker_ip, m_broker_tcp_port);
  //  } while (!tcpClient.connected());

  //  Serial.println("connected to broker");
  //
  //  //publish new topic value
  //  tcpClient.print("Publish" + String(" ") + m_topic + String(":") + value);
  //
  //  Serial.println("published " +  m_topic + ":" + value);
  //
  //
  //  tcpClient.stop();
  //  Serial.println("closed tcp connection");
}
