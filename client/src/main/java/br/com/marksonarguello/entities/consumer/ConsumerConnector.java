package br.com.marksonarguello.entities.consumer;

import br.com.marksonarguello.connect.HttpClient;
import br.com.marksonarguello.consumer.ConsumerRecord;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class ConsumerConnector {

    private HttpClient httpClient = new HttpClient();
    private String consumerId;
    private String host;
    private String port;

    public ConsumerConnector(String consumerId, String host, String port) {
        this.consumerId = consumerId;
        this.host = host;
        this.port = port;
    }

    protected ConsumerRecord requestForMessages() throws URISyntaxException, IOException {
        String url = getMiddlewareUrl() + "/poll";
        URI uri = new URI(getMiddlewareUrl());

        Map<String, String> parameters = Map.of(
                "consumerId", consumerId
        );

        return httpClient.request(uri, parameters, ConsumerRecord.class);
    }

    private String getMiddlewareUrl() {
        return host + ":" + port;
    }

}
