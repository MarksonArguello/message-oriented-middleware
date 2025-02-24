package teste;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            throw new RuntimeException("No arguments provided arguments should be: consumer or producer");
        }

        int i = 0;

        String type = args[i++];
        int number = Integer.parseInt(args[i++]);
        String deliveryMode = args[i++];
        String queueType = args[i++];

        if (type.equals("consumer")) {
            Set<String> topics = new HashSet<>(Arrays.asList(args).subList(i, args.length));
            ConsumerTest consumerTest = new ConsumerTest(number, deliveryMode, queueType, topics, false);
            consumerTest.consumeMessages();
        } else if (type.equals("producer")) {
            Set<String> topics = new HashSet<>(Arrays.asList(args).subList(i, args.length));
            ProducerTest producerTest = new ProducerTest(number, topics);
            producerTest.sendMessages();
        } else {
            System.out.println("Invalid argument, arguments should be: consumer or producer");
        }
        System.exit(0);
    }
}