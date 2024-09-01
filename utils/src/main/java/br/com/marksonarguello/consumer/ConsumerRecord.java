package br.com.marksonarguello.consumer;


import br.com.marksonarguello.message.Message;

import java.util.List;
import java.util.Map;

public record ConsumerRecord(
       Map<String, List<Message>> records
) {
}
