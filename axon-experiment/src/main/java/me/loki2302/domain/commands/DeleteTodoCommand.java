package me.loki2302.domain.commands;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

public class DeleteTodoCommand {
    @TargetAggregateIdentifier
    public final String todoId;

    public DeleteTodoCommand(String todoId) {
        this.todoId = todoId;
    }
}
