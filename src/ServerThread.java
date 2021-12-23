import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;

/**
 * worker thread that services
 * a client request
 */
public class ServerThread extends Thread {

    private Socket connSocket;
    private Broker broker;

    public ServerThread(Socket connSocket, Broker broker) {
        this.broker = broker;
        this.connSocket = connSocket;
    }

    public void run() {

        //talk to client
        try {

            InputStream input = connSocket.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            String msg = "";
            String line;

            while ((line = reader.readLine()) != null) {
                msg += line;
            }

            /*
             * format
             * ------
             *   Publish <Topic>:<newValue>
             *   Subscribe <Topic> <Listening port>
             */


            String[] splitMsg = msg.split("[ :]");
            System.out.println(msg + " : " + Arrays.toString(splitMsg));

            String topicName;
            String topicValue;
            int listeningPort;
            switch (splitMsg[0].trim()) {
                case "Publish":

                    topicName = splitMsg[1].trim();
                    topicValue = splitMsg[2].trim();
                    broker.publishTopic(topicName, topicValue);

                    break;
                case "Subscribe":

                    topicName = splitMsg[1].trim();
                    listeningPort = Integer.valueOf(splitMsg[2].trim());
                    broker.addSubscriberToTopic(topicName, new SubscriberAddr(connSocket.getInetAddress(), listeningPort ));
                    break;
                default:
                    System.out.println("unknown message format");


            }

            connSocket.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
