import java.net.*;
import java.io.*;
import java.util.HashMap;

/**
 * Broker (server) class that
 * handles publishing updated topic values from
 * publishing clients to subscribing clients
 */
public class Broker {

    private ServerSocket welcomingSocket = null;

    private HashMap<String, Topic> topicsMap;

    public static final int BROKER_PORT = 6667;

    private Broker() {

        topicsMap = new HashMap<>();

        try {

            //create server welcoming tcp socket
            welcomingSocket = new ServerSocket(BROKER_PORT, 10);

            //create the thread that listens to
            // broadcast messages from new clients
            BroadcastListenerThread broadcastListenerThread = new BroadcastListenerThread();
            broadcastListenerThread.setDaemon(true);//run independently
            broadcastListenerThread.start();

            while (true) {

                //accept incoming tcp connections from clients
                Socket connSocket = welcomingSocket.accept();

                //assign a working thread to each client
                ServerThread serverThread = new ServerThread(connSocket, this);
                serverThread.setDaemon(true);//run independently
                serverThread.start();

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public synchronized void publishTopic(String topic, String value) {
        if (topicsMap.containsKey(topic)) {

            notifySubscribers(topic, value);

        } else {
            topicsMap.put(topic, new Topic(topic));
        }

    }

    public synchronized void addSubscriberToTopic(String topicName, SubscriberAddr subscriber) {
        if (!topicsMap.containsKey(topicName)) {
            Topic topic = new Topic(topicName);
            topic.addSubscriber(subscriber);
            topicsMap.put(topicName, topic);
        } else {
            topicsMap.get(topicName).addSubscriber(subscriber);
        }
    }

    private void notifySubscribers(String topicName, String newValue) {


        NotifyingThread notifyingThread = null;
        for (SubscriberAddr client : topicsMap.get(topicName).getSubscribers()) {
            notifyingThread = new NotifyingThread(topicName, newValue, client);
            notifyingThread.setDaemon(true);
            notifyingThread.start();
        }


    }


    public static void main(String args[]) {
        new Broker();
    }


}
