package br.com.marksonarguello.entities.broker;

import br.com.marksonarguello.entities.baseEntity.BaseEntity;
import br.com.marksonarguello.entities.message.Message;
import br.com.marksonarguello.entities.queue.MessageQueue;

import java.util.HashMap;
import java.util.Map;

public final class Broker extends BaseEntity {
    private static Broker broker;
    private static final Map<String, MessageQueue> queues = new HashMap<>();
    private Broker() {
    }

    public static Broker getInstance() {
        if (broker == null) {
            return new Broker();
        }

        return broker;
    }

    public void createQueue(MessageQueue messageQueue) {
        queues.put(messageQueue.getTopic(), messageQueue);
    }

    public void addMessageInTopic(String topic, Message message) {
        MessageQueue queue = queues.get(topic);
        queue.addMessage(message);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Queues:\n");
        for (String key : queues.keySet()) {
           stringBuilder.append(queues.get(key));
        }
        return stringBuilder.toString();
    }
}
