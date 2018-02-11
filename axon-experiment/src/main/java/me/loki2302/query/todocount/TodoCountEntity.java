package me.loki2302.query.todocount;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TodoCountEntity {
    @Id
    public String id;
    public long count;
}
