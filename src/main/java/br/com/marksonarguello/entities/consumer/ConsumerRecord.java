package br.com.marksonarguello.entities.consumer;

import br.com.marksonarguello.entities.message.Message;

import java.util.HashMap;
import java.util.List;

public record ConsumerRecord(
        HashMap<String, List<Message>> records
) {
}
