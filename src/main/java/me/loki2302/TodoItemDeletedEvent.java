package me.loki2302;

public class TodoItemDeletedEvent {
    public final String todoId;

    public TodoItemDeletedEvent(String todoId) {
        this.todoId = todoId;
    }
}
