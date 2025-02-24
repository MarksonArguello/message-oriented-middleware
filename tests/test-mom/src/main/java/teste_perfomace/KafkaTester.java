package teste_perfomace;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

public class KafkaTester {
    private static final String TOPIC = "test-topic";
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    public static void main(String[] args) throws InterruptedException {
        if (args.length == 0) {
            System.out.println("Usage: java KafkaTest <producer|consumer>");
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

    public static final int messageCount = 100000;

    public static void runProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        Instant startTime = Instant.now(); // Start time of the producer

        for (int i = 0; i < messageCount; i++) {
            String message = startTime.toString() + " | Message " + i;
            producer.send(new ProducerRecord<>(TOPIC, UUID.randomUUID().toString(), message));
            System.out.println("Sent: " + message);
        }

        producer.close();
    }

    public static void runConsumer() throws InterruptedException {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(TOPIC));

        Instant firstMessageTime = null;
        int messageCounter = 0;

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            Thread.sleep(200);

            for (ConsumerRecord<String, String> record : records) {
                String[] parts = record.value().split(" \\| ");
                Instant sentTime = Instant.parse(parts[0]); // Extract time from message

                if (firstMessageTime == null) {
                    firstMessageTime = sentTime;
                }

                messageCounter++;
                Instant receivedTime = Instant.now();
                Duration duration = Duration.between(firstMessageTime, receivedTime);

                System.out.println("Received: " + record.value());

                if (messageCounter >= messageCount) {
                    System.out.println("Elapsed Time: " + duration.toMillis() + " ms");
                    consumer.close();
                    return;
                }
            }
        }
    }
}
