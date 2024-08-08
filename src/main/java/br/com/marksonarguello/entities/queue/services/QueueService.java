package br.com.marksonarguello.entities.queue.services;

import br.com.marksonarguello.entities.queue.MessageQueue;
import br.com.marksonarguello.entities.queue.dto.QueueCreateDTO;
import br.com.marksonarguello.entities.queue.dto.QueueMapper;

public class QueueService {

    private static QueueService queueService;

    private QueueService() {
    }

    public static QueueService getInstance() {
        if (queueService == null) {
            queueService = new QueueService();
        }
        return queueService;
    }

    public MessageQueue createQueue(QueueCreateDTO queueCreateDTO) {

        return QueueMapper.toEntity(queueCreateDTO);
    }

}
