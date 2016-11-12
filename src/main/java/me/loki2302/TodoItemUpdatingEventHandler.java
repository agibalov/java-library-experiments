package me.loki2302;

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
    public void handle(TodoItemCreatedEvent event) {
        LOGGER.info("in create handler, event={}", event);

        TodoEntity todoEntity = new TodoEntity();
        todoEntity.id = event.todoId;
        todoEntity.text = event.text;
        todoEntityRepository.save(todoEntity);
    }

    @EventHandler
    public void handle(TodoItemUpdatedEvent event) {
        LOGGER.info("in update handler, event={}", event);

        TodoEntity todoEntity = todoEntityRepository.findOne(event.todoId);
        todoEntity.text = event.text;
        todoEntityRepository.save(todoEntity);
    }

    @EventHandler
    public void handle(TodoItemDeletedEvent event) {
        LOGGER.info("in delete handler, event={}", event);

        todoEntityRepository.delete(event.todoId);
    }
}
