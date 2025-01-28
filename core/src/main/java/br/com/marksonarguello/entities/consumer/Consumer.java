package br.com.marksonarguello.entities.consumer;


import br.com.marksonarguello.consumer.ConsumerRecord;
import br.com.marksonarguello.entities.network.ConsumerConnectionSocket;
import java.io.IOException;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Consumer implements Serializable {
    private final Map<String, Integer> topicsOffset = new HashMap<>();
    private final String id;

    private transient ConsumerConnectionSocket consumerConnectionSocket;

    public Consumer(String id) {
        this.id = id;
    }

    public Integer getTopicOffset(String topic) {
        return topicsOffset.get(topic);
    }

    public Collection<String> getInterestedTopics() {
        return topicsOffset.keySet();
    }

    public void subscribe(String topic) {
        topicsOffset.put(topic, 0);
    }

    public void setTopicOffset(String topic, int size) {
        topicsOffset.put(topic, size);
    }

    public void setConsumerConnectionSocket(String ip, int port) throws IOException {
        consumerConnectionSocket = new ConsumerConnectionSocket();
        consumerConnectionSocket.startConnection(ip, port);
        System.out.println("Consumer " + id + " connected to " + ip + ":" + port);
    }

    public String getId() {
        return id;
    }

    public boolean sendMessages(ConsumerRecord records) throws IOException {
        consumerConnectionSocket.sendMessages(records);
        return true;
    }

    public boolean hasConnection() {
        return consumerConnectionSocket != null;
    }
}
