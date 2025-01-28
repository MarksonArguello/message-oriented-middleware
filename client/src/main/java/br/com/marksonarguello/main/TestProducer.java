package br.com.marksonarguello.main;

import br.com.marksonarguello.entities.producer.Producer;
import br.com.marksonarguello.message.Message;

public class TestProducer extends Thread {
    private Producer producer = new Producer("localhost", "8080");
    private String topic = "news";
    private int key = 1;

    public void run() {
        try {

            while (true) {
                Message message = new Message(String.valueOf(key), "Message from producer " + key);
                System.out.println("Sending message: " + message.getValue());
                producer.sendMessage(topic, message);
                System.out.println("Message sent: " + message.getValue());
                Thread.sleep(5000);
                key++;
            }
        } catch (InterruptedException e) {
           e.printStackTrace();
        }
    }
}
