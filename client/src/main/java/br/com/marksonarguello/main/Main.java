package br.com.marksonarguello.main;


public class Main {

    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            TestConsumer consumer = new TestConsumer(i);
            consumer.start();
        }

        TestProducer producer = new TestProducer();
        producer.start();
    }
}
