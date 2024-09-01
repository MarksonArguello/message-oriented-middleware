package br.com.marksonarguello.entities.producer;

import br.com.marksonarguello.connect.HttpClient;
import br.com.marksonarguello.message.Message;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class ProducerConnector {
    public HttpClient httpClient = new HttpClient();
    private String producerId;
    private String host;
    private String port;

    public ProducerConnector(String producerId, String host, String port) {
        this.producerId = producerId;
        this.host = host;
        this.port = port;
    }

    protected void send(Message message, String topic) throws URISyntaxException, IOException {
        String resource = "/sendMessage";
        URI uri = new URI(getMiddlewareUrl(resource));

        Map<String, String> parameters = Map.of(
                Message.TOPIC, topic,
                "producerId", producerId
        );

        httpClient.request(uri, parameters, message);
    }

    private String getMiddlewareUrl(String resource) {
        return host + ":" + port + "/" + resource;
    }
}
