package br.com.marksonarguello.entities.consumer;

import br.com.marksonarguello.consumer.ConsumerRecord;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.ServerSocket;

public class ConnectionSocket extends Thread {
    public static final int PORT = 10004;
    private ServerSocket serverSocket;
    private Consumer consumer;
    public ConnectionSocket(Consumer consumer) throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        this.consumer = consumer;
    }

    public void run() {
        try(
                Socket clientSocket = serverSocket.accept();
                ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream())
        ) {
            ConsumerRecord consumerRecord = (ConsumerRecord) objectInputStream.readObject();
            while (consumerRecord != null) {
                consumer.addConsumerRecordInMessages(consumerRecord);
                consumerRecord = (ConsumerRecord) objectInputStream.readObject();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
