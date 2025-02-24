package br.com.marksonarguello.entities.configuration;

import br.com.marksonarguello.entities.queue.dto.QueueCreateDTO;
import br.com.marksonarguello.entities.queue.services.QueueService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationService {

    private static Properties properties = new Properties();

    public static void loadProperties() {
        setDefaultProperties();

        try {
            String path = "mom-config.properties";

            InputStream inputStream = ConfigurationService.class.getClassLoader().getResourceAsStream(path);
            if (inputStream == null) {
                System.out.println("Properties file not found: " + path);
                return;
            }
            properties.load(inputStream);
            for (String key : properties.stringPropertyNames()) {
                System.setProperty(key, properties.getProperty(key));
            }

        } catch (IOException e) {
            System.out.println("Error loading properties file: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void loadQueues() {
        QueueService queueService = QueueService.getInstance();
        queueService.loadQueues();

        queueService.createQueue(new QueueCreateDTO("test-topic"));

    }

    private static void setDefaultProperties() {
        // Persistence
        System.setProperty("persistence.enable", "false");
        System.setProperty("persistence.base.folder", "data/");
        System.setProperty("persistence.file.separator", "_");
        System.setProperty("persistence.consumer.folder", "queueConsumers/");
        System.setProperty("persistence.consumer.max.per.file", "2");
        System.setProperty("persistence.queue.folder", "queueMessages/");
        System.setProperty("persistence.queue.max.file.size.bytes", "5");

        // Message
        System.setProperty("message.max.size.bytes", "1024");
        System.setProperty("message.log.file", "message.log");

        // Queue
        String mode = System.getenv("QUEUE_MODE");
        System.setProperty("queue.delivery.mode", mode != null ? mode : "PUBLISHER_SUBSCRIBER");
        String type = System.getenv("QUEUE_TYPE");
        System.setProperty("queue.type", type != null && type.equals("PUSH") ? type : "PULL");
    }
}
