package teste_perfomace;

import br.com.marksonarguello.consumer.ConsumerRecord;
import br.com.marksonarguello.entities.consumer.Consumer;
import br.com.marksonarguello.entities.producer.Producer;
import br.com.marksonarguello.message.Message;
import com.google.gson.Gson;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MomPerfomanceTest {
    public static final int messageCount = 100000;
    public static final String topic = "test-topic";

    public static void main(String[] args) throws InterruptedException {
        if (args.length == 0) {
            System.out.println("Usage: java MomPerfomanceTest <producer|consumer>");
            return;
        }

        if (args[0].equalsIgnoreCase("producer")) {
            runProducer();
        } else if (args[0].equalsIgnoreCase("consumer")) {
            runConsumer();
        } else {
            System.out.println("Invalid argument! Use 'producer' or 'consumer'");
        }
    }

    public static void runProducer() {
        Producer producerTest = new Producer("localhost", "8080");
        Instant startTime = Instant.now(); // Start time of the producer
        for (int i = 1; i <= messageCount; i++) {
            Message message = new Message(topic, startTime.toString());
            //System.out.println(i);
            producerTest.sendMessage(topic, message);
        }
    }

    public static void runConsumer() throws InterruptedException {
        Consumer consumer = new Consumer("localhost", "8080", false);
        Set<String> topics = new HashSet<>();
        topics.add(topic);
        consumer.subscribe(topics);
        int messageCounter = 0;
        Instant firstMessageTime = null;

        while (true) {
            ConsumerRecord consumerRecord = consumer.poll();
            Thread.sleep(200);

            List<Message> messages = consumerRecord.records().get(topic);

            if (messages == null || messages.isEmpty()) {

                continue;
            }

            Message message = messages.get(messages.size() - 1);
            messageCounter += messages.size();
            Instant sentTime = Instant.parse(message.getValue());

            if (firstMessageTime == null) {
                firstMessageTime = sentTime;
            }

            Instant receivedTime = Instant.now();
            Duration duration = Duration.between(firstMessageTime, receivedTime);

            System.out.println("Message received: " + messageCounter);
            System.out.println("Elapsed Time: " + duration.toMillis() + " ms");

            if (messageCounter >= messageCount) {
                System.out.println("Elapsed Time: " + duration.toMillis() + " ms");
                return;
            }

        }
    }
}
