package br.com.marksonarguello.entities.queue.dto;

import br.com.marksonarguello.entities.queue.MessageQueue;

public final class QueueMapper {

    private QueueMapper() {
    }

    public static MessageQueue toEntity(QueueCreateDTO queueCreateDTO) {
        return new MessageQueue(queueCreateDTO.topic());
    }
}
