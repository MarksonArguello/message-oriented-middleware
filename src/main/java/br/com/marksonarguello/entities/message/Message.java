package br.com.marksonarguello.entities.message;

import br.com.marksonarguello.entities.baseEntity.BaseEntity;


public class Message extends BaseEntity {
    private final String content;

    public Message(String content) {
        this.content = content;
    }


}
