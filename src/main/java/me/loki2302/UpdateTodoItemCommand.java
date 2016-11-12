package me.loki2302;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

public class UpdateTodoItemCommand {
    @TargetAggregateIdentifier
    public final String todoId;
    public final String text;

    public UpdateTodoItemCommand(String todoId, String text) {
        this.todoId = todoId;
        this.text = text;
    }
}
