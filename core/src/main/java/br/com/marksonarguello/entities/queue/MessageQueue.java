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
    private Iterator<Consumer> consumerIterator = consumers.iterator();

    public MessageQueue(String topic) {
        this.topic = topic;
    }

    public synchronized void addMessage(Message message) {
        queue.add(message);
        //System.out.println(queue.size());
    }

    public synchronized List<Message> getMessages() {
        return new ArrayList<>(this.queue);
    }
    public synchronized List<Message> getMessages(int offset) {
        System.out.println(offset);
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

    public Message popMessage() {
        if (queue.isEmpty()) {
            return null;
        }
        return queue.remove(0);
    }

    public List<Message> popAllMessages() {
        List<Message> messages = new ArrayList<>(queue);
        queue.clear();
        return messages;
    }

    public Consumer nextConsumer() {
        if (!consumerIterator.hasNext()) {
            if (this.consumers.isEmpty()) {
                return null;
            }

            consumerIterator = consumers.iterator();
        }

        return consumerIterator.next();
    }

    public void subscribe(List<Consumer> consumers) {
        consumers.forEach(this::subscribe);
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

    public List<Consumer> getConsumers() {
        return new ArrayList<>(consumers);
    }

    @Override
    public String toString() {

        return "MessageQueue {\n" +
                "topic: " + topic + '\n' +
                "messagesSize: " + queue.size() + "\n" +
                "consumers: " + consumers.size() + "\n" +
                "}\n";
    }

    public synchronized void addMessages(List<Message> messages) {
        queue.addAll(messages);
        System.out.println(queue.size());
    }
}
