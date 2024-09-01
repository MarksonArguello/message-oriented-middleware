package br.com.marksonarguello.entities.consumer;

import br.com.marksonarguello.baseEntity.BaseEntity;
import br.com.marksonarguello.consumer.ConsumerRecord;

import java.io.IOException;
import java.net.URISyntaxException;

public class Consumer extends BaseEntity {

    private String host;
    private String port;
    private final ConsumerConnector connector;

    public Consumer(ConsumerConnector connector) {
        this.connector = new ConsumerConnector(super.getId(), host, port);
    }

    public ConsumerRecord poll() {
        try {
            return connector.requestForMessages();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Erro ao gerar URL de consumidor (%s) para middleware.".formatted(super.getId()));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler resposta do middleware no consumidor (%s).".formatted(super.getId()));
        }

    }

    private String getMiddlewareUrl() {
        return host + ":" + port;
    }
}
