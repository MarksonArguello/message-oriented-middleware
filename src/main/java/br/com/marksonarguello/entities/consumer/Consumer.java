package br.com.marksonarguello.entities.consumer;

import br.com.marksonarguello.entities.baseEntity.BaseEntity;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class Consumer extends BaseEntity {
    private Map<String, Integer> topicsOffset = new HashMap<>();
    private ConsumerConnector connector;

    public Consumer(String host, String port) {
        super();
        this.connector = new ConsumerConnector(super.getId(), host, port);
    }


    public ConsumerRecord poll() {
        try {
            return connector.poll();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao gerar URL de consumidor (%s) para middleware.".formatted(super.getId()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao ler resposta do middleware no consumidor (%s).".formatted(super.getId()));
        }

    }


}
