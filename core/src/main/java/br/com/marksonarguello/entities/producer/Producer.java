package br.com.marksonarguello.entities.producer;

import br.com.marksonarguello.message.Message;

import java.io.IOException;
import java.net.URISyntaxException;

public class Producer {
    private String id;
    private String host;
    private String port;

    private final ProducerConnector connector;

    public Producer(String id) {
        this.connector = new ProducerConnector(id, host, port);
    }

    public void send(Message message, String topic) {
        try {
            connector.send(message, topic);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao gerar URL de produtor (%s) para middleware.".formatted(id));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao ler resposta do middleware no produtor (%s).".formatted(id));
        }

    }
}
