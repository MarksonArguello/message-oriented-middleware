package br.com.marksonarguello.entities.queue.services;

import br.com.marksonarguello.consumer.ConsumerRecord;
import br.com.marksonarguello.entities.consumer.Consumer;
import br.com.marksonarguello.entities.persistence.FilePersistenceManager;
import br.com.marksonarguello.entities.queue.MessageQueue;
import br.com.marksonarguello.entities.queue.dto.QueueCreateDTO;
import br.com.marksonarguello.entities.queue.dto.QueueMapper;
import br.com.marksonarguello.message.Message;
import br.com.marksonarguello.util.IdUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class QueueService {

    private static QueueService queueService;
    private static final Map<String, MessageQueue> queues = new HashMap<>();
    private static final Map<String, Consumer> consumers = new HashMap<>();
    private static final FilePersistenceManager filePersistenceManager = new FilePersistenceManager();

    private QueueService() {
    }

    public static QueueService getInstance() {
        if (queueService == null) {
            queueService = new QueueService();
        }
        return queueService;
    }

    public List<MessageQueue> getAllQueues() {
        return queues.values().stream().toList();
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

    public void addMessageInTopic(String topic, Message message) {
        MessageQueue queue = queues.get(topic);
        queue.addMessage(message);
        filePersistenceManager.saveMessage(message, topic);
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
        filePersistenceManager.saveConsumer(consumer);
    }

    public String register() {
        return IdUtil.newId();
    }

    public ConsumerRecord consumeMessages(String id) {
        Consumer consumer = consumers.get(id);
        if (consumer == null) {
            throw new RuntimeException("Id não existe");
        }

        Map<String, List<Message>> records = new HashMap<>();

        for (String topic : consumer.getInterestedTopics()) {
            MessageQueue queue = queues.get(topic);
            List<Message> messages = queue.getMessages(consumer.getTopicOffset(topic));

            if (messages != null) {
                records.put(topic, messages);
                consumer.setTopicOffset(topic, queue.size());
            }
            filePersistenceManager.saveConsumer(consumer);
        }

        return new ConsumerRecord(records);
    }

    public void loadQueues() {
        Map<String, MessageQueue> storedQueues = filePersistenceManager.loadQueues();
        List<Consumer> storedConsumers = filePersistenceManager.loadConsumers();

        if (queues == null) {
            return;
        }

        queues.putAll(storedQueues);
        consumers.putAll(storedConsumers.stream().collect(Collectors.toMap(Consumer::getId, Function.identity())));
    }

}
