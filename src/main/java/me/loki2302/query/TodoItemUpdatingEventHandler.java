package me.loki2302.query;

import me.loki2302.domain.events.TodoCreatedEvent;
import me.loki2302.domain.events.TodoDeletedEvent;
import me.loki2302.domain.events.TodoUpdatedEvent;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TodoItemUpdatingEventHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(TodoItemUpdatingEventHandler.class);

    @Autowired
    private TodoEntityRepository todoEntityRepository;

    @EventHandler
    public void handle(TodoCreatedEvent event) {
        LOGGER.info("in create handler, event={}", event);

        TodoEntity todoEntity = new TodoEntity();
        todoEntity.id = event.todoId;
        todoEntity.text = event.text;
        todoEntityRepository.save(todoEntity);
    }

    @EventHandler
    public void handle(TodoUpdatedEvent event) {
        LOGGER.info("in update handler, event={}", event);

        TodoEntity todoEntity = todoEntityRepository.findOne(event.todoId);
        todoEntity.text = event.text;
        todoEntityRepository.save(todoEntity);
    }

    @EventHandler
    public void handle(TodoDeletedEvent event) {
        LOGGER.info("in delete handler, event={}", event);

        todoEntityRepository.delete(event.todoId);
    }
}
