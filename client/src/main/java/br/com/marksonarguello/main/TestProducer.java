package br.com.marksonarguello.main;

import br.com.marksonarguello.entites.producer.Producer;
import br.com.marksonarguello.message.Message;

public class TestProducer extends Thread {
    private Producer producer = new Producer("localhost", "8080");
    private String topic = "news";
    private int key = 1;

    public void run() {
        while (true) {
            Message message = new Message(String.valueOf(key), "Message from producer");
            System.out.println("Sending message: " + message.getValue());
            producer.sendMessage(topic, message);
            System.out.println("Message sent: " + message.getValue());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            key++;
        }
    }
}
