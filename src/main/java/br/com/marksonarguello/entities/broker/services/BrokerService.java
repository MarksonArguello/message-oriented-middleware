package br.com.marksonarguello.entities.broker.services;

import br.com.marksonarguello.entities.broker.Broker;
import br.com.marksonarguello.entities.queue.MessageQueue;
import br.com.marksonarguello.entities.queue.dto.QueueCreateDTO;
import br.com.marksonarguello.entities.queue.services.QueueService;

import java.util.List;

public class BrokerService {
    private static BrokerService brokerService;
    private final Broker broker = Broker.getInstance();

    private final QueueService queueService = QueueService.getInstance();

    private BrokerService() {
    }

    public static BrokerService getInstance() {
        if (brokerService == null)
            brokerService = new BrokerService();

        return brokerService;
    }

    public String createQueue(QueueCreateDTO queueCreateDTO) {
        MessageQueue messageQueue = queueService.createQueue(queueCreateDTO);

        broker.createQueue(messageQueue);

        return broker.toString();
    }

    public List<String> findAllTopics() {
        return this.broker.getAllTopics();
    }
}
