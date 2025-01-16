package br.com.marksonarguello.entities.queue.services;

import br.com.marksonarguello.consumer.ConsumerRecord;
import br.com.marksonarguello.entities.consumer.Consumer;
import br.com.marksonarguello.entities.network.dto.ConsumerConnectionDTO;
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
    private final Map<String, MessageQueue> queues = new HashMap<>();
    private final Map<String, Consumer> consumers = new HashMap<>();
    private final FilePersistenceManager filePersistenceManager = new FilePersistenceManager();

    private QueueService() {
    }

    public static QueueService getInstance() {
        if (queueService == null) {
            queueService = new QueueService();
        }
        return queueService;
    }

    public void addMessageInTopic(String topic, Message message) {
        System.out.println("Adding message to topic: " + topic);
        MessageQueue queue = queues.get(topic);
        queue.addMessage(message);
        filePersistenceManager.saveMessage(message, topic);
        sendMessagesToSubscribers(queue);
    }

    private void sendMessagesToSubscribers(MessageQueue queue) {
        System.out.println("Sending messages to subscribers");
        for (Consumer consumer : queue.getConsumers()) {
            if (!consumer.hasConnection()) {
                System.out.printf("Consumer %s não possui conexão%n", consumer.getId());
                continue;
            }

            Map<String, List<Message>> records = new HashMap<>();
            List<Message> messages = queue.getMessages(consumer.getTopicOffset(queue.getTopic()));

            records.put(queue.getTopic(), messages);
            if (consumer.hasConnection()) {
                boolean receivedMessages = consumer.sendMessages(new ConsumerRecord(records));
                if (receivedMessages) {
                    consumer.setTopicOffset(queue.getTopic(), queue.size());
                }

                filePersistenceManager.saveConsumer(consumer);
            }
        }
    }

    public MessageQueue createQueue(QueueCreateDTO queueCreateDTO) {
        System.out.println("Creating queue with name: " + queueCreateDTO.topic());
        MessageQueue queue = QueueMapper.toEntity(queueCreateDTO);
        queues.put(queue.getTopic(), queue);
        return queue;
    }

    public void subscribe(String id, List<String> topics) {
        System.out.println("Subscribing consumer with id: " + id + " to topics");
        Consumer consumer = new Consumer(id);
        if (consumers.containsKey(id)) {
            consumer = consumers.get(id);
        }

        for (String topic : topics) {
            MessageQueue queue = queues.get(topic);
            if (queue == null) {
                System.out.println("Queue não existe: " + topic);
                throw new RuntimeException("Queue não existe");
            }
            queue.subscribe(consumer);
        }

        consumers.put(id, consumer);
        filePersistenceManager.saveConsumer(consumer);
    }

    public String register(ConsumerConnectionDTO consumerConnectionDTO) {
        System.out.println("Registering consumer");
        Consumer consumer = new Consumer(IdUtil.newId());
        consumers.put(consumer.getId(), consumer);

        if (consumerConnectionDTO != null) {
            try {
                consumer.setConsumerConnectionSocket(consumerConnectionDTO.ip(), consumerConnectionDTO.port());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return consumer.getId();
    }

    public ConsumerRecord consumeMessages(String id) {
        System.out.println("Consuming messages for consumer with id: " + id);
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
        System.out.println("Loading queues");
        Map<String, MessageQueue> storedQueues = filePersistenceManager.loadQueues();
        List<Consumer> storedConsumers = filePersistenceManager.loadConsumers();

        if (storedQueues != null) {
            queues.putAll(storedQueues);
        }

        if (storedConsumers != null) {
            consumers.putAll(storedConsumers.stream().collect(Collectors.toMap(Consumer::getId, Function.identity())));
        }
    }

    public List<MessageQueue> getAllQueues() {
        return queues.values().stream().toList();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : queues.keySet()) {
            stringBuilder.append(queues.get(key));
        }
        return stringBuilder.toString();
    }

}
