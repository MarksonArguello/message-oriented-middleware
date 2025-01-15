package br.com.marksonarguello.main;

import br.com.marksonarguello.entities.consumer.Consumer;
import br.com.marksonarguello.message.Message;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Consumer consumer = new Consumer("localhost", "8080");
        consumer.subscribe(Arrays.asList("news"));

        TestProducer producer = new TestProducer();
        producer.start();

        try {
            while (true) {
                Message message = consumer.consumeMessage();
                System.out.println("Message received: " + message.getValue());
                System.out.println();

            }
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
