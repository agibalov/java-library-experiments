package me.loki2302.query;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TodoEntity {
    @Id
    public String id;
    public String text;
}
