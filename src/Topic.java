import java.util.ArrayList;

public class Topic {
    private ArrayList<SubscriberAddr> subscribers;
    private String topic;

    public Topic(String topic) {
        this.subscribers = new ArrayList<>();
        this.topic = topic;
    }

    public void addSubscriber(SubscriberAddr subscriberAddress) {
        subscribers.add(subscriberAddress);
    }

    public ArrayList<SubscriberAddr> getSubscribers() {
        return subscribers;
    }

    @Override
    public String toString() {
        return topic;
    }
}
