package br.com.marksonarguello.entities.persistence;

import br.com.marksonarguello.entities.consumer.Consumer;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ConsumerFileManager {

    private static String queueConsumersPath = FilePersistenceManager.baseFolderPath + "queueConsumers/";
    private static final String SEPARATOR = FilePersistenceManager.SEPARATOR;
    private static final int MAX_CONSUMERS_PER_FILE = 2;

    private FileWriter fileWriter = new FileWriter();

    private FileReader fileReader = new FileReader();

    private Map<String, String> consumerFile = new HashMap<>();

    private Map<String, Integer> consumerFileNumber =  new HashMap<>();

    private int lastConsumerFileNumber = 0;

    public ConsumerFileManager() {
        File file = new File(queueConsumersPath);
        file.mkdir();
    }

    public void saveConsumer(Consumer consumer) {
        if (consumerFileNumber.get(consumer.getId()) == null) {
            consumerFileNumber.put(consumer.getId(), lastConsumerFileNumber);
            String filePath = getFileName(consumer);
            File file = new File(filePath);
            List<Consumer> consumers = new ArrayList<>();
            if (file.exists()) {
                consumers = (List<Consumer>) fileReader.readObject(file.getAbsolutePath());
            }
            consumers.add(consumer);
            fileWriter.writeObject(filePath, consumers);
            if (consumers.size() >= MAX_CONSUMERS_PER_FILE) {
                lastConsumerFileNumber++;
            }
        } else {
            String filePath = getFileName(consumer);
            List<Consumer> consumers = (List<Consumer>) fileReader.readObject(filePath);
            consumers.removeIf(c -> c.getId().equals(consumer.getId()));
            consumers.add(consumer);
            fileWriter.writeObject(filePath, consumers);
        }
    }

    public void saveConsumers(List<Consumer> consumers) {
        for (Consumer consumer : consumers) {
            saveConsumer(consumer);
        }
    }

    public List<Consumer> loadConsumers() {
        List<Consumer> consumers = new ArrayList<>();
        File file = new File(queueConsumersPath);
        if (file.listFiles() == null) {
            return consumers;
        }
        for (File consumerFile : Objects.requireNonNull(file.listFiles())) {
            List<Consumer> consumersList = (List<Consumer>) fileReader.readObject(consumerFile.getAbsolutePath());
            Integer fileNumber = Integer.parseInt(consumerFile.getName().split(SEPARATOR)[1]);
            consumers.forEach(c -> consumerFileNumber.put(c.getId(), fileNumber));
            consumers.addAll(consumersList);
        }
        return consumers;
    }

    private String getFileName(Consumer consumer) {
        return queueConsumersPath + "consumers" + SEPARATOR + this.consumerFileNumber.get(consumer.getId());
    }


}
