package me.loki2302.domain.commands;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

public class DeleteTodoItemCommand {
    @TargetAggregateIdentifier
    public final String todoId;

    public DeleteTodoItemCommand(String todoId) {
        this.todoId = todoId;
    }
}
