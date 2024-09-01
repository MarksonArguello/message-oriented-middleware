package br.com.marksonarguello.entities.queue;

import br.com.marksonarguello.baseEntity.BaseEntity;
import br.com.marksonarguello.entities.consumer.Consumer;
import br.com.marksonarguello.message.Message;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MessageQueue extends BaseEntity {
    private final String topic;
    private final List<Message> queue = new LinkedList<>();
    private final Set<Consumer> consumers = new HashSet<>();
    private final ConsumptionMode consumptionMode = ConsumptionMode.PULL;

    public MessageQueue(String topic) {
        this.topic = topic;
    }

    public void addMessage(Message message) {
        queue.add(message);
    }

    public List<Message> getMessages(int offset) {
        if (offset >= queue.size())
            return null;

        List<Message> messages = new ArrayList<>();
        Iterator<Message> iterator = queue.listIterator(offset);
        while (iterator.hasNext()) {
            messages.add(iterator.next());
        }

        return messages;
    }

    public void subscribe(Consumer consumer) {
        consumers.add(consumer);
        consumer.subscribe(topic);
    }

    public void unsubscribe(Consumer consumer) {
        consumers.remove(consumer);
    }

    public String getTopic() {
        return topic;
    }

    public int size() {
        return queue.size();
    }

    @Override
    public String toString() {

        return "MessageQueue {\n" +
                "topic: " + topic + '\n' +
                "messagesSize: " + queue.size() + "\n" +
                "consumers: " + consumers.size() + "\n" +
                "}\n";
    }
}
