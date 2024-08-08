package br.com.marksonarguello.entities.baseEntity;

import br.com.marksonarguello.util.IdUtil;

import java.util.Objects;
public abstract class BaseEntity {
    private final String id = IdUtil.newId();

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity that)) return false;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}