package br.com.marksonarguello.consumer;


import br.com.marksonarguello.message.Message;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public record ConsumerRecord(
       Map<String, List<Message>> records
)  implements Serializable {
}
