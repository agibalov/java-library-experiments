package me.loki2302;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TodoItem extends AbstractAnnotatedAggregateRoot {
    private final static Logger LOGGER = LoggerFactory.getLogger(TodoItem.class);

    @AggregateIdentifier
    private String id;
    private String text;

    public TodoItem() {
    }

    @CommandHandler
    public TodoItem(CreateTodoItemCommand command) {
        LOGGER.info("in constructor, command={}", command);
        apply(new TodoItemCreatedEvent(command.todoId, command.text));
    }

    @CommandHandler
    public void on(UpdateTodoItemCommand command) {
        LOGGER.info("in update command handler, command={}", command);
        apply(new TodoItemUpdatedEvent(command.todoId, command.text));
    }

    @CommandHandler
    public void on(DeleteTodoItemCommand command) {
        LOGGER.info("in delete command handler, command={}", command);
        apply(new TodoItemDeletedEvent(command.todoId));
    }

    @EventHandler
    public void on(TodoItemCreatedEvent event) {
        LOGGER.info("in create event handler, event={}", event);
        id = event.todoId;
        text = event.text;
    }

    @EventHandler
    public void on(TodoItemUpdatedEvent event) {
        LOGGER.info("in update event handler, event={}", event);
        text = event.text;
    }

    @EventHandler
    public void on(TodoItemDeletedEvent event) {
        LOGGER.info("in delete event handler, event={}", event);
        // ?
    }
}
