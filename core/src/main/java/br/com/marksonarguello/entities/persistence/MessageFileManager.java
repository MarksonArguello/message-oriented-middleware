package br.com.marksonarguello.entities.persistence;

import br.com.marksonarguello.entities.queue.MessageQueue;
import br.com.marksonarguello.message.Message;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MessageFileManager {
    private static String queueMessagesPath = FilePersistenceManager.baseFolderPath + "queueMessages/";
    private static final String SEPARATOR = FilePersistenceManager.SEPARATOR;
    private static final int MAX_FILE_SIZE_IN_BYTES = 1;
    private FileWriter fileWriter = new FileWriter();
    private FileReader fileReader = new FileReader();
    private Map<String, Integer> messageFileNumber = new HashMap<>();

    public MessageFileManager() {
        File file = new File(queueMessagesPath);
        file.mkdir();
    }

    public void saveQueueMessages(MessageQueue messageQueue) {
        List<Message> messages = messageQueue.getMessages();

        int fileNumber = 0;
        int currentSize = 0;
        List<Message> currentMessages = new ArrayList<>();
        for (Message message : messages) {
            if (currentSize + message.getSizeInBytes() > MAX_FILE_SIZE_IN_BYTES && !currentMessages.isEmpty()) {
                this.writeMessagesInFile(messageQueue.getTopic(), currentMessages);
                currentMessages.clear();
                currentSize = 0;
                fileNumber++;
                this.messageFileNumber.put(messageQueue.getTopic(), fileNumber);
            }
            currentMessages.add(message);
            currentSize += message.getSizeInBytes();
        }

        if (!currentMessages.isEmpty()) {
            this.writeMessagesInFile(messageQueue.getTopic(), currentMessages);
        }

    }

    public void saveMessage(Message message, String topic) {
        File file = new File(getLastFileName(topic));

        List<Message> messages = new ArrayList<>();
        messageFileNumber.putIfAbsent(topic, 0);
        if (file.exists()) {
            messages = (List<Message>) fileReader.readObject(getLastFileName(topic));
        }

        int currentSize = messages.stream().mapToInt(Message::getSizeInBytes).sum();
        if (!messages.isEmpty() && currentSize + message.getSizeInBytes() > MAX_FILE_SIZE_IN_BYTES) {
            messages.clear();
            messageFileNumber.put(topic, messageFileNumber.get(topic) + 1);
        }

        messages.add(message);
        writeMessagesInFile(topic, messages);
    }

    public Map<String, MessageQueue> loadMessageQueues() {
        File messagesFolder = new File(queueMessagesPath);
        Map<String, MessageQueue> messageQueues = new HashMap<>();

        if (!messagesFolder.exists() || messagesFolder.listFiles() == null) {
            return null;
        }

        List<File> files = new ArrayList<>(Arrays.stream(Objects.requireNonNull(messagesFolder.listFiles())).toList());

        for (File file : files) {
            String[] fileNameParts = file.getName().split(SEPARATOR);
            String topic = fileNameParts[0];
            int number = Integer.parseInt(fileNameParts[1]);

            this.messageFileNumber.put(topic, number);

            List<Message> messages = (List<Message>) fileReader.readObject(file.getAbsolutePath());
            messageQueues.computeIfAbsent(topic, k -> new MessageQueue(topic));
            messageQueues.get(topic).addMessages(messages);
        }

        for (String topic : messageQueues.keySet()) {
            System.out.println("Loaded " + messageQueues.get(topic).size() + " messages for topic '" + topic + "'");
        }

        return messageQueues;
    }

    private void writeMessagesInFile(String topic, List<Message> messages) {
        fileWriter.writeObject(getLastFileName(topic), messages);
    }

    private String getLastFileName(String topic) {
        return queueMessagesPath + topic + SEPARATOR + this.messageFileNumber.get(topic);
    }
}
