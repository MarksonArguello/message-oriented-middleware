package br.com.marksonarguello.entities.broker.services;

import br.com.marksonarguello.entities.broker.Broker;
import br.com.marksonarguello.entities.queue.MessageQueue;
import br.com.marksonarguello.entities.queue.dto.QueueCreateDTO;
import br.com.marksonarguello.entities.queue.services.QueueService;

public class BrokerService {

    private final Broker broker = Broker.getInstance();

    private final QueueService queueService = QueueService.getInstance();

    public String createQueue(QueueCreateDTO queueCreateDTO) {
        MessageQueue messageQueue = queueService.createQueue(queueCreateDTO);

       broker.createQueue(messageQueue);

        return broker.toString();
    }
}
