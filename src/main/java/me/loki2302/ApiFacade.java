package me.loki2302;

import me.loki2302.domain.commands.CreateTodoCommand;
import me.loki2302.domain.commands.DeleteTodoCommand;
import me.loki2302.domain.commands.UpdateTodoCommand;
import me.loki2302.query.todocount.TodoCountEntity;
import me.loki2302.query.todocount.TodoCountEntityRepository;
import me.loki2302.query.todocount.TodoCountEntityUpdatingEventHandler;
import me.loki2302.query.todo.TodoEntityRepository;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ApiFacade {
    @Autowired
    private CommandGateway commandGateway;

    @Autowired
    private TodoEntityRepository todoEntityRepository;

    @Autowired
    private TodoCountEntityRepository todoCountEntityRepository;

    @Transactional
    public void createTodo(String id, String text) {
        commandGateway.send(new CreateTodoCommand(id, text));
    }

    @Transactional
    public void updateTodo(String id, String text) {
        commandGateway.send(new UpdateTodoCommand(id, text));
    }

    @Transactional
    public void deleteTodo(String id) {
        commandGateway.send(new DeleteTodoCommand(id));
    }

    @Transactional
    public long countTodos() {
        return todoEntityRepository.count();
    }

    @Transactional
    public long countTodos2() {
        TodoCountEntity todoCountEntity =
                todoCountEntityRepository.findOne(TodoCountEntityUpdatingEventHandler.SINGLETON_TODO_COUNT_ENTITY_ID);
        return todoCountEntity.count;
    }
}
