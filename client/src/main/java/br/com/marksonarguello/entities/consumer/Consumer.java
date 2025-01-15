package br.com.marksonarguello.entities.consumer;

import br.com.marksonarguello.connect.ConnectionDTO;
import br.com.marksonarguello.consumer.ConsumerRecord;
import br.com.marksonarguello.message.Message;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Consumer  {
    private String id;
    private final ConsumerConnector connector;
    private Set<String> topics = new HashSet<>();

    private Map<String, List<Message>> messages = new HashMap<>();
    private Thread thread  = Thread.currentThread();
    private ConnectionSocket connectionSocket;

    private Lock lock = new ReentrantLock();

    public Consumer(String host, String port)  {
        this.connector = new ConsumerConnector(host, port);
        initSocketConnection();
        this.id = this.register();

    }

    private void initSocketConnection() {
        try {
            this.connectionSocket = new ConnectionSocket(this);
            this.connectionSocket.start();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao iniciar conexão de socket para consumidor");
        }

    }

    private String register() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            var consumerConnectionDTO = new ConnectionDTO(
                    localHost.getHostAddress(),
                    ConnectionSocket.PORT
            );
            return connector.register(consumerConnectionDTO);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao gerar URL de registro de consumidor (%s) para middleware.".formatted(this.getConsumerId()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao ler resposta do middleware no registro de consumidor (%s).".formatted(this.getConsumerId()));
        }
    }

    public ConsumerRecord poll() {
        try {
            ConsumerRecord record = connector.requestForMessages();

            return record;
        } catch (URISyntaxException e) {
            throw new RuntimeException("Erro ao gerar URL de consumidor (%s) para middleware.".formatted(this.getConsumerId()));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler resposta do middleware no consumidor (%s).".formatted(this.getConsumerId()));
        }

    }

    protected void addConsumerRecordInMessages(ConsumerRecord record) {
        lock.lock();
        for (String topic : record.records().keySet()) {
            List<Message> messages = record.records().get(topic);
            this.messages.get(topic).addAll(messages);
            synchronized (this) {
                this.notify();
            }
        }
        lock.unlock();
    }

    public synchronized Message consumeMessage() throws InterruptedException {
        lock.lock();
        while (true) {
            for (String topic : this.topics) {
                if (!this.messages.get(topic).isEmpty()) {
                    Message message = this.messages.get(topic).remove(0);
                    lock.unlock();
                    return message;
                }
            }
            synchronized (this) {
                lock.unlock();
                System.out.println("Waiting for messages...");
                this.wait();
                lock.lock();
            }
        }
    }

    public void subscribe(Collection<String> topics) {
        try {
            topics.removeAll(this.topics);
            connector.subscribe(topics.stream().toList());
            this.topics.addAll(topics);
            for (String topic : topics) {
                this.messages.put(topic, new ArrayList<>());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao ler resposta do middleware no registro de consumidor (%s).".formatted(this.getConsumerId()));
        }
    }

    public String getConsumerId() {
        return this.id;
    }
}
