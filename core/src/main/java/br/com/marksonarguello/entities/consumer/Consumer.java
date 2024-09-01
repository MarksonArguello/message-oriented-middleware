package br.com.marksonarguello.entities.consumer;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Consumer {
    private final Map<String, Integer> topicsOffset = new HashMap<>();
    private final String id;

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
}
