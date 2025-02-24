package br.com.marksonarguello.main;

import br.com.marksonarguello.entities.consumer.Consumer;
import br.com.marksonarguello.message.Message;

import java.util.HashSet;
import java.util.Set;

public class TestConsumer extends Thread {
    private Consumer consumer = new Consumer("localhost", "8080", false);
    private int key;

    public TestConsumer(int key) {
        this.key = key;
        consumer.subscribe(new HashSet<>(Set.of("news")));
    }

    public void run() {
        try {
            while (true) {
                System.out.println("Waiting for messages...");
                Message message = consumer.consumeMessage();
                System.out.println("Consumer " + key + " received message: " + message.getValue());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
