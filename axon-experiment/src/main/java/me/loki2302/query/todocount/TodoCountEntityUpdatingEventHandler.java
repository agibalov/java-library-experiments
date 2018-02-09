package me.loki2302.query.todocount;

import me.loki2302.domain.events.TodoCreatedEvent;
import me.loki2302.domain.events.TodoDeletedEvent;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TodoCountEntityUpdatingEventHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(TodoCountEntityUpdatingEventHandler.class);
    public final static String SINGLETON_TODO_COUNT_ENTITY_ID = "singleton";

    @Autowired
    private TodoCountEntityRepository todoCountEntityRepository;

    @PostConstruct
    public void init() {
        TodoCountEntity todoCountEntity = todoCountEntityRepository.findOne(SINGLETON_TODO_COUNT_ENTITY_ID);
        if(todoCountEntity == null) {
            todoCountEntity = new TodoCountEntity();
            todoCountEntity.id = SINGLETON_TODO_COUNT_ENTITY_ID;
            todoCountEntity.count = 0;
            todoCountEntityRepository.save(todoCountEntity);
        }
    }

    @EventHandler
    public void handle(TodoCreatedEvent event) {
        TodoCountEntity todoCountEntity = todoCountEntityRepository.findOne(SINGLETON_TODO_COUNT_ENTITY_ID);
        ++todoCountEntity.count;
        todoCountEntityRepository.save(todoCountEntity);
    }

    @EventHandler
    public void handle(TodoDeletedEvent event) {
        TodoCountEntity todoCountEntity = todoCountEntityRepository.findOne(SINGLETON_TODO_COUNT_ENTITY_ID);
        --todoCountEntity.count;
        todoCountEntityRepository.save(todoCountEntity);
    }
}
