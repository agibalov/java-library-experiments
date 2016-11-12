package me.loki2302;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

public class CreateTodoItemCommand {
    @TargetAggregateIdentifier
    public final String todoId;
    public final String text;

    public CreateTodoItemCommand(String todoId, String text) {
        this.todoId = todoId;
        this.text = text;
    }
}
