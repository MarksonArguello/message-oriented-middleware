package br.com.marksonarguello.entites.producer;


import br.com.marksonarguello.message.Message;

public class Producer extends Thread {

    private ProducerConnector connector;

    public Producer(String host, String port)  {
        this.connector = new ProducerConnector(host, port);
    }

    public void sendMessage(String topic, Message message) {
        try {
            connector.sendMessage(topic, message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
