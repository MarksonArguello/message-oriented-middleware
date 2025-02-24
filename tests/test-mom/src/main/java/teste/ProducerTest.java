package teste;

import br.com.marksonarguello.entities.producer.Producer;
import br.com.marksonarguello.message.Message;
import com.google.gson.Gson;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ProducerTest {
    private int key;
    private Producer producerTest = new Producer("localhost", "8080");
    private LoggerWriter log;
    private List<String> topics;
    private Random random = new Random();

    public ProducerTest(int key, Collection<String> topics) {
        this.key = key;
        this.log = new LoggerWriter(getFileName());
        this.topics = List.copyOf(topics);
    }

    public void sendMessages() {
        Gson gson = new Gson();
        try {
            for (int i = 1; i <= 10; i++) {
                for (String topic : topics) {
                    Map<String, String> messageMap = Map.of(
                            "topic", topic,
                            "producer", String.valueOf(key),
                            "message", String.valueOf(i)
                    );

                    Message message = new Message(String.valueOf(key), gson.toJson(messageMap));
                    producerTest.sendMessage(topic, message);
                    log.info(message.getValue() + " to topic: " + topic);
                    Thread.sleep(random.nextLong(3000));
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String getFileName() {
        return "logs/producer_" + key + ".txt";
    }
}
