package br.com.marksonarguello.entities.queue;

import br.com.marksonarguello.entities.baseEntity.BaseEntity;
import br.com.marksonarguello.entities.consumer.Consumer;
import br.com.marksonarguello.entities.message.Message;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class MessageQueue extends BaseEntity {
    private final String topic;
    private final Queue<Message> queue = new LinkedList<>();
    private final Set<Consumer> consumers = new HashSet<>();

    public MessageQueue(String topic) {
        this.topic = topic;
    }

    public void addMessage(Message message) {
        queue.add(message);
    }

    public Message getLast(Message message) {
        return queue.peek();
    }

    public void subscribe(Consumer consumer) {
        consumers.add(consumer);
    }

    public void unsubscribe(Consumer consumer) {
        consumers.remove(consumer);
    }

    public String getTopic() {
        return topic;
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
