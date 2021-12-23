import java.net.InetAddress;

public class SubscriberAddr {
    private InetAddress subscriberIp;
    private int subscriberListeningPort;

    public SubscriberAddr(InetAddress subscriberIp, int subscriberListeningPort) {
        this.subscriberIp = subscriberIp;
        this.subscriberListeningPort = subscriberListeningPort;
    }

    public InetAddress getSubscriberIp() {
        return subscriberIp;
    }

    public int getSubscriberListeningPort() {
        return subscriberListeningPort;
    }

    @Override
    public String toString() {
        return subscriberIp + " " + subscriberListeningPort;
    }

}
