package br.com.marksonarguello.entites.producer;

import br.com.marksonarguello.connect.ConnectionDTO;
import br.com.marksonarguello.connect.HttpClient;
import br.com.marksonarguello.message.Message;
import jakarta.ws.rs.HttpMethod;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ProducerConnector {
    private HttpClient httpClient = new HttpClient();
    private String producerId;
    private String host;
    private String port;

    public  ProducerConnector(String host, String port) {
        this.host = host;
        this.port = port;
    }

    public String register(ConnectionDTO connectionDTO) throws URISyntaxException, IOException {
        String url = getMiddlewareUrl() + "/register";
        URI uri = new URI(url);


        this.producerId = httpClient.request(uri,null,  String.class, HttpMethod.POST, connectionDTO);
        return this.producerId;
    }

    protected void sendMessage(String topic, Message message) throws IOException {
        String url = getMiddlewareUrl() + "/send?topic=" + topic;
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

       httpClient.request(uri, null, message);
    }


    private String getMiddlewareUrl() {
        return "http://" + host + ":" + port + "/mom";
    }
}
