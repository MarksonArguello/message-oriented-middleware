package br.com.marksonarguello.entities.queue.services;

import br.com.marksonarguello.consumer.ConsumerRecord;
import br.com.marksonarguello.entities.consumer.Consumer;
import br.com.marksonarguello.entities.queue.MessageQueue;
import br.com.marksonarguello.entities.queue.dto.QueueCreateDTO;
import br.com.marksonarguello.entities.queue.dto.QueueMapper;
import br.com.marksonarguello.message.Message;
import br.com.marksonarguello.util.IdUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueueService {

    private static QueueService queueService;
    private static final Map<String, MessageQueue> queues = new HashMap<>();
    private static final Map<String, Consumer> consumers = new HashMap<>();

    private QueueService() {
    }

    public static QueueService getInstance() {
        if (queueService == null) {
            queueService = new QueueService();
        }
        return queueService;
    }

    public void addMessageInTopic(String topic, Message message) {
        MessageQueue queue = queues.get(topic);
        queue.addMessage(message);
    }

    public List<String> getAllTopics() {
        return queues.values().stream().map(MessageQueue::getTopic).toList();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : queues.keySet()) {
            stringBuilder.append(queues.get(key));
        }
        return stringBuilder.toString();
    }

    public MessageQueue createQueue(QueueCreateDTO queueCreateDTO) {
        MessageQueue queue = QueueMapper.toEntity(queueCreateDTO);
        queues.put(queue.getTopic(), queue);
        return queue;
    }

    public void subscribe(String id, List<String> topics) {
        Consumer consumer = new Consumer(id);
        if (consumers.containsKey(id)) {
            consumer = consumers.get(id);
        }

        for (String topic : topics) {
            MessageQueue queue = queues.get(topic);
            queue.subscribe(consumer);
        }

        consumers.put(id, consumer);
    }

    public String register() {
        return IdUtil.newId();
    }

    public ConsumerRecord consumeMessages(String id) {
        Consumer consumer = consumers.get(id);
        if (consumer == null) {
            Map<String, Consumer> c = new HashMap<>(QueueService.consumers);
            System.out.println(c);
            //throw new RuntimeException("Id não existe");
        }

        Map<String, List<Message>> records = new HashMap<>();

        for (String topic : consumer.getInterestedTopics()) {
            MessageQueue queue = queues.get(topic);
            List<Message> messages = queue.getMessages(consumer.getTopicOffset(topic));

            if (messages != null) {
                records.put(topic, messages);
                consumer.setTopicOffset(topic, queue.size());
            }
        }

        return new ConsumerRecord(records);
    }
}
