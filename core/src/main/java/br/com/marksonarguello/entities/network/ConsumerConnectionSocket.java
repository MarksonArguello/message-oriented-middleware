package br.com.marksonarguello.entities.network;

import br.com.marksonarguello.consumer.ConsumerRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class ConsumerConnectionSocket implements Serializable {
    private Socket clientSocket;
    private ObjectOutputStream out;
    private BufferedReader in;

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void sendMessages(ConsumerRecord consumerRecord) throws IOException {
        out.writeObject(consumerRecord);
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}
