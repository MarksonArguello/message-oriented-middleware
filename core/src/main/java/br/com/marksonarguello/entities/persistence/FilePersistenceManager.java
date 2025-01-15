package br.com.marksonarguello.entities.persistence;

import br.com.marksonarguello.entities.consumer.Consumer;
import br.com.marksonarguello.entities.queue.MessageQueue;
import br.com.marksonarguello.message.Message;

import java.io.File;
import java.util.List;
import java.util.Map;


public class FilePersistenceManager {
    public static final String baseFolderPath = "data/";
    public static final String SEPARATOR = "_";
    private static final Boolean persistToFile = true;
    private ConsumerFileManager consumerFileManager;
    private MessageFileManager messageFileManager;

    public FilePersistenceManager() {
        File file = new File(baseFolderPath);
        file.mkdir();
        consumerFileManager = new ConsumerFileManager();
        messageFileManager = new MessageFileManager();
    }

    public void saveQueueMessages(MessageQueue messageQueue) {
        if (!persistToFile) {
            return;
        }

        messageFileManager.saveQueueMessages(messageQueue);
    }

    public void saveMessage(Message message, String topic) {
        if (!persistToFile) {
            return;
        }

        messageFileManager.saveMessage(message, topic);
    }

    public void saveConsumer(Consumer consumer) {
        if (!persistToFile) {
            return;
        }

        consumerFileManager.saveConsumer(consumer);
    }


    public Map<String, MessageQueue> loadQueues() {
        if (!persistToFile) {
            return null;
        }
        Map<String, MessageQueue> messageQueues = messageFileManager.loadMessageQueues();

        List<Consumer> consumers = consumerFileManager.loadConsumers();

        if (consumers == null || messageQueues == null || messageQueues.isEmpty()) {
            return messageQueues;
        }

        for (Consumer consumer : consumers) {
            for (String topic : consumer.getInterestedTopics()) {
                messageQueues.get(topic).subscribe(consumer);
            }
        }

        return messageQueues;
    }

    public List<Consumer> loadConsumers() {
        if (!persistToFile) {
            return null;
        }
        return consumerFileManager.loadConsumers();
    }

}
