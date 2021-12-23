import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Thread class used to receive broadcasted messages from
 * new clients that join the network
 */
public class BroadcastListenerThread extends Thread {

    //create udp socket
    private DatagramSocket broadcastListeningSocket;

    //create a datagram used
    //to receive incoming udp packets
    private DatagramPacket packet;
    private byte[] buf;


    public void run() {

        //udp socket that listens to new clients that join the network
        try {

            broadcastListeningSocket = new DatagramSocket(6666);
            buf = new byte[1024];
            packet = new DatagramPacket(buf, buf.length);

            while (true) {

                //receive broadcast messages
                broadcastListeningSocket.receive(packet);

                System.out.println("received" + new String(buf, StandardCharsets.UTF_8).trim());

                //get client IP address
                InetAddress clientIpAddr = packet.getAddress();

                //get client port
                int clientPort = packet.getPort();

                //send udp packet to client to let him know
                //server ip and enable it open a tcp connection later
                buf = String.valueOf(Broker.BROKER_PORT).getBytes();
                System.out.println("sent" + new String(buf, StandardCharsets.UTF_8).trim());
                packet = new DatagramPacket(buf, buf.length, clientIpAddr, clientPort);
                broadcastListeningSocket.send(packet);


            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
