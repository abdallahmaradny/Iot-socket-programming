import java.io.*;
import java.net.Socket;

public class NotifyingThread extends Thread {
    private String topic;
    private String newValue;
    private SubscriberAddr subscriberAddress;

    public NotifyingThread(String topic, String newValue, SubscriberAddr subscriberAddress) {
        this.topic = topic;
        this.newValue = newValue;
        this.subscriberAddress = subscriberAddress;
    }

    public void run() {

        System.out.println(subscriberAddress.toString());

        try (Socket socket = new Socket(subscriberAddress.getSubscriberIp(), subscriberAddress.getSubscriberListeningPort())) {

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            writer.println(topic + ":" + newValue);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
