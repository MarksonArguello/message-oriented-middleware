package teste;

import br.com.marksonarguello.consumer.ConsumerRecord;
import br.com.marksonarguello.entities.consumer.Consumer;
import br.com.marksonarguello.message.Message;

import java.util.Random;
import java.util.Set;

public class ConsumerTest {
    private int key;
    private Consumer consumer;
    private LoggerWriter log;
    private String deliveryMode;
    private String queueType;
    private Random random = new Random();

    public ConsumerTest(int key, String deliveryMode, String queueType, Set<String> topics, Boolean push) {
        this.key = key;
        this.deliveryMode = deliveryMode;
        this.queueType = queueType;
        this.log = new LoggerWriter(getFileName());
        consumer =  new Consumer("localhost", "8080", push);
        consumer.subscribe(topics);
    }

    public void consumeMessages() {
        if (deliveryMode.equalsIgnoreCase("push")) {
            consumePushMode();
        } else if (deliveryMode.equalsIgnoreCase("pull")) {
            consumePullMode();
        } else {
            throw new RuntimeException("Invalid delivery mode, delivery mode should be: push or pull");
        }

        log.close();

    }

    private void consumePushMode() {
        System.out.println("Waiting for messages...");
        try {
            while (true) {
                Message message = consumer.consumeMessage();
                System.out.println("Message received: " + message.getValue());
                log(message);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void consumePullMode() {
        System.out.println("Waiting for messages...");
        try {
            int tries = 0;
            while (true) {
                ConsumerRecord consumerRecord = consumer.poll();
                if (consumerRecord.records().isEmpty() && tries > 2) {
                    break;
                }
                tries++;

                System.out.println("Readed messages: " + consumerRecord.records().values().stream().reduce(0, (acc, list) -> acc + list.size(), Integer::sum));
                for (String topic : consumerRecord.records().keySet()) {
                    for (Message message : consumerRecord.records().get(topic)) {
                        tries = 0;
                        log(message);
                    }
                }


                Thread.sleep(3000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private String getFileName() {
        return "logs/" + deliveryMode.toLowerCase() + "/" + queueType + "_consumer_" + key + ".txt";
    }

    private void log(Message message) {
        log.info(message.getValue());
    }

}
