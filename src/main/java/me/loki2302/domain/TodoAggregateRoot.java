package me.loki2302.domain;

import me.loki2302.domain.commands.CreateTodoCommand;
import me.loki2302.domain.commands.DeleteTodoCommand;
import me.loki2302.domain.commands.UpdateTodoCommand;
import me.loki2302.domain.events.TodoCreatedEvent;
import me.loki2302.domain.events.TodoDeletedEvent;
import me.loki2302.domain.events.TodoUpdatedEvent;
import org.axonframework.commandhandling.annotation.CommandHandler;
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

    @CommandHandler
    public TodoAggregateRoot(CreateTodoCommand command) {
        LOGGER.info("in constructor, command={}", command);
        apply(new TodoCreatedEvent(command.todoId, command.text));
    }

    @CommandHandler
    public void on(UpdateTodoCommand command) {
        LOGGER.info("in update command handler, command={}", command);
        apply(new TodoUpdatedEvent(command.todoId, command.text));
    }

    @CommandHandler
    public void on(DeleteTodoCommand command) {
        LOGGER.info("in delete command handler, command={}", command);
        apply(new TodoDeletedEvent(command.todoId));
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
        // ?
    }
}
