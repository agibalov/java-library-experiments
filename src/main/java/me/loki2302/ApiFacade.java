package me.loki2302;

import me.loki2302.domain.commands.CreateTodoItemCommand;
import me.loki2302.domain.commands.DeleteTodoItemCommand;
import me.loki2302.domain.commands.UpdateTodoItemCommand;
import me.loki2302.query.TodoEntityRepository;
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

    @Transactional
    public void createTodo(String id, String text) {
        commandGateway.send(new CreateTodoItemCommand(id, text));
    }

    @Transactional
    public void updateTodo(String id, String text) {
        commandGateway.send(new UpdateTodoItemCommand(id, text));
    }

    @Transactional
    public void deleteTodo(String id) {
        commandGateway.send(new DeleteTodoItemCommand(id));
    }

    @Transactional
    public long countTodos() {
        return todoEntityRepository.count();
    }
}
