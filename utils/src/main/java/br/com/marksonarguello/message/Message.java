package br.com.marksonarguello.message;

import br.com.marksonarguello.baseEntity.BaseEntity;


public class Message extends BaseEntity {

    public static final String TOPIC = "topic";
    public static final String TOPICS = "topics";
    private final String key;
    private final String value;


    public Message(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
