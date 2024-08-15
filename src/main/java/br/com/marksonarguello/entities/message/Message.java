package br.com.marksonarguello.entities.message;

import br.com.marksonarguello.entities.baseEntity.BaseEntity;


public class Message extends BaseEntity {
    private final String key;
    private final String value;


    public Message(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
