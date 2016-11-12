package me.loki2302.domain.commands;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

public class UpdateTodoCommand {
    @TargetAggregateIdentifier
    public final String todoId;
    public final String text;

    public UpdateTodoCommand(String todoId, String text) {
        this.todoId = todoId;
        this.text = text;
    }
}
