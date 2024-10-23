package br.com.marksonarguello.entities.consumer;

import br.com.marksonarguello.consumer.ConsumerRecord;
import br.com.marksonarguello.message.Message;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Consumer {
    private String id;
    private final ConsumerConnector connector;
    private Set<String> topics = new HashSet<>();

    private Map<String, List<Message>> messages = new HashMap<>();

    public Consumer(String host, String port) {
        this.connector = new ConsumerConnector(host, port);
        this.id = this.register();
    }

    public String register() {
        try {
            return connector.register();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Erro ao gerar URL de registro de consumidor (%s) para middleware.".formatted(this.getId()));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler resposta do middleware no registro de consumidor (%s).".formatted(this.getId()));
        }
    }

    public ConsumerRecord poll() {
        try {
            ConsumerRecord record = connector.requestForMessages();

            for (String topic : record.records().keySet()) {
                List<Message> messages = record.records().get(topic);
                this.messages.get(topic).addAll(messages);
            }

            return record;
        } catch (URISyntaxException e) {
            throw new RuntimeException("Erro ao gerar URL de consumidor (%s) para middleware.".formatted(this.getId()));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler resposta do middleware no consumidor (%s).".formatted(this.getId()));
        }

    }

    public void subscribe(Collection<String> topics) {
        try {
            topics.removeAll(this.topics);
            connector.subscribe(topics.stream().toList());
            this.topics.addAll(topics);
            for (String topic : topics) {
                this.messages.put(topic, List.of());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler resposta do middleware no registro de consumidor (%s).".formatted(this.getId()));
        }
    }

    public String getId() {
        return this.id;
    }
}
