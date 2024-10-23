package br.com.marksonarguello.message;

import br.com.marksonarguello.baseEntity.BaseEntity;

import java.io.Serializable;


public class Message extends BaseEntity implements Serializable {

    public static final String TOPIC = "topic";
    public static final String TOPICS = "topics";
    private final String key;
    private final String value;


    public Message(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public Integer getSizeInBytes() {
        return key.getBytes().length + value.getBytes().length;
    }
}
