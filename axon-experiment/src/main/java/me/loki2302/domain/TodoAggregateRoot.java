package me.loki2302.domain;

import me.loki2302.domain.events.TodoCreatedEvent;
import me.loki2302.domain.events.TodoDeletedEvent;
import me.loki2302.domain.events.TodoUpdatedEvent;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TodoAggregateRoot extends AbstractAnnotatedAggregateRoot {
    private final static Logger LOGGER = LoggerFactory.getLogger(TodoAggregateRoot.class);

    @AggregateIdentifier
    private String id;
    private String text;

    public TodoAggregateRoot() {
    }

    public void create(String id, String text) {
        apply(new TodoCreatedEvent(id, text));
    }

    public void update(String text) {
        apply(new TodoUpdatedEvent(id, text));
    }

    public void delete() {
        apply(new TodoDeletedEvent(id));
    }

    @EventHandler
    public void on(TodoCreatedEvent event) {
        LOGGER.info("in create event handler, event={}", event);
        id = event.todoId;
        text = event.text;
    }

    @EventHandler
    public void on(TodoUpdatedEvent event) {
        LOGGER.info("in update event handler, event={}", event);
        text = event.text;
    }

    @EventHandler
    public void on(TodoDeletedEvent event) {
        LOGGER.info("in delete event handler, event={}", event);
        markDeleted();
    }
}
