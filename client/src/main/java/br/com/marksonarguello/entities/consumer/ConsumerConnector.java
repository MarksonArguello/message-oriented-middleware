package br.com.marksonarguello.entities.consumer;

import br.com.marksonarguello.connect.ConnectionDTO;
import br.com.marksonarguello.connect.HttpClient;
import br.com.marksonarguello.consumer.ConsumerRecord;
import jakarta.ws.rs.HttpMethod;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public class ConsumerConnector {

    private HttpClient httpClient = new HttpClient();
    private String consumerId;
    private String host;
    private String port;
    public ConsumerConnector(String host, String port) {
        this.host = host;
        this.port = port;
    }

    public String register(ConnectionDTO connectionDTO) throws URISyntaxException, IOException {
        String url = getMiddlewareUrl() + "/register";
        URI uri = new URI(url);

        this.consumerId = httpClient.request(uri,null,  String.class, HttpMethod.POST, connectionDTO);
        return this.consumerId;
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
        return "http://" + host + ":" + port + "/mom";
    }

    public String subscribe(List<String> topics) throws IOException {
        String url = getMiddlewareUrl() + "/subscribe" + "?id=" + consumerId;
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return httpClient.request(uri, null, topics);
    }
}
